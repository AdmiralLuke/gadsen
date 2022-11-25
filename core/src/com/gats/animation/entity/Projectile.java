package com.gats.animation.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.gats.animation.AnimatedEntity;

/**
 * Repräsentiert ein Projektil einer Waffe
 */
public class Projectile extends AnimatedEntity {

    public Projectile(Animation<TextureRegion> animation) {
        super(animation);
    }
}
