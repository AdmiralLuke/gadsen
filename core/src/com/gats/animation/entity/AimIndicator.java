package com.gats.animation.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * Visual representation of the aiming Angle and Strength
 */
public class AimIndicator extends NinePatchEntity implements Toggleable {


    private boolean aimActive = false;
    private SpriteEntity aimCircle;

    private boolean drawCircle;

    private float circleDrawMinLength;
    private float lengthPercent;
    public AimIndicator(TextureRegion hudSprite) {
        super(new NinePatch(hudSprite,4,4,3,3),  new Vector2(64,7));
        circleDrawMinLength = getMaxSize().x/2;
        setOrigin(new Vector2(0, hudSprite.getRegionHeight() / 2f));
        float circleOpacity = 0.5f;
        createAimCircle(circleOpacity);

    }

    @Override
    public void draw(Batch batch, float deltaTime, float parentAlpha) {

        if (aimActive) {
            super.draw(batch, deltaTime, parentAlpha);
            if (drawCircle) {
                Color batchColor = new Color(batch.getColor());
                //adjust the  alpha value in regadrs to the distance(lengthrpercent)
                //changing the color is the easiest way to do so i think, without needing to change the batch too much
                batch.setColor(Color.WHITE.r, Color.WHITE.g, Color.WHITE.g, lengthPercent*parentAlpha);
                aimCircle.draw(batch,deltaTime,parentAlpha);
                batch.setColor(batchColor);
            }
        }

    }

    @Override
    public void toggle() {
        aimActive = !aimActive;
    }

    @Override
    public void setEnabled(boolean isEnabled) {
        aimActive = isEnabled;
    }

    private void createAimCircle(float circleOpacity) {
        TextureRegion circleTexture;
        //get max size of the aim indicator
        int circleSize = (int) maxSize.x;
        //create a Pixmap for drawing the circle texture
        Pixmap circle = new Pixmap(2 * circleSize + 1, 2 * circleSize + 1, Pixmap.Format.RGBA8888);
        //circle Color
        Color color = new Color(Color.WHITE);
        //circle alpha
        color.a = circleOpacity;
        circle.setColor(color);
        //draw circle at
        circle.drawCircle(circleSize, circleSize, circleSize);
        circleTexture = new TextureRegion(new Texture(circle));
        int circleTextureRegionHeight = circleTexture.getRegionHeight();
        aimCircle = new SpriteEntity(circleTexture,new Vector2(circleTextureRegionHeight/2f,circleTextureRegionHeight/2f),new Vector2(circleTextureRegionHeight,circleTextureRegionHeight));
        //aimCircle.setOrigin(new Vector2(circleSize,circleSize));
    }

    @Override
    public void updatePos() {
        super.updatePos();
        if(aimCircle!=null&&aimCircle.getSize()!=null) {
            aimCircle.setPos(new Vector2(getPos().x-aimCircle.getSize().x/2,getPos().y-aimCircle.getSize().x/2));
        }
    }

    @Override
    public void setScale(Vector2 scale) {
        super.setScale(scale);
        //determine, wether the size is bigger than the circle draw threshold
        Vector2 size = getSize();
        drawCircle= circleDrawMinLength < size.x;
        if(drawCircle) {
            lengthPercent =(getSize().x - circleDrawMinLength) / (maxSize.x - circleDrawMinLength);
        }
    }
}