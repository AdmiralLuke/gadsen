package com.gats.ui;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.*;
import com.gats.animation.Animator;
import com.gats.animation.AnimatorCamera;
import com.gats.manager.AnimationLogProcessor;
import com.gats.manager.HumanPlayer;
import com.gats.manager.Manager;
import com.gats.manager.RunConfiguration;
import com.gats.simulation.ActionLog;

import java.util.List;

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
    private final GADSAssetManager assetManager;
    // adjust pipeline, so it provides a different directory for ingame assets
    // and menu assets? or we code importing into AssetManager?
    private TextureAtlas ingameAtlas;
    public InGameScreen(GADS instance, GADSAssetManager aM, GameSettings gameSettings){

        gameManager = instance;
        assetManager = aM;
        assetManager.loadTextures();
        ingameAtlas = assetManager.getAtlas();
        gameViewport = new FillViewport(worldWidth,worldHeight);
        hudViewport = new FitViewport(worldWidth,worldHeight);


        hudStage = new HudStage(hudViewport,this, assetManager);
        setupInput();

        RunConfiguration runConfiguration = gameSettings.toRunConfiguration();
        runConfiguration.gui = true;
        runConfiguration.animationLogProcessor = this;
        runConfiguration.hud = hudStage;
        manager = new Manager(runConfiguration);
        animator = new Animator(manager.getState(), gameViewport, ingameAtlas, gameSettings.getGameMode());
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
        hudStage.act();
        animator.render(delta);
        hudStage.draw();
        //animator.animate(gameManager.simulation.getActionLog());
    }

    /**
     * Forwards the ActionLog to the Animator for processing
     *
     * @param log Queue of all {@link com.gats.simulation.Action animation-related Actions}
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
        Gdx.input.setInputProcessor(hudStage);
        hudStage.setHumanPlayers(humanList);

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
}
