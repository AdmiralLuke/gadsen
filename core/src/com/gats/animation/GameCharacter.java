package com.gats.animation;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.gats.animation.entity.AnimatedEntity;
import com.gats.animation.entity.GameCharacterHudElement;

/**
 * Repräsentiert eine Spielfigur auf der Karte
 */
public class GameCharacter extends AnimatedEntity {
    private AimIndicator aimingIndicator;

    public GameCharacter(Animation<TextureRegion> animation) {
        super(animation);
        animation.setPlayMode(Animation.PlayMode.LOOP);
    }

    public GameCharacter(Animation<TextureRegion> animation, AimIndicator aimIndicator){
        super(animation);
        animation.setPlayMode(Animation.PlayMode.LOOP);
        this.aimingIndicator = aimIndicator;
    }

    @Override
    public void draw(Batch batch, float deltaTime, float parentAlpha) {

        super.draw(batch, deltaTime, parentAlpha);
        if(aimingIndicator!=null){
            aimingIndicator.draw(batch,deltaTime,parentAlpha);
        }
    }

    public AimIndicator getAimingIndicator(){
        return this.aimingIndicator;
    }

    public void setAimingIndicator(AimIndicator aimIndicator) {
        this.aimingIndicator = aimIndicator;
    }
}
