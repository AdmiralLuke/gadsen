package com.gats.animation.action.uiActions;

import com.gats.simulation.GameCharacter;
import com.gats.simulation.WeaponType;
import com.gats.ui.hud.UiMessenger;

public class UpdateInventoryItem extends NotifyUiAction{

	GameCharacter currentPlayer;
	WeaponType weaponType;
	public UpdateInventoryItem(float start, UiMessenger uiMessenger, GameCharacter currentPlayer, WeaponType type) {
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
