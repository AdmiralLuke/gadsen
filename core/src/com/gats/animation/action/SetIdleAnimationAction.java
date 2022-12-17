package com.gats.animation.action;

import com.gats.animation.GameCharacter;

public class SetIdleAnimationAction extends Action{
    private GameCharacter target;
    private GameCharacter.AnimationType type;

    public SetIdleAnimationAction(float delay, GameCharacter target, GameCharacter.AnimationType type) {
        super(delay);
        this.target = target;
        this.type = type;
    }

    @Override
    protected void runAction(float oldTime, float current) {
        if(target!= null) target.setIdleType(type);
        endAction(oldTime);
    }

    public void setTarget(GameCharacter target) {
        this.target = target;
    }
}
