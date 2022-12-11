package com.gats.ui;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.HdpiMode;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gats.animation.Animator;
import com.gats.manager.HumanPlayer;
import com.gats.simulation.CharacterMoveAction;
import com.gats.simulation.GameCharacter;
import com.gats.simulation.GameCharacterController;
import com.gats.simulation.Simulation;
import org.junit.Assert;
import org.lwjgl.Sys;

import java.util.ArrayList;
import java.util.List;

public class HudStage extends Stage {

    SpriteBatch batch;
    TextureAtlas atlas;
    TextureRegion healthbar;
    GADSAssetManager assetManager;

    InGameScreen ingameScreen;
    Simulation gameSimulation;
    final int cameraUp = Keys.UP;
    final int cameraDown = Keys.DOWN;
    final int cameraLeft = Keys.LEFT;
    final int cameraRight = Keys.RIGHT;

    private HumanPlayer currentPlayer;

    private boolean turnInProgress = false;
    private List<HumanPlayer> humanList = new ArrayList<>();


    public HudStage(Viewport hudViewport, InGameScreen ingameScreen, GADSAssetManager assetManager) {
        super(hudViewport);
        this.ingameScreen = ingameScreen;
        batch = new SpriteBatch();
        this.atlas = assetManager.getAtlas();

    }


    //used for storing arrow key input -> for now used for camera
    //first index for x Axis, second for y
    //length 3 because the cameraposition is 3d
    float[] directions = new float[3];


    /**
     * Called whenever a button is just pressed.
     * For now it handles input from the Arrow Keys, used for the camera Movement.
     * This is done by storing the direction in {@link HudStage#directions}.
     *
     * @param keycode one of the constants in {@link com.badlogic.gdx.Input.Keys}
     * @return was the input handled
     */

    @Override
    public boolean keyDown(int keycode) {

        //input handling for the camera
        switch (keycode) {
            case cameraUp:
                directions[1] += 1;
                break;
            case cameraDown:
                directions[1] -= 1;
                break;
            case cameraLeft:
                directions[0] -= 1;
                break;
            case cameraRight:
                directions[0] += 1;
                break;
            default:
                if (turnInProgress && currentPlayer != null) {
                    currentPlayer.processKeyDown(keycode);
                }
                break;
        }
        ingameScreen.setCameraDir(directions);



        return true;


    }

    public void activateTurn(HumanPlayer humanPlayer){
        currentPlayer = humanPlayer;
//        System.out.printf("Activating turn for player %s\n", humanPlayer.toString());
        turnInProgress = true;
    }

    public void endTurn(){
        turnInProgress = false;
    }

    /**
     * Called whenever a button stops getting pressed/is lifted up.
     * Resets the values to some Keypresses, changed in {@link HudStage#keyDown(int)}
     *
     * @param keycode one of the constants in {@link com.badlogic.gdx.Input.Keys}
     * @return
     */
    @Override
    public boolean keyUp(int keycode) {

        switch (keycode) {
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
            default:
                if (turnInProgress && currentPlayer != null) {
                    currentPlayer.processKeyUp(keycode);
                }
                break;
        }
        ingameScreen.setCameraDir(directions);

        return true;
    }

    @Override
    public void draw() {
        super.draw();
    }


    void setCurrentPlayer(HumanPlayer currentHumanPlayer) {
        this.currentPlayer = currentHumanPlayer;
    }

    public void setHumanPlayers(List<HumanPlayer> humanList) {
        this.humanList = humanList;
    }
}
