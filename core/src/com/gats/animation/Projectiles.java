package com.gats.animation;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.gats.animation.entity.AnimatedEntity;
import com.gats.animation.entity.Entity;
import com.gats.animation.entity.SpriteEntity;
import com.gats.simulation.ProjectileAction;

public class Projectiles {


    protected static TextureAtlas projectileAtlas;

    protected static Entity summon(ProjectileAction.ProjectileType type){
        switch (type){
            case COOKIE:
                return new AnimatedEntity(new Animation<>(1/8f, projectileAtlas.findRegions("tile/#161_cookieTumblingCropped")), new Vector2(1,1));
            case CANDY_CANE:
                return new AnimatedEntity(new Animation<>(1/8f, projectileAtlas.findRegions("tile/sugarcaneProjectileFront")), new Vector2(1,1));

        }
        throw new RuntimeException("Type " + type + " is not Supported!");
    }
}
