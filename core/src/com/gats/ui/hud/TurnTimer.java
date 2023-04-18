package com.gats.ui.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import com.gats.ui.assets.AssetContainer;
import com.gats.ui.menu.buttons.ColoredLabelWithBackground;

/**
 * Class representing and drawing the TimerSprite and the remaining time.
 */
public class TurnTimer extends Table {

	Image timerImage;
	Label timeDisplay;
	Timer timer;

	int time;

	public TurnTimer(TextureRegion timerTexture, Skin labelSkin){
		this.timer = new Timer();
		time = 0;
		timer.scheduleTask(new Timer.Task() {
			@Override
			public void run() {
				tick();
			}
		},0,1);
		this.timerImage = new Image(timerTexture);
		//Todo, change label to ColoredLabel from other branch
		timeDisplay = new ColoredLabelWithBackground("", AssetContainer.MainMenuAssets.skin, Color.WHITE,new TextureRegionDrawable(AssetContainer.IngameAssets.pixel));


		//add both to the horizontal group for drawing them next to each other
		add(timeDisplay).width(44);
		timeDisplay.setFontScale(3);
		add(timerImage).width(64).height(64).fill();
	}


	public void setCurrentTime(int seconds){
		timeDisplay.setText(seconds);
		time=seconds;
	}
	public void startTimer(){
		timer.start();
	}

	public void stopTimer(){
		timer.stop();
	}

	private void tick(){
		time--;
		timeDisplay.setText(time);
	}
}
