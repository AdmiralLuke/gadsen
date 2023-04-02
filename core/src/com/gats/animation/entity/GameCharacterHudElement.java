package com.gats.animation.entity;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.gats.animation.GameCharacter;

/**
 * Abstract class representing Character depentdant HudElements like Healtbar and AimIndicator
 */
public abstract class GameCharacterHudElement extends SpriteEntity {

//Todo add drawing offset so that something like the healthbar can easily be drawn with an offset relative to the game character.
    private GameCharacter gameCharacter;
    public GameCharacterHudElement(TextureRegion textureRegion, Vector2 relPos, Vector2 size,GameCharacter gameCharacter) {
        super(textureRegion, relPos, size);
        this.gameCharacter = gameCharacter;

    }
    public GameCharacter getGameCharacter(){
        return gameCharacter;
    }

}
