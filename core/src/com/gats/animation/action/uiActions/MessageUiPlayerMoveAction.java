package com.gats.animation.action.uiActions;

import com.gats.simulation.GameCharacter;
import com.gats.ui.hud.UiMessenger;

/**
 * Calls ui Functions to update based on Player movement
 */
public class MessageUiPlayerMoveAction extends MessageUiAction {

    private float endTime;
    private int staminaBefore;
    private int staminaAfter;
    private float duration;
    GameCharacter currPlayer;

    public MessageUiPlayerMoveAction(float start, float duration, UiMessenger uiMessenger, GameCharacter currentPlayer, int staminaBefore, int staminaAfter) {
        super(start, uiMessenger);
        this.duration = duration;
        this.currPlayer = currentPlayer;
        this.endTime = delay + duration;

        this.staminaBefore = staminaBefore;
        this.staminaAfter = staminaAfter;
    }

    @Override
    protected void runAction(float oldTime, float current) {
        uiMessenger.playerMoved(currPlayer, (int) (staminaBefore + (Math.min(current, endTime) - delay) / duration * (staminaAfter - staminaBefore)));
        if (current > endTime) endAction(endTime);
    }
}
