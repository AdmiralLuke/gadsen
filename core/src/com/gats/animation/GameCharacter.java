package com.gats.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.StringBuilder;
import com.gats.animation.entity.AnimatedEntity;
import com.gats.ui.assets.AssetContainer;
import com.gats.animation.entity.GameCharacterHudElement;
import com.gats.ui.assets.AssetContainer.IngameAssets;
import com.gats.ui.assets.AssetContainer.IngameAssets.GameCharacterAnimationType;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

/**
 * Repräsentiert eine Spielfigur auf der Karte
 */
public class GameCharacter extends AnimatedEntity {


    private float accSkinTime = 0;
    private boolean aimActive = false;

    private Animation<TextureRegion> skin;

    private AimIndicator aimingIndicator;

    private GameCharacterAnimationType idleType = GameCharacterAnimationType.ANIMATION_TYPE_IDLE;

    private GameCharacterAnimationType currentAnimation = GameCharacterAnimationType.ANIMATION_TYPE_IDLE;
    private final Color teamColor;

    private static final float OUTLINE_ALPHA = 0.5f;


    public GameCharacter(Color teamColor) {
        super(IngameAssets.gameCharacterAnimations[GameCharacterAnimationType.ANIMATION_TYPE_IDLE.ordinal()], new Vector2(16, 16));
        switch (new Random().nextInt(3)){
            case 1:
                skin = IngameAssets.orangeCatSkin;
                break;
            case 2:
                skin = IngameAssets.yinYangSkin;
                break;
            default:
                skin = IngameAssets.coolCatSkin;
        }
        this.teamColor = new Color(teamColor.r, teamColor.g, teamColor.b, OUTLINE_ALPHA);
    }

    @Override
    public void draw(Batch batch, float deltaTime, float parentAlpha) {
        accSkinTime += deltaTime;

        batch.flush();

        //draw the aimIndicator before the character, so it is overlapped by it
        if (aimingIndicator != null && aimActive) {
            aimingIndicator.draw(batch, deltaTime, parentAlpha);
        }

        ShaderProgram shader = IngameAssets.lookupOutlineShader;
        batch.setShader(shader);
        shader.setUniformf("outline_color", teamColor);
        shader.setUniformf("line_thickness", 1f);
        Texture texture = getAnimation().getKeyFrame(0).getTexture();
        shader.setUniformf("tex_size", new Vector2(texture.getWidth(), texture.getHeight()));
        shader.setUniformi("u_skin", 1);
        Gdx.gl.glActiveTexture(GL20.GL_TEXTURE1);
        TextureRegion skinFrame = this.skin.getKeyFrame(accSkinTime);
        skinFrame.getTexture().bind();
        shader.setUniformf("flipped", isFlipped()? 1 : 0);
        shader.setUniformf("v_skinBounds",
                skinFrame.getU(),
                skinFrame.getV(),
                skinFrame.getU2() - skinFrame.getU(),
                skinFrame.getV2() - skinFrame.getV());
        Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
        super.draw(batch, deltaTime, parentAlpha);
        batch.flush();
        batch.setShader(null);

    }

    public void setAnimation(GameCharacterAnimationType type) {
        currentAnimation = type;
        if (type == GameCharacterAnimationType.ANIMATION_TYPE_IDLE)
            super.setAnimation(IngameAssets.gameCharacterAnimations[idleType.ordinal()]);
        else
            super.setAnimation(IngameAssets.gameCharacterAnimations[type.ordinal()]);
    }

    public Animation<TextureRegion> getAnimation() {
        return super.getAnimation();
    }

    public Animation<TextureRegion> getSkin() {
        return skin;
    }

    public void setSkin(Animation<TextureRegion> skin) {
        this.skin = skin;
    }

    public GameCharacterAnimationType getIdleType() {
        return idleType;
    }

    public void setIdleType(GameCharacterAnimationType idleType) {
        this.idleType = idleType;
        if (currentAnimation == GameCharacterAnimationType.ANIMATION_TYPE_IDLE)
            setAnimation(GameCharacterAnimationType.ANIMATION_TYPE_IDLE);
    }

    public AimIndicator getAimingIndicator() {
        return this.aimingIndicator;
    }

    public void setAimingIndicator(AimIndicator aimIndicator) {
        this.aimingIndicator = aimIndicator;
    }

    @Override
    public void setRelPos(Vector2 pos) {
        //setFlipped(this.getPos().x < pos.x);
        super.setRelPos(pos);
    }

    public static float getAnimationDuration(GameCharacterAnimationType type) {
        return IngameAssets.gameCharacterAnimations[type.ordinal()].getAnimationDuration();
    }

    public void aimActive(boolean active) {
        aimActive = active;
    }
}
