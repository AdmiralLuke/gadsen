package com.gats.animation.action;

import com.gats.animation.GameCharacter;
import com.gats.animation.entity.Entity;
import com.gats.animation.entity.Parent;

public class AddAction extends Action{

    private final Parent target;
    private final Entity toAdd;

    public AddAction(float delay, Parent target, Entity toAdd) {
        super(delay);
        this.target = target;
        this.toAdd = toAdd;
    }

    @Override
    protected void runAction(float oldTime, float current) {
        if (target != null) target.add(toAdd);
        endAction(oldTime);
    }
}
