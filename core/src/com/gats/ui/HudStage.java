package com.gats.ui;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gats.animation.AnimatorCamera;
import com.gats.manager.HumanPlayer;
import com.gats.simulation.Simulation;
import com.gats.ui.assets.GADSAssetManager;

import java.util.ArrayList;
import java.util.List;

public class HudStage extends Stage {


    enum Key {
        KEY_CAMERA_UP,
        KEY_CAMERA_DOWN,
        KEY_CAMERA_LEFT,
        KEY_CAMERA_RIGHT,
        KEY_CAMERA_ZOOM_IN,
        KEY_CAMERA_ZOOM_OUT,
        KEY_CAMERA_ZOOM_RESET,
        KEY_EXIT_TO_MENU
    }

    private final int KEY_CAMERA_UP = Keys.UP;
    private final int KEY_CAMERA_DOWN = Keys.DOWN;
    private final int KEY_CAMERA_LEFT = Keys.LEFT;
    private final int KEY_CAMERA_RIGHT = Keys.RIGHT;
    private final int KEY_CAMERA_ZOOM_IN = Keys.I;
    private final int KEY_CAMERA_ZOOM_OUT = Keys.O;
    private final int KEY_CAMERA_ZOOM_RESET = Keys.R;

    private final int KEY_CAMERA_TOGGLE_PLAYER_FOCUS = Keys.F;

    private final int KEY_EXIT_TO_MENU = Keys.ESCAPE;
    private
    SpriteBatch batch;

    InGameScreen ingameScreen;
    AnimatorCamera camera;

    Simulation gameSimulation;
    final int cameraDown = Keys.DOWN;
    final int cameraLeft = Keys.LEFT;
    final int cameraRight = Keys.RIGHT;

    final int exitToMenu = Keys.ESCAPE;

    private HumanPlayer currentPlayer;

    private boolean turnInProgress = false;
    private List<HumanPlayer> humanList = new ArrayList<>();


    public HudStage(Viewport hudViewport, InGameScreen ingameScreen) {
        super(hudViewport);
        this.ingameScreen = ingameScreen;
        batch = new SpriteBatch();

    }


    //used for storing arrow key input -> for now used for camera
    //first index for x Axis, second for y
    //length 3 because the cameraposition is 3d
    float[] ingameCameraDirection = new float[3];

    float cameraZoomPressed = 0;
    boolean cameraReset;

    /**
     * Called whenever a button is just pressed.
     * For now it handles input from the Arrow Keys, used for the camera Movement.
     * This is done by storing the direction in {@link HudStage#ingameCameraDirection}.
     *
     * Also calls {@link HumanPlayer#processKeyDown(int keycode)} for user input.
     * Currently is the default case.
     *
     * @param keycode one of the constants in {@link com.badlogic.gdx.Input.Keys}
     * @return was the input handled
     */

    @Override
    public boolean keyDown(int keycode) {
        //input handling for the camera and ui
        switch (keycode) {
            case KEY_CAMERA_UP:
                ingameCameraDirection[1] += 1;
                break;
            case KEY_CAMERA_DOWN:
                ingameCameraDirection[1] -= 1;
                break;
            case KEY_CAMERA_LEFT:
                ingameCameraDirection[0] -= 1;
                break;
            case KEY_CAMERA_RIGHT:
                ingameCameraDirection[0] += 1;
                break;
            case KEY_EXIT_TO_MENU:
                ingameScreen.dispose();
                break;
            case KEY_CAMERA_ZOOM_IN:
                cameraZoomPressed -= 1;
                break;
            case KEY_CAMERA_ZOOM_OUT:
                cameraZoomPressed += 1;

                break;
            case KEY_CAMERA_ZOOM_RESET:
              ingameScreen.resetCamera();
               //Todo: Zoom Reset
                break;
            case KEY_CAMERA_TOGGLE_PLAYER_FOCUS:
                ingameScreen.toggleCameraMove();

            default:
                if (turnInProgress && currentPlayer != null) {
                    currentPlayer.processKeyDown(keycode);
                }
                break;
        }
        ingameScreen.processInputs(ingameCameraDirection,cameraZoomPressed);


        return true;


    }

    public void activateTurn(HumanPlayer humanPlayer) {
        currentPlayer = humanPlayer;
//        System.out.printf("Activating turn for player %s\n", humanPlayer.toString());
        turnInProgress = true;
    }

    public void endTurn() {
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
                ingameCameraDirection[1] -= 1;
                break;
            case Keys.DOWN:
               ingameCameraDirection[1] += 1;
                break;
            case Keys.LEFT:
               ingameCameraDirection[0] += 1;
                break;
            case Keys.RIGHT:
                ingameCameraDirection[0] -= 1;
                break;
             case KEY_CAMERA_ZOOM_IN:
                cameraZoomPressed += 1;
                break;
            case KEY_CAMERA_ZOOM_OUT:
                cameraZoomPressed -= 1;

                break;
            default:
                if (turnInProgress && currentPlayer != null) {
                    currentPlayer.processKeyUp(keycode);
                }
                break;
        }
        ingameScreen.processInputs(ingameCameraDirection,cameraZoomPressed);

        return true;
    }

    @Override
    public void draw() {
        super.draw();
    }


    protected void tick(float delta) {
        if (turnInProgress && currentPlayer != null) {
            currentPlayer.tick(delta);
        }
    }


    void setCurrentPlayer(HumanPlayer currentHumanPlayer) {
        this.currentPlayer = currentHumanPlayer;
    }

    public void setHumanPlayers(List<HumanPlayer> humanList) {
        this.humanList = humanList;
    }
}
