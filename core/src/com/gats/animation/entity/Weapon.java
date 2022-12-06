package com.gats.animation.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Repräsentiert eine von einem Character gehaltene Waffe
 */
public class Weapon extends AnimatedEntity {
    public Weapon(Animation<TextureRegion> animation) {
        super(animation);
    }
}
