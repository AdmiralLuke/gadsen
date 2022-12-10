package com.gats.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gats.animation.action.*;
import com.gats.animation.action.Action;
import com.gats.animation.entity.*;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.gats.manager.AnimationLogProcessor;
import com.gats.simulation.*;


import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Function;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Kernklasse für die Visualisierung des Spielgeschehens.
 * Übersetzt {@link com.gats.simulation.GameState GameState} und {@link com.gats.simulation.ActionLog ActionLog}
 * des {@link com.gats.simulation Simulation-Package} in für libGDX renderbare Objekte
 */
public class Animator implements Screen, AnimationLogProcessor {

    //ToDo organize Assets in seperate class
    private final Animation<TextureRegion> destroyTileAnimation; //ToDo replace placeholder
    private OrthographicCamera camera;
    private final TextureAtlas textureAtlas;
    private GameState state;

    private Viewport viewport;
    private Viewport backgroundViewport;

    private SpriteEntity background;
    private Texture backgroundTexture;

    private TextureRegion[] tileTextures;
    private Batch batch;

    private EntityGroup root;

    private TileMap map;

    private BlockingQueue<ActionLog> pendingLogs = new LinkedBlockingQueue<>();



    private GameCharacter[][] teams;

    private int teamCount;
    private int charactersPerTeam;
    private List<Action> actionList = new LinkedList<>();

    private Map<Class<?>, ActionConverter> actionConverters = ActionConverters.map;

    interface ActionConverter {
        public ExpandedAction apply(com.gats.simulation.Action simAction, Animator animator);
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
                        put(CharacterMoveAction.class, ActionConverters::convertCharacterMoveAction);
                        put(ProjectileAction.class, ActionConverters::convertProjectileMoveAction);
                        put(TileMoveAction.class, ActionConverters::convertTileMoveAction);
                        put(TileDestroyAction.class, ActionConverters::convertTileDestroyAction);
                    }
                };


        public static Action convert(com.gats.simulation.Action simAction, Animator animator) {
            ExpandedAction expandedAction = map.getOrDefault(simAction.getClass(), (v, w) -> new ExpandedAction(new IdleAction(0, 0)))
                    .apply(simAction, animator);
            expandedAction.tail.setChildren(extractChildren(simAction, animator));

            return expandedAction.head;
        }

        private static Action[] extractChildren(com.gats.simulation.Action action, Animator animator) {
            int childCount = action.getChildren().size();
            if (childCount == 0) return new Action[]{};

            Action[] children = new Action[childCount];
            int i = 0;
            Iterator<com.gats.simulation.Action> iterator = action.iterator();
            while (iterator.hasNext()) {
                com.gats.simulation.Action curChild = iterator.next();
                children[i] = convert(curChild, animator);
                i++;
            }

            return children;
        }

        private static ExpandedAction convertCharacterMoveAction(com.gats.simulation.Action action, Animator animator) {
            CharacterMoveAction moveAction = (CharacterMoveAction) action;
            Entity target = animator.teams[moveAction.getTeam()][moveAction.getCharacter()];

            return new ExpandedAction(new MoveAction(action.getDelay(), target, moveAction.getDuration(), moveAction.getPath()));
        }

        private static ExpandedAction convertProjectileMoveAction(com.gats.simulation.Action action, Animator animator) {
            ProjectileAction projectileAction = (ProjectileAction) action;

            MoveAction moveProjectile = new MoveAction(0, null, projectileAction.getDuration(), projectileAction.getPath());

            DestroyAction destroyProjectile = new DestroyAction(0, null, null, animator.root::remove);

            SummonAction summonProjectile = new SummonAction(action.getDelay(), target -> {
                moveProjectile.setTarget(target);
                destroyProjectile.setTarget(target);
            }, () -> {
                Entity projectile = Projectiles.summon();
                animator.root.add(projectile);
                return projectile;
            });

            //The Projectile should be moved after being summoned
            summonProjectile.setChildren(new Action[]{moveProjectile});

            //The Projectile should get destroyed at the end of its path
            moveProjectile.setChildren(new Action[]{destroyProjectile});

            //We sliced the projectile Action: Summon is now the first and Destroy the last Action with Move in between
            return new ExpandedAction(summonProjectile, destroyProjectile);
        }

        private static ExpandedAction convertTileMoveAction(com.gats.simulation.Action action, Animator animator) {
            TileMoveAction tileMoveAction = (TileMoveAction) action;

            final IntVector2 IntPos = tileMoveAction.getPos();
            final IntVector2 IntPosAfter = tileMoveAction.getPosAfter();
            AtomicInteger tileType = new AtomicInteger(-1);

            MoveAction moveTileEntity = new MoveAction(0, null, tileMoveAction.getDuration(), tileMoveAction.getPath());

            DestroyAction destroyTileEntity = new DestroyAction(0, null, null, child -> {
                animator.root.remove(child);
                animator.map.setTile(IntPosAfter, tileType.intValue());
            });

            SummonAction summonTileEntity = new SummonAction(action.getDelay(), target -> {
                moveTileEntity.setTarget(target);
                destroyTileEntity.setTarget(target);
            }, () -> {
                tileType.set(animator.map.getTile(IntPos));
                animator.map.setTile(IntPos, TileMap.TYLE_TYPE_NONE);
                if (tileType.intValue() != TileMap.TYLE_TYPE_NONE) {
                    Entity projectile = new SpriteEntity(animator.tileTextures[tileType.intValue()]);
                    animator.root.add(projectile);
                    return projectile;
                }
                return new Entity();
            });

            return new ExpandedAction(summonTileEntity, destroyTileEntity);
        }
        private static ExpandedAction convertTileDestroyAction(com.gats.simulation.Action action, Animator animator) {

            TileDestroyAction destroyAction = (TileDestroyAction) action;

            DestroyAction destroyProjectile = new DestroyAction(animator.destroyTileAnimation.getAnimationDuration()*1000, null, null, animator.root::remove);

            SummonAction summonProjectile = new SummonAction(action.getDelay(), destroyProjectile::setTarget, () -> {
                animator.map.setTile(destroyAction.getPos(), TileMap.TYLE_TYPE_NONE);
                Entity particle = new AnimatedEntity(animator.destroyTileAnimation);
                particle.setRelPos(destroyAction.getPos().toFloat().scl(animator.map.getTileSize()));
                animator.root.add(particle);
                return particle;
            });

            return new ExpandedAction(summonProjectile, destroyProjectile);
        }
    }

    /**
     * Setzt eine Welt basierend auf den Daten in state auf und bereitet diese für nachfolgende Animationen vor
     *
     * @param state    Contains the initial state of the game before any actions are played
     * @param viewport viewport used for rendering
     * @param atlas    the TextureAtlas containing all required assets
     */
    public Animator(GameState state, Viewport viewport, TextureAtlas atlas) {
        this.state = state;
        this.textureAtlas = atlas;
        this.batch = new SpriteBatch();
        this.root = new EntityGroup();

        this.destroyTileAnimation = new Animation<TextureRegion>(0.5f,
                textureAtlas.findRegions("tile/coolCat"));

        setupView(viewport);

        setup(state);
        // assign textures to tiles after processing game Stage
        //put sprite information into gameStage?
    }

    private void setup(GameState state) {


        background = new SpriteEntity(
                textureAtlas.findRegion("background/WeihnachtsBG"),
                new Vector2(-backgroundViewport.getWorldWidth()/2, -backgroundViewport.getWorldHeight()/2),
                new Vector2(259, 128));
        ////root.add(background);

        backgroundTexture = textureAtlas.findRegion("background/WeihnachtsBG").getTexture();
        //backgroundTexture.setWrap();

        TextureRegion[] tileTextures = new TextureRegion[]{
                textureAtlas.findRegion("tile/16x_box01"),
                textureAtlas.findRegion("tile/16x_anchor01")
        };
        map = new TileMap(tileTextures, state);
        root.add(map);


        teamCount = state.getTeamCount();
        charactersPerTeam = state.getCharactersPerTeam();

        teams = new GameCharacter[teamCount][charactersPerTeam];

        Animation<TextureRegion> idleAnimation = new Animation<TextureRegion>(0.5f,
                textureAtlas.findRegions("tile/coolCat"));


        for (int curTeam = 0; curTeam < teamCount; curTeam++)
            for (int curCharacter = 0; curCharacter < charactersPerTeam; curCharacter++) {
                com.gats.simulation.GameCharacter simGameCharacter = state.getCharacterFromTeams(curTeam, curCharacter);
                GameCharacter animGameCharacter = new GameCharacter(idleAnimation);
                animGameCharacter.setRelPos(simGameCharacter.getPlayerPos().cpy());
                teams[curTeam][curCharacter] = animGameCharacter;
                root.add(animGameCharacter);
            }

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
        camera = new OrthographicCamera();
        //center camera once
        //camera.position.set(Gdx.graphics.getWidth()/2f, Gdx.graphics.getHeight()/2f-12,0);
        //camera.zoom = 1;
        //viewport.setCamera(camera);
        //viewport.update(400,400);


        this.camera = new OrthographicCamera(30, 30 * width / height);
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
     * @param log Queue aller {@link com.gats.simulation.Action animations-relevanten Ereignisse}
     */
    public void animate(ActionLog log) {
        pendingLogs.add(log);
    }

    private Action convertAction(com.gats.simulation.Action action) {
        return ActionConverters.convert(action, this);
    }

    protected void setActiveCharacter(int teamIndex, int characterIndex) {

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        if (actionList.isEmpty()){
            if(!pendingLogs.isEmpty()){
                actionList.add(convertAction(pendingLogs.poll().getRootAction()));
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
                if (children.length > 0) remainders.push(new Remainder(remainder, children));
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
                    if (children.length > 0) remainders.push(new Remainder(remainder, children));
                } else {
                    //Add the child to the list of running actions if not completed in the remaining time
                    actionList.add(cur);
                }
            }
        }


        cameraMove(delta);
        camera.update();




        backgroundViewport.apply();
        //begin drawing elements of the SpriteBatch

        batch.setProjectionMatrix(backgroundViewport.getCamera().combined);
        batch.begin();
        background.draw(batch, delta, 1);
        //ToDo: Make the BG fill out the screen
        batch.setProjectionMatrix(camera.combined);
        //tells the batch to render in the way specified by the camera
        // e.g. Coordinate-system and Viewport scaling

        viewport.apply();


        //ToDo: make one step in the scheduled actions


        //recursively draw all entities by calling the root group
        root.draw(batch, delta, 1);
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
        textureAtlas.dispose();
    }

    //animator movement
    Vector3 cameraDirection = new Vector3(0, 0, 0);
    int cameraSpeed = 200;

    public void setCameraDir(float[] directions) {
        this.cameraDirection = new Vector3(directions);
//left*-cameraSpeed+right*cameraSpeed,up*cameraSpeed+down*-cameraSpeed,0
        //for later camera.position.lerp() for smooth movement
    }

    /**
     * Moves the camera with the Speed defined in {@link Animator#cameraSpeed} and the {@link Animator#cameraDirection}.
     *
     * @param delta Delta-time of the Application.
     */
    private void cameraMove(float delta) {

        camera.translate(new Vector3(cameraDirection).scl(cameraSpeed * delta));

    }


}
