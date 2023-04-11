package com.gats.animation.action.uiActions;

import com.gats.simulation.GameCharacter;
import com.gats.ui.hud.UiMessenger;

/**
 * Calls ui Functions to update based on Player movement
 */
public class MessageUiPlayerMoveAction extends MessageUiAction{

	GameCharacter currPlayer;
	public MessageUiPlayerMoveAction(float start, UiMessenger uiMessenger, GameCharacter currentPlayer) {
		super(start, uiMessenger);
		this.currPlayer = currentPlayer;
	}

	@Override
	protected void runAction(float oldTime, float current) {
		uiMessenger.playerMoved(currPlayer);
		endAction(oldTime);
	}
}
