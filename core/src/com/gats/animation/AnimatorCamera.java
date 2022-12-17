package com.gats.animation;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class AnimatorCamera extends OrthographicCamera {

    //Todo Comment this class
    private final float defaultZoomValue = 1f;
    private final float zoomIncrement = 0.01f;
    private final Vector3 defaultPosition;
    private Vector3 cameraDirection;
    private float cameraSpeed;

    //1 = zoom out, 0 = no zoom, -1 = zoom in
    private float cameraZoomPressed;

    public AnimatorCamera(float viewportWidth, float viewportHeight) {
        super(viewportWidth,viewportHeight);
        this.defaultPosition = new Vector3(0,0,0);
        this.cameraDirection = new Vector3(0,0,0);
        this.cameraSpeed = 200;

    }

     /**
     * Moves the camera with the Speed defined in {@link AnimatorCamera#cameraSpeed} and the {@link AnimatorCamera#cameraDirection}.
     *
     * @param delta Delta-time of the Application.
     */
    private void processMoveInput(float delta) {

        this.translate(new Vector3(cameraDirection).scl(cameraSpeed * delta));

    }
    private void processZoomInput(float delta){
        if(this.zoom > 0.1||cameraZoomPressed>0) {
            this.zoom += zoomIncrement * cameraZoomPressed;
        }

    }
    public void resetCamera(){
        this.zoom = defaultZoomValue;
        this.position.set(defaultPosition);
    }


    public void setDirections(float[] ingameCameraDirection) {
             //left*-cameraSpeed+right*cameraSpeed,up*cameraSpeed+down*-cameraSpeed,0
            //for later camera.position.lerp() for smooth movement
       this.cameraDirection = new Vector3(ingameCameraDirection);
    }

    public void updateMovement(float delta) {
       processMoveInput(delta);
       processZoomInput(delta);
    }

    public void setZoomPressed(float zoomPressed) {
        this.cameraZoomPressed = zoomPressed;
    }
}
