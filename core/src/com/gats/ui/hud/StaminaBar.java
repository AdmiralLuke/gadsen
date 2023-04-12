package com.gats.ui.hud;

import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class StaminaBar extends ProgressBar {
	public StaminaBar(float min, float max, float stepSize, boolean vertical, Skin skin) {
		super(min, max, stepSize, vertical, skin);
	}

	public void setMaxStamina(int stamina) {
		setRange(0, stamina);
	}

	public void resetStamina() {
		setValue(getMaxValue());
	}

	public void updateCurrentStamina(int currentStamina){
		setValue(currentStamina);
	}

}
