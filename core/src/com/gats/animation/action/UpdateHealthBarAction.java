package com.gats.animation.action;

import com.gats.animation.action.uiActions.MessageUiAction;
import com.gats.animation.entity.Healthbar;
import com.gats.ui.hud.UiMessenger;

public class UpdateHealthBarAction extends Action {

	int health;
	Healthbar healthbar;
	public UpdateHealthBarAction(float start, int newHealth, Healthbar healthbar) {
		super(start);
		health = newHealth;
		this.healthbar = healthbar;
	}

	@Override
	protected void runAction(float oldTime, float current) {
		healthbar.updateHealth(health);
		endAction(oldTime);
	}
}
