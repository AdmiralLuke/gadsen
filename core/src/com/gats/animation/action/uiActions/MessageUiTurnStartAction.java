package com.gats.animation.action.uiActions;

import com.gats.simulation.GameCharacter;
import com.gats.ui.hud.UiMessenger;

/**
 * Action to signal a Turn start with the current {@link GameCharacter} to the ui
 */
public class MessageUiTurnStartAction extends MessageUiAction {


	GameCharacter currentPlayer;
	public MessageUiTurnStartAction(float start, UiMessenger uiMessenger, GameCharacter currentPlayer) {
		super(start, uiMessenger);
		this.currentPlayer = currentPlayer;
	}

	@Override
	protected void runAction(float oldTime, float current) {
		uiMessenger.turnChanged(currentPlayer);
		endAction(oldTime);
	}
}
