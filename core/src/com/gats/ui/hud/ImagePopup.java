package com.gats.ui.hud;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Creates an Image that is only drawn for a limited amount of time.
 * After that it removes itself from the stage.
 */
public class ImagePopup extends Image {

	float drawDuration;
	float currentTime;


	/**
	 * Creates an {@link ImagePopup} wich is drawn for a limited amount of time.
	 * @param image to Draw
	 * @param posX
	 * @param posY
	 * @param initalDelay in seconds
	 * @param drawDuration in seconds
	 */

	public ImagePopup(TextureRegion image,float posX,float posY, float initalDelay,float drawDuration){
		super(image);
		setPosition(posX,posY);
		this.currentTime = -initalDelay;
		this.drawDuration = drawDuration;

	}

	/**
	 * Creates an {@link ImagePopup} wich is drawn for a limited amount of time.
	 * @param image to draw
	 * @param drawDuration in seconds
	 */
	public ImagePopup(TextureRegion image,float drawDuration){
		this(image,0,0,0,drawDuration);
	}


	@Override
	public void draw(Batch batch, float parentAlpha) {
		if(drawDuration>0||currentTime<0) {
			currentTime += Gdx.graphics.getDeltaTime();
		}
		if(currentTime>=0) {

			//todo draw background with a sprite
			super.draw(batch, parentAlpha);
			if(drawDuration>0&&currentTime>drawDuration){
				remove();
			}

		}
	}

}
