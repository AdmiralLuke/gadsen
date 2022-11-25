package com.gats.animation;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Repräsentiert ein Projektil einer Waffe
 */
public class Projectile extends AnimatedEntity{

    public Projectile(Animation<TextureRegion> animation) {
        super(animation);
    }
}
