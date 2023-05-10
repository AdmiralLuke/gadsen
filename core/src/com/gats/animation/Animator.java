package com.gats.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gats.animation.action.*;
import com.gats.animation.action.Action;
import com.gats.animation.action.uiActions.*;
import com.gats.animation.entity.*;
import com.gats.manager.AnimationLogProcessor;
import com.gats.simulation.*;
import com.gats.simulation.action.*;
import com.gats.ui.assets.AssetContainer.IngameAssets;
import com.gats.ui.assets.AssetContainer.IngameAssets.GameCharacterAnimationType;
import com.gats.ui.hud.UiMessenger;


import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import com.gats.simulation.GameState.GameMode;

/**
 * Kernklasse für die Visualisierung des Spielgeschehens.
 * Übersetzt {@link com.gats.simulation.GameState GameState} und {@link ActionLog ActionLog}
 * des {@link com.gats.simulation Simulation-Package} in für libGDX renderbare Objekte
 */
public class Animator implements Screen, AnimationLogProcessor {
    private AnimatorCamera camera;
    private GameState state;

    private GameCharacter activeCharacter;

    private Viewport viewport;
    private Viewport backgroundViewport;

    private SpriteEntity background;
    private final GameMode gameMode;
    private final UiMessenger uiMessenger;

    private final Batch batch;

    private final EntityGroup root;

    private TileMap map;

    private final BlockingQueue<ActionLog> pendingLogs = new LinkedBlockingQueue<>();


    private GameCharacter[][] teams;

    private final Object notificationObject = new Object();

    private static final Color[] teamColors = new Color[]{
            Color.BLUE,
            Color.RED,
            Color.YELLOW,
            Color.GREEN,
            Color.CYAN,
            Color.PINK,
            Color.ORANGE,
            Color.PURPLE,
            Color.WHITE,
            Color.BLACK
    };

    private int teamCount;
    private int charactersPerTeam;
    private final List<Action> actionList = new LinkedList<>();

    private EntityGroup characterGroup;

    public AnimatorCamera getCamera() {
        return this.camera;
    }


    interface ActionConverter {
        public ExpandedAction apply(com.gats.simulation.action.Action simAction, Animator animator);
    }

    private static class Remainder {
        private float remainingDelta;
        private Action[] actions;

        public Remainder(float remainingDelta, Action[] actions) {
            this.remainingDelta = remainingDelta;
            this.actions = actions;
        }

        public float getRemainingDelta() {
            return remainingDelta;
        }

        public Action[] getActions() {
            return actions;
        }
    }

    /**
     * One Simulation Action may be sliced into multiple Animation Actions to keep generalization high
     */
    private static class ExpandedAction {
        Action head;
        Action tail;

        public ExpandedAction(Action head) {
            this.head = head;
            this.tail = head;
        }

        public ExpandedAction(Action head, Action tail) {
            this.head = head;
            this.tail = tail;
        }
    }

    public static class ActionConverters {

        private static final Map<Class<?>, ActionConverter> map =
                new HashMap<Class<?>, ActionConverter>() {
                    {
                        put(CharacterWalkAction.class, ActionConverters::convertCharacterWalkAction);
                        put(ProjectileAction.class, ActionConverters::convertProjectileMoveAction);
                        put(TileMoveAction.class, ActionConverters::convertTileMoveAction);
                        put(TileDestroyAction.class, ActionConverters::convertTileDestroyAction);
                        put(CharacterFallAction.class, ActionConverters::convertCharacterFallAction);
                        put(CharacterAimAction.class, ActionConverters::convertCharacterAimAction);
                        put(InitAction.class, ((simAction, animator) -> new ExpandedAction(new IdleAction(0, 0))));
                        put(TurnStartAction.class, ActionConverters::convertTurnStartAction);
                        put(CharacterSwitchWeaponAction.class, ActionConverters::convertCharacterSwitchWeaponAction);
                        put(CharacterShootAction.class, ActionConverters::convertCharacterShootAction);
                        put(CharacterHitAction.class, ActionConverters::convertCharacterHitAction);
                        put(GameOverAction.class, ActionConverters::convertGameOverAction);
                        put(DebugPointAction.class, ActionConverters::convertDebugPointAction);
                        put(CharacterMoveAction.class, ActionConverters::convertCharacterMoveAction);
                    }
                };


        public static Action convert(com.gats.simulation.action.Action simAction, Animator animator) {
//            System.out.println("Converting " + simAction.getClass());
            ExpandedAction expandedAction = map.getOrDefault(simAction.getClass(), (v, w) -> {
                        System.err.println("Missing Converter for Action of type " + simAction.getClass());
                        return new ExpandedAction(new IdleAction(simAction.getDelay(), 0));
                    })
                    .apply(simAction, animator);
            expandedAction.tail.setChildren(extractChildren(simAction, animator));

            return expandedAction.head;
        }

        private static Action[] extractChildren(com.gats.simulation.action.Action action, Animator animator) {
            int childCount = action.getChildren().size();
            if (childCount == 0) return new Action[]{};

            Action[] children = new Action[childCount];
            int i = 0;
            Iterator<com.gats.simulation.action.Action> iterator = action.iterator();
            while (iterator.hasNext()) {
                com.gats.simulation.action.Action curChild = iterator.next();
                children[i] = convert(curChild, animator);
                i++;
            }

            return children;
        }

        private static ExpandedAction convertCharacterWalkAction(com.gats.simulation.action.Action action, Animator animator) {
            CharacterWalkAction moveAction = (CharacterWalkAction) action;

            GameCharacter target = animator.teams[moveAction.getTeam()][moveAction.getCharacter()];
            SetAnimationAction startWalking = new SetAnimationAction(action.getDelay(), target, GameCharacterAnimationType.ANIMATION_TYPE_WALKING);

            MoveAction animMoveAction = new MoveAction(0, target, moveAction.getDuration(), new CharacterPath(moveAction.getPath()));


            DestroyAction<ParticleEntity> destroyParticle = new DestroyAction<ParticleEntity>(moveAction.getDuration(), null, null, (entity) -> {
                target.remove(entity);
                entity.free();
            });

            SummonAction<ParticleEntity> summonParticle = new SummonAction<ParticleEntity>(0, destroyParticle::setTarget, () -> {
                ParticleEntity particleEntity = ParticleEntity.getParticleEntity(IngameAssets.walkParticle);
                target.add(particleEntity);
                particleEntity.setLoop(true);
                particleEntity.setRelPos(0, -3);
                return particleEntity;
            });
            summonParticle.setChildren(new Action[]{destroyParticle});
            //rotateAction to set the angle/direction of movement, to flip the character sprite
            RotateAction animRotateAction = new RotateAction(0, target, moveAction.getDuration(), moveAction.getPath());
            startWalking.setChildren(new Action[]{animMoveAction, animRotateAction, summonParticle});
            SetAnimationAction stopWalking = new SetAnimationAction(0, target, GameCharacterAnimationType.ANIMATION_TYPE_IDLE);
            //notify ui
            MessageUiPlayerMoveAction messageUiPlayerMoveAction = new MessageUiPlayerMoveAction(0, animator.uiMessenger, animator.state.getCharacterFromTeams(moveAction.getTeam(), moveAction.getCharacter()));
            animMoveAction.setChildren(new Action[]{stopWalking, messageUiPlayerMoveAction});
            return new ExpandedAction(startWalking, messageUiPlayerMoveAction);
        }

        private static ExpandedAction convertCharacterFallAction(com.gats.simulation.action.Action action, Animator animator) {
            CharacterFallAction moveAction = (CharacterFallAction) action;

            GameCharacter target = animator.teams[moveAction.getTeam()][moveAction.getCharacter()];
            SetAnimationAction startFalling = new SetAnimationAction(action.getDelay(), target, GameCharacterAnimationType.ANIMATION_TYPE_FALLING);
            MoveAction animMoveAction = new MoveAction(0, target, moveAction.getDuration(), new CharacterPath(moveAction.getPath()));
            startFalling.setChildren(new Action[]{animMoveAction});
            SetAnimationAction stopFalling = new SetAnimationAction(0, target, GameCharacterAnimationType.ANIMATION_TYPE_IDLE);
            animMoveAction.setChildren(new Action[]{stopFalling});

            return new ExpandedAction(startFalling, stopFalling);
        }

        private static ExpandedAction convertProjectileMoveAction(com.gats.simulation.action.Action action, Animator animator) {
            ProjectileAction projectileAction = (ProjectileAction) action;
            Path path = projectileAction.getPath();


            MoveAction moveProjectile = new MoveAction(0, null, path.getDuration(), path);
            RotateAction rotateProjectile = new RotateAction(0, null, path.getDuration(), path);

            DestroyAction<Entity> destroyProjectile = new DestroyAction<Entity>(0, null, null, animator.root::remove);

            SummonAction<Entity> summonProjectile = new SummonAction<Entity>(action.getDelay(), target -> {
                moveProjectile.setTarget(target);
                rotateProjectile.setTarget(target);
                destroyProjectile.setTarget(target);
            }, () -> {
                Entity projectile = Projectiles.summon(projectileAction.getType());
                animator.root.add(projectile);
                return projectile;
            });

            //The Projectile should be moved after being summoned
            summonProjectile.setChildren(new Action[]{moveProjectile, rotateProjectile});
            ExpandedAction particleAction;
            switch (projectileAction.getType()) {
                case WATERBOMB:
                    particleAction = addParticle(IngameAssets.splashParticle, path.getPos(path.getDuration()), 4f, animator);
                    moveProjectile.setChildren(new Action[]{destroyProjectile, particleAction.head});
                    break;
                case GRENADE:
                    //ToDo fix first explosion
                    particleAction = addParticle(IngameAssets.explosionParticle, path.getPos(path.getDuration()), 5f, animator);
                    moveProjectile.setChildren(new Action[]{destroyProjectile, particleAction.head});
                    break;
                default:
                    //The Projectile should get destroyed at the end of its path
                    moveProjectile.setChildren(new Action[]{destroyProjectile});
            }

            //We sliced the projectile Action: Summon is now the first and Destroy the last Action with Move in between
            return new ExpandedAction(summonProjectile, destroyProjectile);
        }

        private static ExpandedAction addParticle(ParticleEffectPool effect, Vector2 pos, float duration, Animator animator){
            DestroyAction<ParticleEntity> destroyParticle = new DestroyAction<ParticleEntity>(duration, null, null, (entity) -> {
            animator.root.remove(entity);
            entity.free();
        });

            SummonAction<ParticleEntity> summonParticle = new SummonAction<ParticleEntity>(0, destroyParticle::setTarget, () -> {
                ParticleEntity particleEntity = ParticleEntity.getParticleEntity(effect);
                animator.root.add(particleEntity);
                particleEntity.setLoop(false);
                particleEntity.setRelPos(pos);
                return particleEntity;
            });
            summonParticle.setChildren(new Action[]{destroyParticle});
            return new ExpandedAction(summonParticle, destroyParticle);
        }

        private static ExpandedAction convertTileMoveAction(com.gats.simulation.action.Action action, Animator animator) {
            TileMoveAction tileMoveAction = (TileMoveAction) action;

            final IntVector2 IntPos = tileMoveAction.getPos();
            final IntVector2 IntPosAfter = tileMoveAction.getPosAfter();
            AtomicInteger tileType = new AtomicInteger(-1);

            MoveAction moveTileEntity = new MoveAction(0, null, tileMoveAction.getDuration(), tileMoveAction.getPath());

            DestroyAction<Entity> destroyTileEntity = new DestroyAction<Entity>(0, null, null, child -> {
                animator.root.remove(child);
                animator.map.setTile(IntPosAfter, tileType.intValue());
            });

            SummonAction<Entity> summonTileEntity = new SummonAction<Entity>(action.getDelay(), target -> {
                moveTileEntity.setTarget(target);
                destroyTileEntity.setTarget(target);
            }, () -> {
                tileType.set(animator.map.getTile(IntPos));
                animator.map.setTile(IntPos, TileMap.TYLE_TYPE_NONE);
                if (tileType.intValue() != TileMap.TYLE_TYPE_NONE) {
                    TextureRegion tex = IngameAssets.tileTextures[tileType.intValue()];
                    SpriteEntity projectile = new SpriteEntity(tex);//tileType.intValue()]);
                    projectile.setSize(new Vector2(tex.getRegionWidth(), tex.getRegionHeight()));
                    animator.root.add(projectile);
                    return projectile;
                }
                return new Entity();
            });

            summonTileEntity.setChildren(new Action[]{moveTileEntity});
            moveTileEntity.setChildren(new Action[]{destroyTileEntity});
            return new ExpandedAction(summonTileEntity, destroyTileEntity);
        }

        private static ExpandedAction convertTileDestroyAction(com.gats.simulation.action.Action action, Animator animator) {

            TileDestroyAction destroyAction = (TileDestroyAction) action;

            DestroyAction<Entity> destroyProjectile = new DestroyAction<Entity>(IngameAssets.destroyTileAnimation.getAnimationDuration(), null, null, animator.root::remove);

            SummonAction<Entity> summonProjectile = new SummonAction<Entity>(action.getDelay(), destroyProjectile::setTarget, () -> {
                animator.map.setTile(destroyAction.getPos(), TileMap.TYLE_TYPE_NONE);
                Entity particle = new AnimatedEntity(IngameAssets.destroyTileAnimation);
                particle.setRelPos(destroyAction.getPos().toFloat().scl(animator.map.getTileSize()));
                animator.root.add(particle);
                return particle;
            });

            summonProjectile.setChildren(new Action[]{destroyProjectile});

            return new ExpandedAction(summonProjectile, destroyProjectile);
        }

        /**
         * Converts a {@link CharacterAimAction} to adjust the rotation and scale of its {@link AimIndicator}.
         *
         * @param action   Aim Action to be converted
         * @param animator Current Animator
         * @return {@link ExpandedAction} with a {@link RotateAction} and {@link ScaleAction}.
         */
        private static ExpandedAction convertCharacterAimAction(com.gats.simulation.action.Action action, Animator animator) {
            //cast to access CharacterAimAction values
            CharacterAimAction aimAction = (CharacterAimAction) action;
            //get the aimIndicator from the player that shot
            AimIndicator currentAimIndicator = animator.teams[aimAction.getTeam()][aimAction.getCharacter()].getAimingIndicator();
            RotateAction rotateAction = new RotateAction(0, currentAimIndicator, aimAction.getAngle());
            ScaleAction scaleAction = new ScaleAction(0, currentAimIndicator, new Vector2(aimAction.getStrength(), 1));

            //notify Ui
            MessageUiPlayerAimAction aimValuesAction = new MessageUiPlayerAimAction(0, animator.uiMessenger, aimAction.getAngle().angleDeg(), aimAction.getStrength());
            rotateAction.setChildren(new Action[]{scaleAction, aimValuesAction});

            return new ExpandedAction(rotateAction, aimValuesAction);
        }

        private static ExpandedAction convertTurnStartAction(com.gats.simulation.action.Action action, Animator animator) {
            TurnStartAction startAction = (TurnStartAction) action;
            GameCharacter target = animator.teams[startAction.getTeam()][startAction.getCharacter()];
            animator.getCamera().moveToVector(target.getPos());

            CharacterSelectAction characterSelectAction = new CharacterSelectAction(startAction.getDelay(), target, animator::setActiveGameCharacter);

            //ui Action
            MessageUiTurnStartAction indicateTurnStartAction = new MessageUiTurnStartAction(0, animator.uiMessenger, animator.state.getCharacterFromTeams(startAction.getTeam(), startAction.getCharacter()));

            characterSelectAction.setChildren(new Action[]{indicateTurnStartAction});
            return new ExpandedAction(characterSelectAction, indicateTurnStartAction);
        }

        private static ExpandedAction convertCharacterSwitchWeaponAction(com.gats.simulation.action.Action action, Animator animator) {
            CharacterSwitchWeaponAction switchWeaponAction = (CharacterSwitchWeaponAction) action;
            GameCharacter target = animator.teams[switchWeaponAction.getTeam()][switchWeaponAction.getCharacter()];
            AddAction addAction = new AddAction(action.getDelay(), target, Weapons.summon(switchWeaponAction.getWpType()));

            MessageUiWeaponSelectAction selectedWeaponAction = new MessageUiWeaponSelectAction(0, animator.uiMessenger, switchWeaponAction.getWpType());
            addAction.setChildren(new Action[]{selectedWeaponAction});

            return new ExpandedAction(addAction, selectedWeaponAction);
        }


        private static ExpandedAction convertCharacterShootAction(com.gats.simulation.action.Action action, Animator animator) {
            CharacterShootAction shootAction = (CharacterShootAction) action;
            com.gats.simulation.GameCharacter currentPlayer = animator.state.getCharacterFromTeams(shootAction.getTeam(), shootAction.getCharacter());
            GameCharacter target = animator.teams[shootAction.getTeam()][shootAction.getCharacter()];

            ExecutorAction shotExecutorAction = new ExecutorAction(shootAction.getDelay(), () -> {
                target.getWeapon().shoot();
                Animation<TextureRegion> anim = target.getWeapon().getShootingAnimation();
                return anim != null ? anim.getAnimationDuration() : 0;
            });

            //uiaction
            MessageItemUpdateAction updateInventoryItem = new MessageItemUpdateAction(0, animator.uiMessenger, currentPlayer, currentPlayer.getSelectedWeapon());
            shotExecutorAction.setChildren(new Action[]{updateInventoryItem});
            return new ExpandedAction(shotExecutorAction, updateInventoryItem);
        }

        private static ExpandedAction convertCharacterHitAction(com.gats.simulation.action.Action action, Animator animator) {
            CharacterHitAction hitAction = (CharacterHitAction) action;
            Action lastAction;
            GameCharacter target = animator.teams[hitAction.getTeam()][hitAction.getCharacter()];
            SetAnimationAction hitAnimation = new SetAnimationAction(action.getDelay(), target, GameCharacterAnimationType.ANIMATION_TYPE_HIT);
            DestroyAction<ParticleEntity> destroyParticle = new DestroyAction<ParticleEntity>(2f, null, null, (entity) -> {
                target.remove(entity);
                entity.free();
            });

            SummonAction<ParticleEntity> summonParticle = new SummonAction<ParticleEntity>(0, destroyParticle::setTarget, () -> {
                ParticleEntity particleEntity = ParticleEntity.getParticleEntity(IngameAssets.damageParticle);
                target.add(particleEntity);
                particleEntity.setLoop(false);
                particleEntity.setRelPos(0, 5);
                return particleEntity;
            });
            summonParticle.setChildren(new Action[]{destroyParticle});
            if (hitAction.getHealthAft() <= 0) {
                SetAnimationAction deathAnimation = new SetAnimationAction(GameCharacter.getAnimationDuration(GameCharacterAnimationType.ANIMATION_TYPE_DEATH), target, GameCharacterAnimationType.ANIMATION_TYPE_DEATH);
                hitAnimation.setChildren(new Action[]{summonParticle, deathAnimation});
                DestroyAction<Entity> destroyCharacter = new DestroyAction<Entity>(GameCharacter.getAnimationDuration(GameCharacterAnimationType.ANIMATION_TYPE_DEATH), target, null, animator.characterGroup::remove);
                deathAnimation.setChildren(new Action[]{destroyCharacter});
                SummonAction<Entity> summonTombstone = new SummonAction<Entity>(0, null, () -> {
                    AnimatedEntity tombstone = new AnimatedEntity(IngameAssets.tombstoneAnimation);
                    tombstone.setRelPos(target.getRelPos());
                    tombstone.setOrigin(new Vector2(IngameAssets.tombstoneAnimation.getKeyFrame(0).getRegionWidth() / 2f, target.getOrigin().y));
                    animator.root.add(tombstone);
                    return tombstone;

                });
                destroyCharacter.setChildren(new Action[]{summonTombstone});
                IdleAction waitAnimation = new IdleAction(0, IngameAssets.tombstoneAnimation.getAnimationDuration());
                summonTombstone.setChildren(new Action[]{waitAnimation});

            } else {
                SetAnimationAction resetAnimationAction = new SetAnimationAction(GameCharacter.getAnimationDuration(GameCharacterAnimationType.ANIMATION_TYPE_HIT), target, GameCharacterAnimationType.ANIMATION_TYPE_IDLE);
                hitAnimation.setChildren(new Action[]{summonParticle, resetAnimationAction});
            }
           UpdateHealthBarAction updateHealthBarAction  = new UpdateHealthBarAction(0,hitAction.getHealthAft(),target.getHealthbar());
            hitAnimation.addChild(updateHealthBarAction);
            lastAction=updateHealthBarAction;

            return new ExpandedAction(hitAnimation, lastAction);
        }

        private static ExpandedAction convertGameOverAction(com.gats.simulation.action.Action action, Animator animator) {
            GameOverAction winAction = (GameOverAction) action;

                MessageUiGameEndedAction gameEndedAction;
                if (winAction.getTeam() < 0) {
                    //Todo replace with draw display
                    //gameEndedAction = new MessageUiGameEndedAction(0,animator.uiMessenger,true, winAction.getTeam());
                    gameEndedAction = new MessageUiGameEndedAction(0,animator.uiMessenger,true);
                } else {

                    //Todo display with winner
                    gameEndedAction = new MessageUiGameEndedAction(0,animator.uiMessenger,true, winAction.getTeam());
                }


            return new ExpandedAction(gameEndedAction);
        }

        private static ExpandedAction convertDebugPointAction(com.gats.simulation.action.Action action, Animator animator) {
            DebugPointAction debugPointAction = (DebugPointAction) action;

            DestroyAction<Entity> destroyAction = new DestroyAction<Entity>(debugPointAction.getDuration(), null, null, animator.root::remove);

            SummonAction<Entity> summonAction = new SummonAction<Entity>(action.getDelay(), destroyAction::setTarget, () -> {
                SpriteEntity entity;
                if (debugPointAction.isCross()) {
                    entity = new SpriteEntity(IngameAssets.cross_marker);
                    entity.setSize(new Vector2(3, 3));
                    debugPointAction.getPos().sub(1, 1);
                } else {
                    entity = new SpriteEntity(IngameAssets.pixel);
                }
                entity.setRelPos(debugPointAction.getPos());
                entity.setColor(debugPointAction.getColor());
                animator.root.add(entity);
                return entity;
            });

            summonAction.setChildren(new Action[]{destroyAction});


            return new ExpandedAction(summonAction, destroyAction);
        }

        private static ExpandedAction convertCharacterMoveAction(com.gats.simulation.action.Action action, Animator animator) {
            CharacterMoveAction moveAction = (CharacterMoveAction) action;

            GameCharacter target = animator.teams[moveAction.getTeam()][moveAction.getCharacter()];
            Path path = moveAction.getPath();
            SetAnimationAction startWalking = new SetAnimationAction(action.getDelay(), target, GameCharacterAnimationType.ANIMATION_TYPE_HIT);
            CharacterPath characterPath = new CharacterPath(moveAction.getPath());
            MoveAction animMoveAction = new MoveAction(0, target, characterPath.getDuration(), characterPath);
            //rotateAction to set the angle/direction of movement, to flip the character sprite
            RotateAction animRotateAction = new RotateAction(0, target, characterPath.getDuration(), characterPath);
            startWalking.setChildren(new Action[]{animMoveAction, animRotateAction});
            SetAnimationAction stopWalking = new SetAnimationAction(0, target, GameCharacterAnimationType.ANIMATION_TYPE_IDLE);
            animMoveAction.setChildren(new Action[]{stopWalking});

            return new ExpandedAction(startWalking, stopWalking);
        }

    }


    /**
     * Setzt eine Welt basierend auf den Daten in state auf und bereitet diese für nachfolgende Animationen vor
     *
     * @param viewport viewport used for rendering
     */
    public Animator(Viewport viewport, GameMode gameMode, UiMessenger uiMessenger) {
        this.gameMode = gameMode;
        this.uiMessenger = uiMessenger;
        this.batch = new SpriteBatch();
        this.root = new EntityGroup();

        setupView(viewport);

        setup();
        // assign textures to tiles after processing game Stage
        //put sprite information into gameStage?
    }

    @Override
    public void init(GameState state) {
        synchronized (root) {
            this.state = state;
            map = new TileMap(IngameAssets.tileTextures, state);
            root.add(map);

            teamCount = state.getTeamCount();
            charactersPerTeam = state.getCharactersPerTeam();

            teams = new GameCharacter[teamCount][charactersPerTeam];

            TextureRegion animationFrame = IngameAssets.gameCharacterAnimations[0].getKeyFrame(0);
            //calculate the center of the gameCharacter sprite, so the aim Indicator will be drawn relative to it
            Vector2 centerOfCharacterSprite = new Vector2(animationFrame.getRegionWidth() / 2f, animationFrame.getRegionHeight() / 2f);
            characterGroup = new EntityGroup();

            root.add(characterGroup);
            for (int curTeam = 0; curTeam < teamCount; curTeam++)
                for (int curCharacter = 0; curCharacter < charactersPerTeam; curCharacter++) {
                    com.gats.simulation.GameCharacter simGameCharacter = state.getCharacterFromTeams(curTeam, curCharacter);
                    GameCharacter animGameCharacter;
                    if (gameMode == GameMode.Christmas)
                        animGameCharacter = new GameCharacter(teamColors[Math.min(1, curTeam)]);
                    else
                        animGameCharacter = new GameCharacter(teamColors[curTeam]);

                    AimIndicator aimIndicator = new AimIndicator(IngameAssets.aimingIndicatorSprite, animGameCharacter);
                    aimIndicator.setScale(new Vector2(0.5f, 1));
                    //init healhtbar with correct health and position.
                    new Healthbar(simGameCharacter, animGameCharacter);
                    teams[curTeam][curCharacter] = animGameCharacter;
                    animGameCharacter.setRelPos(simGameCharacter.getPlayerPos().cpy().add(com.gats.simulation.GameCharacter.getSize().scl(0.5f)));
                    characterGroup.add(animGameCharacter);
                }
        }
    }

    private void setup() {


        background = new SpriteEntity(
                IngameAssets.background,
                new Vector2(-backgroundViewport.getWorldWidth() / 2, -backgroundViewport.getWorldHeight() / 2),
                new Vector2(259, 128));


    }

    /**
     * Takes care of setting up the view for the user. Creates a new camera and sets the position to the center.
     * Adds it to the given Viewport.
     *
     * @param newViewport Viewport instance that animator will use to display the game.
     */
    private void setupView(Viewport newViewport) {

        int height = Gdx.graphics.getHeight();
        int width = Gdx.graphics.getWidth();
        this.viewport = newViewport;
        this.backgroundViewport = new FillViewport(259, 128);
        //center camera once
        //camera.position.set(Gdx.graphics.getWidth()/2f, Gdx.graphics.getHeight()/2f-12,0);
        //camera.zoom = 1;
        //viewport.setCamera(camera);
        //viewport.update(400,400);


        this.camera = new AnimatorCamera(30, 30f * width / height);
        this.viewport.setCamera(camera);
        camera.zoom = 1f;
        camera.position.set(new float[]{0, 0, 0});
        this.backgroundViewport.update(width, height);
        this.viewport.update(width, height, true);
        camera.update();

    }

    /**
     * Animates the logs actions
     *
     * @param log Queue aller {@link com.gats.simulation.action.Action animations-relevanten Ereignisse}
     */
    public void animate(ActionLog log) {
        pendingLogs.add(log);
    }

    private Action convertAction(com.gats.simulation.action.Action action) {
        return ActionConverters.convert(action, this);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        if (actionList.isEmpty()) {
            if (!pendingLogs.isEmpty()) {
                actionList.add(convertAction(pendingLogs.poll().getRootAction()));
            } else {
                synchronized (notificationObject) {
                    notificationObject.notifyAll();
                }
            }
        }


        ListIterator<Action> iter = actionList.listIterator();
        Stack<Remainder> remainders = new Stack<>();
        while (iter.hasNext()) {
            Action cur = iter.next();
            float remainder = cur.step(delta);
            if (remainder >= 0) {
                iter.remove();
                //Schedule children to run for the time not consumed by their parent
                Action[] children = cur.getChildren();
                if (children != null && children.length > 0) remainders.push(new Remainder(remainder, children));
            }
        }

        //Process the completed actions children with their respective remaining times
        while (!remainders.empty()) {
            Remainder curRemainder = remainders.pop();
            float remainingDelta = curRemainder.getRemainingDelta();
            Action[] actions = curRemainder.getActions();
            for (Action cur : actions) {
                float remainder = cur.step(remainingDelta);
                if (remainder >= 0) {
                    //Schedule children to run for the time that's not consumed by their parent
                    Action[] children = cur.getChildren();
                    if (children != null && children.length > 0) remainders.push(new Remainder(remainder, children));
                } else {
                    //Add the child to the list of running actions if not completed in the remaining time
                    actionList.add(cur);
                }
            }
        }


        camera.updateMovement(delta);
        camera.update();


        backgroundViewport.apply();
        //begin drawing elements of the SpriteBatch

        batch.setProjectionMatrix(backgroundViewport.getCamera().combined);
        batch.begin();
        background.draw(batch, delta, 1);
        batch.setProjectionMatrix(camera.combined);
        //tells the batch to render in the way specified by the camera
        // e.g. Coordinate-system and Viewport scaling
        viewport.apply();

        //ToDo: make one step in the scheduled actions


        //recursively draw all entities by calling the root group
        synchronized (root) {
            root.draw(batch, delta, 1);
        }
        batch.end();
    }

    @Override
    public void resize(int width, int height) {

        viewport.update(width, height, true);
        backgroundViewport.update(width, height);
        viewport.getCamera().update();
        camera.update();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        //disposes the atlas for every class because it is passed down as a parameter, no bruno for changing back to menu
        //textureAtlas.dispose();

    }


    @Override
    public void awaitNotification() {
        synchronized (notificationObject) {
            try {
                notificationObject.wait();
            } catch (InterruptedException ignored) {
            }
        }
    }

    public GameCharacter setActiveGameCharacter(GameCharacter newCharacter) {
        GameCharacter old = activeCharacter;
        activeCharacter = newCharacter;
        if (old != null) old.setHoldingWeapon(false);
        if (activeCharacter != null) activeCharacter.setHoldingWeapon(true);
        return old;
    }
}
