package com.gats.animation;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.gats.animation.entity.AnimatedEntity;
import com.gats.animation.entity.Entity;
import com.gats.animation.entity.Parent;

public class Weapon extends AnimatedEntity implements Parent {

    private final Animation<TextureRegion> holdingAnimation;

    private Animation<TextureRegion> shootingAnimation;
    private AnimatedEntity carryEntity;

    private boolean shooting = false;
    private boolean holding = false;

    public Weapon(Animation<TextureRegion> holdingAnimation) {
        super(holdingAnimation);
        this.holdingAnimation = holdingAnimation;
    }

    public Weapon(Animation<TextureRegion> holdingAnimation, AnimatedEntity carryEntity) {
        super(holdingAnimation);
        this.holdingAnimation = holdingAnimation;
        add(carryEntity);
    }

    public Animation<TextureRegion> getShootingAnimation() {
        return shootingAnimation;
    }

    public void setShootingAnimation(Animation<TextureRegion> shootingAnimation) {
        this.shootingAnimation = shootingAnimation;
        if (shootingAnimation != null) shootingAnimation.setPlayMode(Animation.PlayMode.NORMAL);
    }

    public boolean isHolding() {
        return holding;
    }

    public void setHolding(boolean holding) {
        this.holding = holding;
    }

    public void shoot(){
        if (shootingAnimation != null){
            shooting = true;
            setAnimation(shootingAnimation);
            resetAccTime();
        }
    }

    @Override
    public void draw(Batch batch, float deltaTime, float parentAlpha) {
        if (holding) super.draw(batch, deltaTime, parentAlpha);
        else if (carryEntity != null) {
            carryEntity.draw(batch, deltaTime, parentAlpha);
        }
        if (shooting && shootingAnimation.isAnimationFinished(getAccTime())){
            shooting = false;
            setAnimation(holdingAnimation);
            resetAccTime();
        }
    }

    @Override
    public Entity asEntity() {
        return this;
    }

    @Override
    public void add(Entity child) {
        if (child instanceof AnimatedEntity || child == null) {
            if (this.carryEntity != null && this.carryEntity.getParent() != null) {
                remove(carryEntity);
            }
            this.carryEntity = (AnimatedEntity) child;
            if (carryEntity == null) return;
            if (carryEntity.getParent() != null) carryEntity.getParent().remove(carryEntity);
            carryEntity.setParent(this);
        }
    }

    @Override
    public void remove(Entity child) {
        if (child == null) return;
        if (carryEntity == child) {
            carryEntity.setParent(null);
            carryEntity = null;
        }
    }
    @Override
    protected void setPos(Vector2 pos) {
        super.setPos(pos);
        if (carryEntity != null) carryEntity.updatePos();
    }



}
