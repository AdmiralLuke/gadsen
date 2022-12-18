package com.gats.animation.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;

/**
 * Entity that gets summoned once a Player has Won.
 */
public class WinEntity extends SpriteEntity{

    public WinEntity(TextureRegion textureRegion, Vector2 screenCenter) {
        super(textureRegion,screenCenter,new Vector2(textureRegion.getRegionWidth()*2,textureRegion.getRegionHeight()*2));
        this.setRelPos(screenCenter.sub(getSpriteCenter()));
    }

    //todo also display winning team name/number
}
