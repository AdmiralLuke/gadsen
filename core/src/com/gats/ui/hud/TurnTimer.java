package com.gats.ui.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Class representing and drawing the TimerSprite and the remaining time.
 */
public class TurnTimer extends HorizontalGroup {

	Image timerImage;
	Label timeDisplay;

	public TurnTimer(TextureRegion timer, Skin labelSkin){

		this.timerImage = new Image(timer);
		//Todo, change label to ColoredLabel from other branch
		timeDisplay = new Label("",labelSkin);
		timeDisplay.setColor(Color.GREEN);


		//add both to the horizontal group for drawing them next to each other
		addActor(timeDisplay);

		space(5);
		addActor(timerImage);
	}


	public void setCurrentTime(int seconds){
		timeDisplay.setText(seconds);
	}
}
