package com.gats.animation.action;

import com.gats.animation.Animator;
import com.gats.animation.GameCharacter;

public class CharacterSelectAction extends Action {

    public interface GameCharacterChanger {
        GameCharacter setNewCharacter(GameCharacter newCharacter);
    }

    private GameCharacter target;
    private GameCharacterChanger changer;

    public CharacterSelectAction(float delay, GameCharacter target, GameCharacterChanger changer) {
        super(delay);
        this.target = target;
        this.changer = changer;
    }

    @Override
    protected void runAction(float oldTime, float current) {
        if (changer != null){
            GameCharacter oldTarget = changer.setNewCharacter(target);
            if (oldTarget != null) oldTarget.getAimingIndicator().aimActive(false);
        }
        target.getAimingIndicator().aimActive(true);
        endAction(oldTime);
    }
}
