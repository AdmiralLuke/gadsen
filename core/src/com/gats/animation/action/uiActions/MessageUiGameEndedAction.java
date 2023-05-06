package com.gats.animation.action.uiActions;

import com.gats.ui.hud.UiMessenger;


/**
 * Animator Action for notifying the Ui that the Game is Over
 */
public class MessageUiGameEndedAction extends MessageUiAction{

	boolean won;
	int team;
	public MessageUiGameEndedAction(float start, UiMessenger uiMessenger, boolean won, int team) {
		super(start, uiMessenger);
		won = won;
		team = team;

	}

	@Override
	protected void runAction(float oldTime, float current) {
		uiMessenger.gameEnded(won,team);
	}
}
