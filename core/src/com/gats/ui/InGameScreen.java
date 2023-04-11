package com.gats.ui;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.*;
import com.gats.animation.Animator;
import com.gats.animation.AnimatorCamera;
import com.gats.manager.*;
import com.gats.simulation.GameState;
import com.gats.simulation.action.ActionLog;
import com.gats.simulation.action.Action;
import com.gats.ui.assets.AssetContainer;
import com.gats.ui.menu.debugView.DebugView;

import java.util.ArrayList;

/**
 * Der Screen welcher ein aktives Spiel anzeigt.
 */
public class InGameScreen implements Screen, AnimationLogProcessor {

    private final Manager manager;
    private Viewport gameViewport;
    private float worldWidth = 80*12;
    private float worldHeight = 80*12;

    private float renderingSpeed = 1;

    //should HUD be handled by GADS
    private Hud hud;
    private Animator animator;
    private final GADS gameManager;

    private DebugView debugView;
    public InGameScreen(GADS instance, RunConfiguration runConfig){

        gameManager = instance;
        gameViewport = new FillViewport(worldWidth,worldHeight);

        hud = new Hud(this, runConfig);

        debugView = new DebugView(AssetContainer.MainMenuAssets.skin);

        setupInput();

        //update runconfig
        runConfig.gui = true;
        runConfig.animationLogProcessor = this;
        runConfig.uiMessenger = hud.getUiMessenger();
        runConfig.inputProcessor = hud.getInputHandler();

        manager = Manager.getManager();
        //ToDo this should be happening in Menu
        ArrayList<Game> games = manager.schedule(runConfig);
        if (games.size() > 1) System.err.println("Warning: RunConfig produced more than one game: Only showing the first game!");
        Game game = games.get(0);
        animator = new Animator(gameViewport, runConfig.gameMode, runConfig.uiMessenger);
        game.start();

    }

    //gets called when the screen becomes the main screen of GADS
    @Override
    public void show() {
        animator.show();
    }
    public void setRenderingSpeed(float speed){
        //negative deltaTime is not allowed
        if(speed>=0) this.renderingSpeed = speed;
    }

    @Override
    public void render(float delta) {
        hud.tick(delta);
        animator.render(renderingSpeed*delta);
        hud.draw();
        debugView.draw();
    }

    @Override
    public void init(GameState state) {
        //ToDo the game is starting remove waiting screen etc.
        animator.init(state);
    }

    /**
     * Forwards the ActionLog to the Animator for processing
     *
     * @param log Queue of all {@link Action animation-related Actions}
     */
    public void animate(ActionLog log) {animator.animate(log);
    debugView.add(log);}


    @Override
    public void awaitNotification() {
        animator.awaitNotification();
    }

    @Override
    public void resize(int width, int height) {
        animator.resize(width, height);
        hud.resizeViewport(width,height);
        gameViewport.update(width,height);
        debugView.getViewport().update(width,height);
    }

    @Override
    public void pause() {
        animator.pause();
    }

    @Override
    public void resume() {
        animator.resume();
    }

    @Override
    public void hide() {
        animator.hide();
    }

    /**
     * Gets called when the application is destroyed or currently when escape is pressed to return to menu. Not the best but the fastest way rn.
     */
    @Override
    public void dispose() {
        animator.dispose();
        manager.dispose();
        hud.dispose();
        gameManager.setScreenMenu();
    }
    public void setupInput(){

        //animator als actor?
         //       simulation als actor?
        Gdx.input.setInputProcessor(hud.getInputProcessor());

    }

    /**
     * Converts Viewport/Screen-Coordinates to World/Ingame-Position
     * @param coordinates to convert.
     * @return Vector with World-Coordinate
     */
    public Vector2 toWorldCoordinates(Vector2 coordinates){
        Vector3 position = gameViewport.unproject(new Vector3(coordinates.x,coordinates.y,0));
        return new Vector2(position.x,position.y);
    }

    //this section handles the input
    public void processInputs(float[] ingameCameraDirection,float zoomPressed) {
      AnimatorCamera camera = animator.getCamera();
       camera.setDirections(ingameCameraDirection);
       camera.setZoomPressed(zoomPressed);
    }
    public void resetCamera(){
        animator.getCamera().resetCamera();
    }

    public void toggleCameraMove() {
        animator.getCamera().toggleCanMoveToVector();
    }

    public void toggleDebugView() {
        debugView.toggleDebugView();
        hud.toggleDebugOutlines();
    }
    public void moveCameraByOffset(Vector2 offset){
        animator.getCamera().moveByOffset(offset);
    }

    /**
     * Calls AnimatorCamera function to Zoom.
     * @param zoom Value that shall be added to the zoom
     */
    public void zoomCamera(float zoom){
        AnimatorCamera camera = animator.getCamera();
        camera.addZoomPercent(zoom);
    }
}
