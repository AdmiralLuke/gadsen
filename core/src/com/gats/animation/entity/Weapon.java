package com.gats.animation.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * Repräsentiert eine von einem Character gehaltene Waffe
 */
public class Weapon extends AnimatedEntity {
    public Weapon(Animation<TextureRegion> animation) {
        super(animation, new Vector2(animation.getKeyFrame(0).getRegionWidth(), animation.getKeyFrame(0).getRegionHeight()));
    }
}
