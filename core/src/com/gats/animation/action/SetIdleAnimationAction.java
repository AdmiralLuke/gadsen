package com.gats.animation.action;

import com.gats.animation.GameCharacter;
import com.gats.ui.assets.AssetContainer;

public class SetIdleAnimationAction extends Action{
    private GameCharacter target;
    private AssetContainer.IngameAssets.GameCharacterAnimationType type;

    public SetIdleAnimationAction(float delay, GameCharacter target, AssetContainer.IngameAssets.GameCharacterAnimationType type) {
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
