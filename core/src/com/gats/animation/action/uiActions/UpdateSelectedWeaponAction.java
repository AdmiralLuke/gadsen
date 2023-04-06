package com.gats.animation.action.uiActions;

import com.gats.simulation.WeaponType;
import com.gats.ui.hud.UiMessenger;

public class UpdateSelectedWeaponAction extends NotifyUiAction{

	WeaponType type;
	public UpdateSelectedWeaponAction(float start, UiMessenger uiMessenger, WeaponType type) {
		super(start, uiMessenger);
		this.type = type;
	}

	@Override
	protected void runAction(float oldTime, float current) {
		uiMessenger.changeSelectedWeapon(type);
		endAction(oldTime);
	}
}
