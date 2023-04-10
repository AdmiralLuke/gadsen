package com.gats.animation.action.uiActions;

import com.gats.simulation.WeaponType;
import com.gats.ui.hud.UiMessenger;

/**
 * Action to Signal a WeaponSelect via the Type of the new Weapon to the Ui
 */
public class MessageUiWeaponSelectAction extends MessageUiAction {

	WeaponType type;
	public MessageUiWeaponSelectAction(float start, UiMessenger uiMessenger, WeaponType type) {
		super(start, uiMessenger);
		this.type = type;
	}

	@Override
	protected void runAction(float oldTime, float current) {
		uiMessenger.changeSelectedWeapon(type);
		endAction(oldTime);
	}
}
