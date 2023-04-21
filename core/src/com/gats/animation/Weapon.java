package com.gats.animation;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.gats.animation.entity.AnimatedEntity;
import com.gats.animation.entity.Entity;
import com.gats.animation.entity.Parent;

public class Weapon extends AnimatedEntity implements Parent {

    private Animation<TextureRegion> holdingAnimation;
    private AnimatedEntity carryEntity;

    private boolean holding = false;

    public Weapon(Animation<TextureRegion> holdingAnimation) {
        super(holdingAnimation);
    }

    public Weapon(Animation<TextureRegion> holdingAnimation, AnimatedEntity carryEntity) {
        super(holdingAnimation);
        add(carryEntity);
    }

    public boolean isHolding() {
        return holding;
    }

    public void setHolding(boolean holding) {
        this.holding = holding;
    }

    @Override
    public void draw(Batch batch, float deltaTime, float parentAlpha) {
        if (holding) super.draw(batch, deltaTime, parentAlpha);
        else if (carryEntity != null) {
            carryEntity.draw(batch, deltaTime, parentAlpha);
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
        updatePos();
    }

    @Override
    public void setRelPos(Vector2 pos) {
        super.setRelPos(pos);
    }

    @Override
    public void updatePos() {
            super.updatePos();
        if (carryEntity != null) carryEntity.updatePos();
    }

}
