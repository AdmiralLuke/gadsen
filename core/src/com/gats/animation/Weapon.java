package com.gats.animation;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.gats.animation.entity.AnimatedEntity;

public class Weapon extends AnimatedEntity {

    private Animation<TextureRegion> holdingAnimation;
    private Animation<TextureRegion> carryAnimation;


    public Weapon(Animation<TextureRegion> holdingAnimation, Animation<TextureRegion> carryAnimation) {
        super(carryAnimation);
        this.holdingAnimation = holdingAnimation;
        this.carryAnimation = carryAnimation;
    }


    public void setHolding(boolean holding) {
        if (holding) setAnimation(holdingAnimation);
        else setAnimation(carryAnimation);
    }
}
