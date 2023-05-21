package com.gats.animation.action.uiActions;

import com.gats.simulation.GameCharacter;
import com.gats.simulation.GameState;
import com.gats.ui.hud.UiMessenger;

/**
 * Action to signal a Turn start with the current {@link GameCharacter} to the ui
 */
public class MessageUiTurnStartAction extends MessageUiAction {


	GameCharacter currentPlayer;
	com.gats.animation.GameCharacter animPlayer;
	GameState state;

	public MessageUiTurnStartAction(float start, UiMessenger uiMessenger, GameCharacter currentPlayer, com.gats.animation.GameCharacter animPlayer,GameState state) {
		super(start, uiMessenger);
		this.currentPlayer = currentPlayer;
		this.animPlayer = animPlayer;
		this.state = state;
	}

	@Override
	protected void runAction(float oldTime, float current) {
		uiMessenger.turnChanged(state,currentPlayer,animPlayer);
		endAction(oldTime);
	}
}
