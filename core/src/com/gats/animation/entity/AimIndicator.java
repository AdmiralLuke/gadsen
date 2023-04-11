package com.gats.animation.entity;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * Visual representation of the aiming Angle and Strength
 */
public class AimIndicator extends SpriteEntity implements Toggleable {


    private boolean aimActive = false;

    public AimIndicator(TextureRegion hudSprite){
        super(hudSprite, new Vector2(0,0), new Vector2(hudSprite.getRegionWidth(), hudSprite.getRegionHeight()));
        setOrigin(new Vector2(0, hudSprite.getRegionHeight()/2f));
    }

    @Override
    public void draw(Batch batch, float deltaTime, float parentAlpha) {

        if (aimActive) super.draw(batch, deltaTime, parentAlpha);

    }

    @Override
    public void toggle() {
        aimActive = !aimActive;
    }

    @Override
    public void setEnabled(boolean isEnabled) {
        aimActive = isEnabled;
    }
}
