package com.gats.animation.action.uiActions;

import com.gats.simulation.GameCharacter;
import com.gats.ui.hud.UiMessenger;

public class ChangeInventoryAction extends NotifyUiAction {

GameCharacter currentPlayer;
	public ChangeInventoryAction(float start, UiMessenger uiMessenger, GameCharacter currentPlayer) {
		super(start, uiMessenger);
		this.currentPlayer = currentPlayer;

	}

	@Override
	protected void runAction(float oldTime, float current) {
		uiMessenger.turnChanged(currentPlayer);
		endAction(oldTime);
	}
}
