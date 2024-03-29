package com.gats.animation.entity;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.gats.animation.GameCharacter;
import com.gats.ui.assets.AssetContainer;

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

	public Healthbar(com.gats.simulation.GameCharacter currSimPlayer, com.gats.animation.GameCharacter currPlayer) {
		int maxHealth = 100; //currSimPlayer.getHealth();
		//ProgressBar.ProgressBarStyle progStyle = new ProgressBar.ProgressBarStyle();
		//background of the healtbar

		//progStyle.background = new NinePatchDrawable(healthbarBackground);
		//create pixmap for the color representation
		//Pixmap pixmap = new Pixmap(1,2, Pixmap.Format.RGBA8888);
		//pixmap.setColor(Color.RED);
		//pixmap.fill();
		//TextureRegionDrawable healthBarColor = new TextureRegionDrawable(new Texture(pixmap));
		//pixmap.dispose();

		////area representing health
		//progStyle.knobBefore = new TextureRegionDrawable(healthBarColor);

		////knob of 0 width,  "points" to current health
		//pixmap = new Pixmap(0,2,Pixmap.Format.RGBA8888);
		//pixmap.setColor(Color.GREEN);
		//pixmap.fill();

		//progStyle.knob = new TextureRegionDrawable(new Texture(pixmap));
		//pixmap.dispose();


		this.healthProgressBar = new ProgressBar(0, maxHealth,1,false, AssetContainer.IngameAssets.healthbarStyle);
		healthProgressBar.setValue(currSimPlayer.getHealth());


		//hardcoded size
		healthProgressBar.setSize(26,2);



		this.isShown = true;

		//relative Pos in regards to the player. centered
		Vector2 relPos = this.getRelPos();
		this.setRelPos(relPos.x- healthProgressBar.getWidth()/2,relPos.y+10);

		currPlayer.setHealthbar(this);
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
		if (parent == null) setPos(getRelPos());
		else {
			setPos(parent.getPos().cpy().add(getRelPos().cpy()));
		}
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
	public void setRelAngle(float angle) {
		super.setRelAngle(angle);
		//healthProgressBar.setRotation(angle);
	}
}
