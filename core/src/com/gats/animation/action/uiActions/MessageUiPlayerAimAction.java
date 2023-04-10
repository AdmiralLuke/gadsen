package com.gats.animation.action.uiActions;

import com.gats.ui.hud.UiMessenger;

/**
 * Action to update the Ui with Information of the current aim Values
 */
public class MessageUiPlayerAimAction extends MessageUiAction {

	float angle;
	float strength;
	public MessageUiPlayerAimAction(float start, UiMessenger uiMessenger, float angle, float strength) {
		super(start, uiMessenger);
		this.angle =angle;
		this.strength = strength;
	}

	@Override
	protected void runAction(float oldTime, float current) {
		uiMessenger.playerAimed(angle,strength);
		endAction(oldTime);
	}

}
