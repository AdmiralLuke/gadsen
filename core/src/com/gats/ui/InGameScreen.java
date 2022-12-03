package com.gats.ui;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.*;
import com.gats.animation.Animator;
import com.gats.simulation.GameState;

import static com.badlogic.gdx.Input.Keys;

/**
 * Der Screen welcher ein aktives Spiel anzeigt.
 */
public class InGameScreen implements Screen {

    private Viewport gameViewport;
    private Viewport hudViewport;

    private float worldWidth = 80*12;
    private float worldHeight = 80*12;

    //should HUD be handled by GADS
    private Stage hudStage;
    private Animator animator;
    private final GADS gameManager;
    private final GADSAssetManager assetManager;
    // adjust pipeline, so it provides a different directory for ingame assets
    // and menu assets? or we code importing into AssetManager?
    private TextureAtlas ingameAtlas;
    public InGameScreen(GADS instance, GADSAssetManager aM){

        gameManager = instance;
        assetManager = aM;
        assetManager.loadTextures();
        ingameAtlas = assetManager.getAtlas();
        gameViewport = new FillViewport(worldWidth,worldHeight);
        hudViewport = new FitViewport(worldWidth,worldHeight);
        animator = new Animator(gameManager.simulation.getState(), gameViewport, ingameAtlas);

        hudStage = new HudStage(hudViewport,this, assetManager);
        setupInput();

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

    @Override
    public void dispose() {
        animator.dispose();
        hudStage.dispose();

    }
    public void setupInput(){

        //animator als actor?
         //       simulation als actor?
        Gdx.input.setInputProcessor(hudStage);


    }

    //this section handles the input
    public void setCameraDir(float[] dir){
        animator.setCameraDir(dir);
    }

}
