package com.gats.ui;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gats.animation.Animator;
import com.gats.simulation.Simulation;
import org.junit.Assert;

public class HudStage extends Stage {

    SpriteBatch batch;
    TextureAtlas atlas;
    TextureRegion healthbar;
    GADSAssetManager assetManager;

    InGameScreen ingameScreen;
    public HudStage(Viewport hudViewport, InGameScreen ingameScreen, GADSAssetManager assetManager) {
        super(hudViewport);
        this.ingameScreen = ingameScreen;
        batch = new SpriteBatch();
        this.atlas = assetManager.getAtlas();

    }


    int left, right, up, down;
    //used for storing arrow key input -> for now used for camera
    //first index for x Axis, second for y
    //length 3 because the cameraposition is 3d
    float[] directions = new float[3];

    /**
     * Called whenever a button stops being pressed.
     * For now it handles input from the Arrow Keys, used for the camera Movement.
     * This is done by storing the direction in {@link HudStage#directions}.
     * @param keycode one of the constants in {@link com.badlogic.gdx.Input.Keys}
     * @return was the input handled
     */
    @Override public boolean keyDown(int keycode){


        switch (keycode){
            case Keys.UP:
                directions[1] +=  1;
                break;
            case Keys.DOWN:
                directions[1] -= 1;
                break;
            case Keys.LEFT:
                directions[0] -= 1;
                break;
            case Keys.RIGHT:
                directions[0] += 1;
                break;
        }
        ingameScreen.setCameraDir(directions);
        return true;
    }

    @Override public boolean keyUp(int keycode){

switch (keycode){
            case Keys.UP:
                directions[1] -= 1;
                break;
            case Keys.DOWN:
                directions[1] += 1;
                break;
            case Keys.LEFT:
                directions[0] += 1;
                break;
            case Keys.RIGHT:
                directions[0] -= 1;
                break;
        }
        ingameScreen.setCameraDir(directions);
        return true;
    }

    @Override
    public void draw() {
        super.draw();
    }
}
