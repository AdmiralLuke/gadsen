package com.gats.animation.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.gats.animation.GameCharacter;

/**
 * Class for displaying the Health of a {@link GameCharacter}
 *
 */

public class Healthbar extends Entity implements Toggleable {

	public boolean isShown;

	/**
	 * Represents the currently displayed health of the player
	 */
	private ProgressBar healthProgressBar;

	public Healthbar(NinePatch healthbarBackground, Vector2 relPos,int maxHealth) {

		ProgressBar.ProgressBarStyle progStyle = new ProgressBar.ProgressBarStyle();
		//background of the healtbar

		progStyle.background = new NinePatchDrawable(healthbarBackground);
		//create pixmap for the color representation
		Pixmap pixmap = new Pixmap(1,2, Pixmap.Format.RGBA8888);
		pixmap.setColor(Color.RED);
		pixmap.fill();
		TextureRegionDrawable healthBarColor = new TextureRegionDrawable(new Texture(pixmap));
		pixmap.dispose();

		//area representing health
		progStyle.knobBefore = new TextureRegionDrawable(healthBarColor);

		//knob of 0 width,  "points" to current health
		pixmap = new Pixmap(0,2,Pixmap.Format.RGBA8888);
		pixmap.setColor(Color.GREEN);
		pixmap.fill();

		progStyle.knob = new TextureRegionDrawable(new Texture(pixmap));
		pixmap.dispose();


		this.healthProgressBar = new ProgressBar(0,maxHealth,1,false,progStyle);
		healthProgressBar.setValue(maxHealth);


		//hardcoded size
		healthProgressBar.setSize(26,2);



		this.isShown = true;

		//relative Pos in regards to the player. centered
		this.setRelPos(relPos.x- healthProgressBar.getWidth()/2,relPos.y);
	}

	public void updateHealth(int currentHealth){
		healthProgressBar.setValue(currentHealth);
	}

	@Override
	public void draw(Batch batch, float deltaTime, float parentAlpha) {
		if(isShown) {
			healthProgressBar.draw(batch, parentAlpha);
			super.draw(batch, deltaTime, parentAlpha);
		}
	}


	@Override
	public void updatePos() {
		super.updatePos();
		Vector2 pos = getPos();
		healthProgressBar.setPosition(pos.x, pos.y);
	}

	@Override
	public void toggle() {
		isShown = !isShown;
	}

	@Override
	public void setEnabled(boolean isEnabled) {
		isShown = isEnabled;
	}

	@Override
	public void setRotationAngle(float angle) {
		super.setRotationAngle(angle);
		healthProgressBar.setRotation(angle);
	}
}
