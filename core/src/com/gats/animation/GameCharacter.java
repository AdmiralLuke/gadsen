package com.gats.animation;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.StringBuilder;
import com.gats.animation.entity.AnimatedEntity;
import com.gats.animation.entity.GameCharacterHudElement;

import java.io.IOException;
import java.io.InputStream;

/**
 * Repräsentiert eine Spielfigur auf der Karte
 */
public class GameCharacter extends AnimatedEntity {


    private boolean aimActive = false;


    public enum AnimationType {
        ANIMATION_TYPE_IDLE,
        ANIMATION_TYPE_WALKING,
        ANIMATION_TYPE_FALLING,
        ANIMATION_TYPE_COOKIE,
        ANIMATION_TYPE_SUGAR_CANE,
        ANIMATION_TYPE_HIT,
        ANIMATION_TYPE_DEATH
    }

    private static Animation<TextureRegion>[] animations = new Animation[AnimationType.values().length];
    private AimIndicator aimingIndicator;

    private AnimationType idleType = AnimationType.ANIMATION_TYPE_IDLE;

    private AnimationType currentAnimation = AnimationType.ANIMATION_TYPE_IDLE;
    private Color teamColor;

    private static ShaderProgram outlineShader;

    //ToDo move all asset loading and prep to Seperate class
    public static void loadAssets(TextureAtlas atlas) {
        StringBuilder builder = new StringBuilder();
        try (InputStream stream = GameCharacter.class.getClassLoader().getResourceAsStream("shader/vertex.glsl")) {
            int c = 0;
            if (stream == null) throw new RuntimeException("Could not read outline vertex shader");
            while ((c = stream.read()) != -1) {
                builder.append((char) c);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String vertexShader = builder.toString();

        builder = new StringBuilder();
        try (InputStream stream = GameCharacter.class.getClassLoader().getResourceAsStream("shader/fragment.glsl")) {
            int c = 0;
            if (stream == null) throw new RuntimeException("Could not read outline fragment shader");
            while ((c = stream.read()) != -1) {
                builder.append((char) c);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String fragmentShader = builder.toString();
        outlineShader = new ShaderProgram(vertexShader, fragmentShader);


        animations[AnimationType.ANIMATION_TYPE_IDLE.ordinal()] = new Animation<>(1 / 10f, atlas.findRegions("tile/idleShort"));
        animations[AnimationType.ANIMATION_TYPE_WALKING.ordinal()] = new Animation<>(1 / 10f, atlas.findRegions("tile/characterOrangeLeftWalking"));
        animations[AnimationType.ANIMATION_TYPE_FALLING.ordinal()] = new Animation<>(1 / 10f, atlas.findRegions("tile/fallShort"));
        animations[AnimationType.ANIMATION_TYPE_COOKIE.ordinal()] = new Animation<>(1 / 10f, atlas.findRegions("tile/idleShortCookie"));
        animations[AnimationType.ANIMATION_TYPE_SUGAR_CANE.ordinal()] = new Animation<>(1 / 10f, atlas.findRegions("tile/idleShortSugarCaneInHand"));
        animations[AnimationType.ANIMATION_TYPE_HIT.ordinal()] = new Animation<>(1 / 10f, atlas.findRegions("tile/hitAnimationRed"));
        animations[AnimationType.ANIMATION_TYPE_DEATH.ordinal()] = new Animation<>(1 / 10f, atlas.findRegions("tile/characterOrangeLeftDeath"));
        for (Animation<TextureRegion> anim : animations
        ) {
            anim.setPlayMode(Animation.PlayMode.LOOP);
        }
    }

    public GameCharacter(Color teamColor) {
        super(animations[AnimationType.ANIMATION_TYPE_IDLE.ordinal()], new Vector2(16, 16));
        this.teamColor = teamColor;
    }

    @Override
    public void draw(Batch batch, float deltaTime, float parentAlpha) {

        batch.flush();

        //draw the aimIndicator before the character, so it is overlapped by it
        if (aimingIndicator != null && aimActive) {
            aimingIndicator.draw(batch, deltaTime, parentAlpha);
        }
        batch.setShader(outlineShader);
        outlineShader.setUniformf("outline_color", teamColor);
        outlineShader.setUniformf("line_thickness", 1f);
        Texture texture = getAnimation().getKeyFrame(0).getTexture();
        outlineShader.setUniformf("tex_size", new Vector2(texture.getWidth(), texture.getHeight()));
        super.draw(batch, deltaTime, parentAlpha);
        batch.flush();
        batch.setShader(null);

    }

    public void setAnimation(AnimationType type) {
        currentAnimation = type;
        if (type == AnimationType.ANIMATION_TYPE_IDLE)
            super.setAnimation(animations[idleType.ordinal()]);
        else
            super.setAnimation(animations[type.ordinal()]);
    }

    public Animation<TextureRegion> getAnimation() {
        return super.getAnimation();
    }

    public AnimationType getIdleType() {
        return idleType;
    }

    public void setIdleType(AnimationType idleType) {
        this.idleType = idleType;
        if (currentAnimation == AnimationType.ANIMATION_TYPE_IDLE)
            setAnimation(AnimationType.ANIMATION_TYPE_IDLE);
    }

    public AimIndicator getAimingIndicator() {
        return this.aimingIndicator;
    }

    public void setAimingIndicator(AimIndicator aimIndicator) {
        this.aimingIndicator = aimIndicator;
    }

    @Override
    public void setRelPos(Vector2 pos) {
        setFlipped(this.getPos().x < pos.x);
        super.setRelPos(pos);
    }

    public static float getAnimationDuration(AnimationType type){
        return animations[type.ordinal()].getAnimationDuration();
    }

    public void aimActive(boolean active) {
        aimActive = active;
    }
}
