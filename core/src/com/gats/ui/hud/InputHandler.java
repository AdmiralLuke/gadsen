package com.gats.ui.hud;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.gats.animation.AnimatorCamera;
import com.gats.manager.HumanPlayer;
import com.gats.ui.HudStage;
import com.gats.ui.InGameScreen;

import java.util.ArrayList;
import java.util.List;

public class InputHandler implements GadsenInputProcessor{


	InGameScreen ingameScreen;



	private final int KEY_CAMERA_UP = Input.Keys.UP;
	private final int KEY_CAMERA_DOWN = Input.Keys.DOWN;
	private final int KEY_CAMERA_LEFT = Input.Keys.LEFT;
	private final int KEY_CAMERA_RIGHT = Input.Keys.RIGHT;
	private final int KEY_CAMERA_ZOOM_IN = Input.Keys.I;
	private final int KEY_CAMERA_ZOOM_OUT = Input.Keys.O;
	private final int KEY_CAMERA_ZOOM_RESET = Input.Keys.R;

	private final int KEY_CAMERA_TOGGLE_PLAYER_FOCUS = Input.Keys.F;

	private final int KEY_EXIT_TO_MENU = Input.Keys.ESCAPE;


	private HumanPlayer currentPlayer;
	private Vector2 lastMousePosition;
	private Vector2 deltaMouseMove;
	private boolean rightMousePressed;
	private List<HumanPlayer> humanList = new ArrayList<>();
	private boolean turnInProgress = false;


	//used for storing arrow key input -> for now used for camera
	//first index for x Axis, second for y
	//length 3 because the cameraposition is 3d
	float[] ingameCameraDirection = new float[3];

	float cameraZoomPressed = 0;

	public InputHandler(InGameScreen ingameScreen){
		this.ingameScreen = ingameScreen;
	}






	public void activateTurn(HumanPlayer humanPlayer) {
		currentPlayer = humanPlayer;
//        System.out.printf("Activating turn for player %s\n", humanPlayer.toString());
		turnInProgress = true;
	}

	public void endTurn() {
		turnInProgress = false;
	}

	public void tick(float delta) {
		if (turnInProgress && currentPlayer != null) {
			currentPlayer.tick(delta);
		}
	}

	public void setHumanPlayers(List<HumanPlayer> humanList) {
		this.humanList = humanList;
	}


	/**
	 * Converts the mouse screen-coordinates to worldPosition and calls {@link HumanPlayer#aimToVector(Vector2)} to aim the Indicator
	 * @param screenX
	 * @param screenY
	 */
	private void processMouseAim(int screenX, int screenY){
		Vector2 worldCursorPos = ingameScreen.toWorldCoordinates(new Vector2(screenX,screenY));
		if(currentPlayer!=null) {
			currentPlayer.aimToVector(worldCursorPos);
		}
	}

	/**
	 * Allows the camera to be moved with the mouse by using the position of the new and old mouse positions, to calculate the distance
	 * to move.
	 * @param screenX
	 * @param screenY
	 */
	private void processMouseCameraMove(int screenX,int screenY){
		Vector2 worldCursorPos = ingameScreen.toWorldCoordinates(new Vector2(screenX, screenY));
		deltaMouseMove = worldCursorPos.sub(ingameScreen.toWorldCoordinates(lastMousePosition));
		lastMousePosition = new Vector2(screenX, screenY);

		ingameScreen.moveCameraByOffset(deltaMouseMove);
	}
	/**
	 * Called whenever a button is just pressed.
	 * For now it handles input from the Arrow Keys, used for the camera Movement.
	 * This is done by storing the direction in {@link InputHandler#ingameCameraDirection}.
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
            case Input.Keys.UP:
                ingameCameraDirection[1] -= 1;
                break;
            case Input.Keys.DOWN:
               ingameCameraDirection[1] += 1;
                break;
            case Input.Keys.LEFT:
               ingameCameraDirection[0] += 1;
                break;
            case Input.Keys.RIGHT:
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
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if(button==Input.Buttons.RIGHT){
			lastMousePosition = new Vector2(screenX,screenY);
			rightMousePressed = true;

		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if(button==Input.Buttons.RIGHT){
			rightMousePressed=false;
		}

		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {

		//wenn gezogen wird, alte position nehmen, distanz zur neuen position ermitteln und die Kamera nun um diesen wert verschieben
		if(rightMousePressed) {
			processMouseCameraMove(screenX,screenY);
			return true;
		}

		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {


		processMouseAim(screenX,screenY);
		return false;
	}

	@Override
	public boolean scrolled(float amountX, float amountY) {

		if(amountY!=0) {
			ingameScreen.zoomCamera(amountY / 10);
		}
		return false;
	}


}
