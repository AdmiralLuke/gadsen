package com.gats.animation.action.uiActions;

import com.gats.simulation.GameCharacter;
import com.gats.simulation.WeaponType;
import com.gats.ui.hud.UiMessenger;

/**
 * Animator Action meant to communicate changes in regards to an Item, to the ui.
 */
public class MessageItemUpdateAction extends MessageUiAction {

	int team;
	WeaponType weaponType;
	int amount;
	public MessageItemUpdateAction(float start, UiMessenger uiMessenger, int team, WeaponType type, int amount) {
		super(start, uiMessenger);
		this.team = team;
		this.weaponType = type;
		this.amount = amount;
	}

	@Override
	protected void runAction(float oldTime, float current) {
		uiMessenger.updateInventoryItem(team, weaponType, amount);
		endAction(oldTime);
	}
}
