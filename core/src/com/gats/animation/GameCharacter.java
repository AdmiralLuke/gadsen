package com.gats.animation;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Repräsentiert eine Spielfigur auf der Karte
 */
public class GameCharacter extends AnimatedEntity{
    public GameCharacter(Animation<TextureRegion> animation) {
        super(animation);
        animation.setPlayMode(Animation.PlayMode.LOOP);
    }
}
