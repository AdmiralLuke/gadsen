package com.gats.animation;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.StringBuilder;
import com.gats.animation.entity.AnimatedEntity;
import com.gats.ui.assets.AssetContainer;
import com.gats.animation.entity.GameCharacterHudElement;

import java.io.IOException;
import java.io.InputStream;

/**
 * Repräsentiert eine Spielfigur auf der Karte
 */
public class GameCharacter extends AnimatedEntity {


    private boolean aimActive = false;


    private AimIndicator aimingIndicator;

    private AssetContainer.IngameAssets.GameCharacterAnimationType idleType = AssetContainer.IngameAssets.GameCharacterAnimationType.ANIMATION_TYPE_IDLE;

    private AssetContainer.IngameAssets.GameCharacterAnimationType currentAnimation = AssetContainer.IngameAssets.GameCharacterAnimationType.ANIMATION_TYPE_IDLE;
    private Color teamColor;


    public GameCharacter(Color teamColor) {
        super(AssetContainer.IngameAssets.gameCharacterAnimations[AssetContainer.IngameAssets.GameCharacterAnimationType.ANIMATION_TYPE_IDLE.ordinal()], new Vector2(16, 16));
        this.teamColor = teamColor;
    }

    @Override
    public void draw(Batch batch, float deltaTime, float parentAlpha) {

        batch.flush();

        //draw the aimIndicator before the character, so it is overlapped by it
        if (aimingIndicator != null && aimActive) {
            aimingIndicator.draw(batch, deltaTime, parentAlpha);
        }
        batch.setShader(AssetContainer.IngameAssets.outlineShader);
        AssetContainer.IngameAssets.outlineShader.setUniformf("outline_color", teamColor);
        AssetContainer.IngameAssets.outlineShader.setUniformf("line_thickness", 1f);
        Texture texture = getAnimation().getKeyFrame(0).getTexture();
        AssetContainer.IngameAssets.outlineShader.setUniformf("tex_size", new Vector2(texture.getWidth(), texture.getHeight()));
        super.draw(batch, deltaTime, parentAlpha);
        batch.flush();
        batch.setShader(null);

    }

    public void setAnimation(AssetContainer.IngameAssets.GameCharacterAnimationType type) {
        currentAnimation = type;
        if (type == AssetContainer.IngameAssets.GameCharacterAnimationType.ANIMATION_TYPE_IDLE)
            super.setAnimation(AssetContainer.IngameAssets.gameCharacterAnimations[idleType.ordinal()]);
        else
            super.setAnimation(AssetContainer.IngameAssets.gameCharacterAnimations[type.ordinal()]);
    }

    public Animation<TextureRegion> getAnimation() {
        return super.getAnimation();
    }

    public AssetContainer.IngameAssets.GameCharacterAnimationType getIdleType() {
        return idleType;
    }

    public void setIdleType(AssetContainer.IngameAssets.GameCharacterAnimationType idleType) {
        this.idleType = idleType;
        if (currentAnimation == AssetContainer.IngameAssets.GameCharacterAnimationType.ANIMATION_TYPE_IDLE)
            setAnimation(AssetContainer.IngameAssets.GameCharacterAnimationType.ANIMATION_TYPE_IDLE);
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

    public static float getAnimationDuration(AssetContainer.IngameAssets.GameCharacterAnimationType type){
        return AssetContainer.IngameAssets.gameCharacterAnimations[type.ordinal()].getAnimationDuration();
    }

    public void aimActive(boolean active) {
        aimActive = active;
    }
}
