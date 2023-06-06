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
public class GameCharacter extends AnimatedEntity {


    private float accSkinTime = 0;
    private final static Vector2 HOLSTER_OFFSET = new Vector2(1, 4);

    private static final float spriteOffsetLeft = 5;

    private static final float spriteOffsetRight = 2;


    private Animation<TextureRegion> skin;


    private AimIndicator aimingIndicator;

    private Weapon weapon;
    private Healthbar healthbar;

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
        setRotate(true);
        TextureRegion texture = IngameAssets.gameCharacterAnimations[0].getKeyFrame(0);
        setOrigin(com.gats.simulation.GameCharacter.getSize().scl(0.5f).add(5, 0));

        this.teamColor = new Color(teamColor.r, teamColor.g, teamColor.b, OUTLINE_ALPHA);

        setShaderHandler((batch) -> {
            ShaderProgram shader = IngameAssets.lookupOutlineShader;
            batch.setShader(shader);
            shader.setUniformf("outline_color", this.teamColor);
            shader.setUniformf("line_thickness", 1f);
            Texture frame = getAnimation().getKeyFrame(0).getTexture();
            shader.setUniformf("tex_size", new Vector2(frame.getWidth(), frame.getHeight()));
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
        });
    }

    @Override
    public void draw(Batch batch, float deltaTime, float parentAlpha) {
        accSkinTime += deltaTime;
        if (!holdingWeapon && weapon != null) {
            weapon.draw(batch, deltaTime, parentAlpha);
            weapon.setVisible(false);
        }
        super.draw(batch, deltaTime, parentAlpha);
        if (!holdingWeapon && weapon != null) {
            weapon.setVisible(true);
        }
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


    public Healthbar getHealthbar() {
        return this.healthbar;
    }

    public void setAimingIndicator(AimIndicator aimIndicator) {
        if (this.aimingIndicator != null) remove(aimIndicator);
        this.aimingIndicator = aimIndicator;
        if (aimIndicator == null) return;
        if (aimIndicator.getParent() != null) aimIndicator.getParent().remove(aimIndicator);
        aimIndicator.setParent(this);
        super.add(aimIndicator);


        //Farbe/Transparenz des Indicators setzen
        Color c;
        if (this.teamColor != null) {
            c = new Color(teamColor);

        } else {
            c = new Color(Color.WHITE);
        }
        c.a = 0.75f;
        aimIndicator.setColor(c);
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

    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
        if (this.weapon != null) {
            remove(this.weapon);
        }
        this.weapon = weapon;
        if (weapon == null) return;
        if (weapon.getParent() != null) weapon.getParent().remove(weapon);
        weapon.setParent(this);
        weapon.setHolding(holdingWeapon);
        super.add(weapon);
    }


    public void setHealthbar(Healthbar healthbar) {
        if (this.healthbar != null) {
            remove(this.healthbar);
        }
        this.healthbar = healthbar;
        if (healthbar == null) {
            return;
        }
        if (healthbar.getParent() != null) healthbar.getParent().remove(healthbar);
        this.healthbar.setParent(this);
        super.add(healthbar);
    }

    @Override
    public void updateAngle() {
        super.updateAngle();
        if (weapon != null) {
            if (isFlipped()) weapon.setRelAngle(-weapon.getRelAngle());
        }
    }

    @Override
    public void add(Entity child) {
        if (child instanceof AimIndicator) {
            setAimingIndicator((AimIndicator) child);
        } else if (child instanceof Weapon) {
            setWeapon((Weapon) child);
        } else if (child instanceof Healthbar) {
            setHealthbar((Healthbar) child);
        } else {
            super.add(child);
        }
    }

    @Override
    public void remove(Entity child) {
        if (child == aimingIndicator) {
            aimingIndicator = null;
        } else if (child == healthbar) {
            healthbar = null;
        } else if (child == weapon) {
            weapon = null;
        }
        super.remove(child);
    }

    public Color getTeamColor() {
        return new Color(teamColor);
    }
}
