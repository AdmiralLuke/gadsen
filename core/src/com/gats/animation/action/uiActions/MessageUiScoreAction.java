package com.gats.animation.action.uiActions;

import com.gats.simulation.GameCharacter;
import com.gats.ui.hud.UiMessenger;

/**
 * Calls ui Functions to update based on Player movement
 */
public class MessageUiScoreAction extends MessageUiAction {

    private int staminaBefore;
    private int staminaAfter;
    GameCharacter currPlayer;
    private int team;
    private float score;

    public MessageUiScoreAction(float start, UiMessenger uiMessenger, int team, float score) {
        super(start, uiMessenger);
        this.team = team;
        this.score = score;
    }

    @Override
    protected void runAction(float oldTime, float current) {
        uiMessenger.teamScore(team, score);
        endAction(oldTime);
    }
}
