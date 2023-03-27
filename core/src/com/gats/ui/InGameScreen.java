package com.gats.ui;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.*;
import com.gats.animation.Animator;
import com.gats.animation.AnimatorCamera;
import com.gats.manager.AnimationLogProcessor;
import com.gats.manager.HumanPlayer;
import com.gats.manager.Manager;
import com.gats.manager.RunConfiguration;
import com.gats.simulation.action.ActionLog;
import com.gats.simulation.action.Action;

import java.util.List;
import java.util.Vector;

/**
 * Der Screen welcher ein aktives Spiel anzeigt.
 */
public class InGameScreen implements Screen, AnimationLogProcessor {

    private final Manager manager;
    private final List<HumanPlayer> humanList;
    private Viewport gameViewport;
    private Viewport hudViewport;

    private float worldWidth = 80*12;
    private float worldHeight = 80*12;

    //should HUD be handled by GADS
    private HudStage hudStage;
    private Animator animator;
    private final GADS gameManager;
    public InGameScreen(GADS instance, RunConfiguration runConfig){

        gameManager = instance;
        gameViewport = new FillViewport(worldWidth,worldHeight);
        hudViewport = new FitViewport(worldWidth,worldHeight);


        hudStage = new HudStage(hudViewport,this);
        setupInput();


        runConfig.gui = true;
        runConfig.animationLogProcessor = this;
        runConfig.input = hudStage.getInputHandler();
        manager = new Manager(runConfig);
        animator = new Animator(manager.getState(), gameViewport, runConfig.gameMode );
        manager.start();

        humanList = manager.getHumanList();

    }

    //gets called when the screen becomes the main screen of GADS
    @Override
    public void show() {
        animator.show();
    }

    @Override
    public void render(float delta) {
        hudStage.tick(delta);
        hudStage.act();
        animator.render(delta);
        hudStage.draw();
        //animator.animate(gameManager.simulation.getActionLog());
    }

    /**
     * Forwards the ActionLog to the Animator for processing
     *
     * @param log Queue of all {@link Action animation-related Actions}
     */
    public void animate(ActionLog log) {animator.animate(log);}


    @Override
    public void awaitNotification() {
        animator.awaitNotification();
    }

    @Override
    public void resize(int width, int height) {
        animator.resize(width, height);
        hudStage.getViewport().update(width, height);
        hudStage.getViewport().apply();
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
        hudStage.dispose();
        manager.dispose();
        gameManager.setScreenMenu();
    }
    public void setupInput(){

        //animator als actor?
         //       simulation als actor?
        Gdx.input.setInputProcessor(hudStage.getInputHandler());
        hudStage.getInputHandler().setHumanPlayers(humanList);

    }

    /**
     * Converts Viewport/Screen-Coordinates to World/Ingame-Position
     * @param coordinates to convert.
     * @return Vector with World-Coordinate
     */
    public Vector2 toWorldCoordinates(Vector2 coordinates){
        Vector3 position = gameViewport.getCamera().unproject(new Vector3(coordinates.x,coordinates.y,0));
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
}
