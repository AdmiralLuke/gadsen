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
import com.gats.animation.action.MoveAction;
import com.gats.animation.entity.EntityGroup;
import com.gats.animation.entity.SpriteEntity;
import com.gats.animation.entity.TileMap;
import com.gats.animation.action.Action;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.gats.simulation.ActionLog;
import com.gats.simulation.CharacterMoveAction;
import com.gats.simulation.GameState;
import com.gats.simulation.Path;


import java.util.*;
import java.util.function.Function;

/**
 * Kernklasse für die Visualisierung des Spielgeschehens.
 * Übersetzt {@link com.gats.simulation.GameState GameState} und {@link com.gats.simulation.ActionLog ActionLog}
 * des {@link com.gats.simulation Simulation-Package} in für libGDX renderbare Objekte
 */
public class Animator implements Screen{

    private OrthographicCamera camera;
    private final TextureAtlas textureAtlas;
    private GameState state;

    private Viewport viewport;
    private Viewport backgroundViewport;

    private SpriteEntity background;
    private Texture backgroundTexture;
    private Batch batch;

    private EntityGroup root;

    private TileMap map;


    private GameCharacter[][] teams;

    private int teamCount;
    private int charactersPerTeam;
    private List<Action> actionList = new LinkedList<>();

    private Map<Class<?>, ActionConverter> actionConverters = ActionConverters.map;

    interface ActionConverter{
        public Action apply(com.gats.simulation.Action simAction, Animator animator);
    }

    public static class ActionConverters {

        public static final Map<Class<?>, ActionConverter> map =
                new HashMap<Class<?>, ActionConverter>(){
                    {
                        put(CharacterMoveAction.class, ActionConverters::convertCharacterMoveAction);
                    }
                };
        public static Action convertCharacterMoveAction(com.gats.simulation.Action action, Animator animator){
            CharacterMoveAction moveAction = (CharacterMoveAction) action;
            return new MoveAction(action.getDelay(), moveAction.getDelay(), 0, animator.teams[0][0], moveAction.getPath());
        }
    }

    /**
     * Setzt eine Welt basierend auf den Daten in state auf und bereitet diese für nachfolgende Animationen vor
     * @param state Contains the initial state of the game before any actions are played
     * @param viewport viewport used for rendering
     * @param atlas the TextureAtlas containing all required assets
     */
    public Animator(GameState state, Viewport viewport, TextureAtlas atlas){
        this.state = state;
        this.textureAtlas = atlas;
        this.batch = new SpriteBatch();
        this.root = new EntityGroup();


        setupView(viewport);

        setup(state);
        // assign textures to tiles after processing game Stage
        //put sprite information into gameStage?
    }

    private void setup(GameState state){



       background = new SpriteEntity(
               textureAtlas.findRegion("tile/GADSBG"),
               new Vector2(0, 0),
               new Vector2(259*4,128*4));
      ////root.add(background);

        backgroundTexture = textureAtlas.findRegion("tile/GADSBG").getTexture();
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
            for (int curCharacter = 0; curCharacter<charactersPerTeam; curCharacter++){
                com.gats.simulation.GameCharacter simGameCharacter = state.getCharacterFromTeams(curTeam, curCharacter);
                GameCharacter animGameCharacter = new GameCharacter(idleAnimation);
                animGameCharacter.setRelPos(simGameCharacter.getPlayerPos().add(new Vector2(45*12,45*12)));
                teams[curTeam][curCharacter] = animGameCharacter;
                root.add(animGameCharacter);
            }

    }

    /**
     * Takes care of setting up the view for the user. Creates a new camera and sets the position to the center.
     * Adds it to the given Viewport.
     * @param newViewport Viewport instance that animator will use to display the game.
     */
    private void setupView(Viewport newViewport){

        int height = Gdx.graphics.getHeight();
        int width = Gdx.graphics.getWidth();
       this.viewport = newViewport;
       this.backgroundViewport = new FillViewport(259, 128);
        camera = new OrthographicCamera();
        //center camera once
      //  camera.position.set(Gdx.graphics.getWidth()/2f, Gdx.graphics.getHeight()/2f-12,0);
        //camera.zoom = 1;
        //viewport.setCamera(camera);
        //viewport.update(400,400);


        this.camera = new OrthographicCamera(30,30*width/height);
        this.viewport.setCamera(camera);
        camera.zoom = 1f;
        camera.position.set(new float[]{0, 0, 0});
        this.backgroundViewport.update(width,height);
        this.viewport.update(width,height, true);
    }
    /**
     * Animates the logs actions
     * @param log Queue aller {@link com.gats.simulation.Action animations-relevanten Ereignisse}
     */
    public void animate(ActionLog log){
        if(!(log == null)) {
            com.gats.simulation.Action currentAction = null; // = log.getNextAction();
            while (currentAction != null) {
                // currentAction = log.getNextAction();

                Action convertedAction = actionConverters.getOrDefault( currentAction.getClass(), (v, w) -> null)
                        .apply(currentAction, this);
                if (convertedAction != null) actionList.add(convertedAction);
            }
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        ListIterator<Action> iter = actionList.listIterator();
        while (iter.hasNext()){
            if (iter.next().step(delta)) iter.remove();
        }

        cameraMove(delta);
        camera.update();




        backgroundViewport.apply();
        //begin drawing elements of the SpriteBatch

        batch.setProjectionMatrix(backgroundViewport.getCamera().combined);
        batch.begin();
       background.draw(batch,delta,1);
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

            viewport.update(width, height,true);
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
    Vector3 cameraDirection = new Vector3(0,0,0);
    int cameraSpeed = 200;
    public void setCameraDir(float[] directions){
        this.cameraDirection = new Vector3(directions);
//left*-cameraSpeed+right*cameraSpeed,up*cameraSpeed+down*-cameraSpeed,0
        //for later camera.position.lerp() for smooth movement
    }

    /**
     * Moves the camera with the Speed defined in {@link Animator#cameraSpeed} and the {@link Animator#cameraDirection}.
     * @param delta Delta-time of the Application.
     */
    private void cameraMove(float delta){

      camera.translate(new Vector3(cameraDirection).scl(cameraSpeed*delta));

    }


}
