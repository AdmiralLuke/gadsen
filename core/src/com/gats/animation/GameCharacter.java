package com.gats.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.gats.animation.entity.*;
import com.gats.ui.assets.AssetContainer.IngameAssets;
import com.gats.ui.assets.AssetContainer.IngameAssets.GameCharacterAnimationType;

import java.util.Random;

/**
 * Repräsentiert eine Spielfigur auf der Karte
 */
public class GameCharacter extends AnimatedEntity implements Parent {


    private float accSkinTime = 0;
    private final static Vector2 HOLSTER_OFFSET = new Vector2(1, 4);

    private static final float spriteOffsetLeft = 5;

    private static final float spriteOffsetRight = 2;

    private Animation<TextureRegion> skin;


    private AimIndicator aimingIndicator;

    private Weapon weapon;

    private boolean holdingWeapon = false;

    private GameCharacterAnimationType currentAnimation = GameCharacterAnimationType.ANIMATION_TYPE_IDLE;
    private final Color teamColor;

    private static final float OUTLINE_ALPHA = 0.5f;


    public GameCharacter(Color teamColor) {
        super(IngameAssets.gameCharacterAnimations[GameCharacterAnimationType.ANIMATION_TYPE_IDLE.ordinal()]);
        switch (new Random().nextInt(4)) {
            case 1:
                skin = IngameAssets.orangeCatSkin;
                break;
            case 2:
                skin = IngameAssets.yinYangSkin;
                break;
            case 3:
                skin = IngameAssets.mioSkin;
                break;
            default:
                skin = IngameAssets.coolCatSkin;
        }
        setMirror(true);
        TextureRegion texture = IngameAssets.gameCharacterAnimations[0].getKeyFrame(0);
        setOrigin(com.gats.simulation.GameCharacter.getSize().scl(0.5f).add(5,0));
        this.teamColor = new Color(teamColor.r, teamColor.g, teamColor.b, OUTLINE_ALPHA);
    }

    @Override
    public void draw(Batch batch, float deltaTime, float parentAlpha) {
        accSkinTime += deltaTime;

        if (aimingIndicator != null) aimingIndicator.draw(batch, deltaTime, parentAlpha);
        if (!holdingWeapon && weapon != null) {
            weapon.draw(batch, deltaTime, parentAlpha);
        }
        batch.flush();

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
        shader.setUniformf("flipped", isFlipped() ? 1 : 0);
        shader.setUniformf("v_skinBounds",
                skinFrame.getU(),
                skinFrame.getV(),
                skinFrame.getU2() - skinFrame.getU(),
                skinFrame.getV2() - skinFrame.getV());
        Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
        super.draw(batch, deltaTime, parentAlpha);
        batch.flush();
        batch.setShader(null);

        if (holdingWeapon && weapon != null) weapon.draw(batch, deltaTime, parentAlpha);

    }

    public void setAnimation(GameCharacterAnimationType type) {
        if (type == currentAnimation) return;
        currentAnimation = type;
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


    public AimIndicator getAimingIndicator() {
        return this.aimingIndicator;
    }

    public void setAimingIndicator(AimIndicator aimIndicator) {
        if (this.aimingIndicator != null && this.aimingIndicator.getParent() != null) remove(aimIndicator);
        this.aimingIndicator = aimIndicator;
        if (aimIndicator == null) return;
        if (aimIndicator.getParent() != null) aimIndicator.getParent().remove(aimIndicator);
        aimIndicator.setParent(this);
    }

    @Override
    protected void setPos(Vector2 pos) {
        super.setPos(pos);
        updatePos();
    }

    @Override
    public void setRelPos(Vector2 pos) {
        super.setRelPos(pos);
    }

    @Override
    public void updatePos() {
        super.updatePos();
        if (aimingIndicator != null) aimingIndicator.updatePos();
        if (weapon != null) weapon.updatePos();
    }

    public static float getAnimationDuration(GameCharacterAnimationType type) {
        return IngameAssets.gameCharacterAnimations[type.ordinal()].getAnimationDuration();
    }

    public boolean isHoldingWeapon() {
        return holdingWeapon;
    }

    public void setHoldingWeapon(boolean holdingWeapon) {
        this.holdingWeapon = holdingWeapon;
        if (weapon == null) return;
        weapon.setHolding(holdingWeapon);
    }

    public void setWeapon(Weapon weapon) {
        if (this.weapon != null && this.weapon.getParent() != null) {
            remove(weapon);
        }
        this.weapon = weapon;
        if (weapon == null) return;
        if (weapon.getParent() != null) weapon.getParent().remove(weapon);
        weapon.setParent(this);
        weapon.setHolding(holdingWeapon);
    }

    @Override
    public Entity asEntity() {
        return this;
    }

    @Override
    public void add(Entity child) {
        if (child instanceof AimIndicator) {
            setAimingIndicator((AimIndicator) child);
        } else if (child instanceof Weapon) {
            setWeapon((Weapon) child);
        }
    }

    @Override
    public void remove(Entity child) {
        if (child == weapon) {
            weapon.setParent(null);
            weapon = null;
        } else if (child == aimingIndicator) {
            aimingIndicator.setParent(null);
            aimingIndicator = null;
        }
    }
}
