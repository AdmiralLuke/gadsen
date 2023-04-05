package com.gats.animation;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.gats.animation.entity.Entity;
import com.gats.animation.entity.GameCharacterHudElement;
import com.gats.animation.GameCharacter;

/**
 * Visual representation of the aiming Angle and Strength
 */
public class AimIndicator extends GameCharacterHudElement {


    private boolean aimActive = false;

    private Vector2 posOffset;

    public AimIndicator(TextureRegion hudSprite, GameCharacter gameCharacter){
        super(hudSprite, new Vector2(0,0), new Vector2(hudSprite.getRegionWidth(), hudSprite.getRegionHeight()),gameCharacter);
        setOrigin(new Vector2(0, hudSprite.getRegionHeight()/2f));
        gameCharacter.setAimingIndicator(this);
    }

    @Override
    public void draw(Batch batch, float deltaTime, float parentAlpha) {

        if (aimActive) super.draw(batch, deltaTime, parentAlpha);

    }

    public void aimActive(boolean active) {
        aimActive = active;
    }
}
