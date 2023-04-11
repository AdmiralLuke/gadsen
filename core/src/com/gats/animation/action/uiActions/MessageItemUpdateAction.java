package com.gats.animation.action.uiActions;

import com.gats.simulation.GameCharacter;
import com.gats.simulation.WeaponType;
import com.gats.ui.hud.UiMessenger;

/**
 * Animator Action meant to communicate changes in regards to an Item, to the ui.
 */
public class MessageItemUpdateAction extends MessageUiAction {

	GameCharacter currentPlayer;
	WeaponType weaponType;
	public MessageItemUpdateAction(float start, UiMessenger uiMessenger, GameCharacter currentPlayer, WeaponType type) {
		super(start, uiMessenger);
		this.currentPlayer = currentPlayer;
		this.weaponType = type;
	}

	@Override
	protected void runAction(float oldTime, float current) {
		uiMessenger.updateInventoryItem(currentPlayer,weaponType);
		endAction(oldTime);
	}
}
