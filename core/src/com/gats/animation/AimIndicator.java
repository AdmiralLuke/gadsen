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




    private Vector2 posOffset;

    public AimIndicator(TextureRegion hudSprite,Vector2 posOffset, GameCharacter gameCharacter){
        super(hudSprite,gameCharacter.getPos().cpy().add(posOffset),new Vector2(hudSprite.getRegionWidth(),hudSprite.getRegionHeight()),gameCharacter);
        this.posOffset = posOffset;
    }

    @Override
    public void draw(Batch batch, float deltaTime, float parentAlpha) {

        this.setRelPos(getGameCharacter().getRelPos().cpy().add(posOffset));
        super.draw(batch, deltaTime, parentAlpha);

    }
}
