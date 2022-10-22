package com.gats.animation;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Verhält sich wie ein Entity mit dem Unterschied, dass beim draw() Aufruf
 * eine Textur mit der absoluten Position des Entity gerendert wird
 */
public class SpriteEntity extends Entity{

    private TextureRegion textureRegion;

    @Override
    void draw(Batch batch) {
        super.draw(batch);
        batch.draw(textureRegion, getPos().x, getPos().y);
    }
}
