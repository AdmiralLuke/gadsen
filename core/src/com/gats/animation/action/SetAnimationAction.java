package com.gats.animation.action;

import com.gats.animation.GameCharacter;
import com.gats.animation.entity.Entity;
import com.gats.simulation.Path;

public class SetAnimationAction extends Action{
    private GameCharacter target;
    private GameCharacter.AnimationType type;

    public SetAnimationAction(float delay, GameCharacter target, GameCharacter.AnimationType type) {
        super(delay);
        this.target = target;
        this.type = type;
    }

    @Override
    protected void runAction(float oldTime, float current) {
        if(target!= null) target.setAnimation(type);
        endAction(oldTime);
    }

    public void setTarget(GameCharacter target) {
        this.target = target;
    }
}
