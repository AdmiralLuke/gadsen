package com.gats.animation.action.uiActions;

import com.gats.ui.hud.UiMessenger;

public class ChangeAimValuesAction extends NotifyUiAction{

	float angle;
	float strength;
	public ChangeAimValuesAction(float start, UiMessenger uiMessenger,float angle,float strength) {
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
