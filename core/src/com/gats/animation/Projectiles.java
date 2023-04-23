package com.gats.animation;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.gats.animation.entity.AnimatedEntity;
import com.gats.animation.entity.Entity;
import com.gats.animation.entity.EntityGroup;
import com.gats.animation.entity.ParticleEntity;
import com.gats.simulation.action.ProjectileAction;
import com.gats.ui.assets.AssetContainer;
import com.gats.ui.assets.AtlasAnimation;
import org.lwjgl.Sys;

public class Projectiles {


    protected static Entity summon(ProjectileAction.ProjectileType type){
        Entity projectile;
        //configuring of drawing properties [0]:AnimatedEntity.rotate [1]:AnimatedEntity.mirror
        boolean[] settings;
        Animation<TextureRegion> animation;
        AnimatedEntity animatedEntity;
        switch (type){
            case GRENADE:
            case WOOL:
                animation = AssetContainer.IngameAssets.projectiles.get(type);
                animatedEntity = new AnimatedEntity(animation);
                animatedEntity.setOrigin(new Vector2(animation.getKeyFrame(0).getRegionWidth()/2f, animation.getKeyFrame(0).getRegionHeight()/2f));
                projectile = animatedEntity;
                break;
            case WATER:
                animation = AssetContainer.IngameAssets.projectiles.get(type);
                animatedEntity = new AnimatedEntity(animation);
                animatedEntity.setOrigin(new Vector2(animation.getKeyFrame(0).getRegionWidth()/2f, animation.getKeyFrame(0).getRegionHeight()/2f));
                ParticleEntity particleEntity = ParticleEntity.getParticleEntity(AssetContainer.IngameAssets.waterParticle);
                particleEntity.setLoop(true);
                projectile = new EntityGroup(animatedEntity, particleEntity);
                break;
            default:
                animation = AssetContainer.IngameAssets.coolCat;
                animatedEntity = new AnimatedEntity(animation);
                animatedEntity.setRotate(true);
                animatedEntity.setMirror(false);
                projectile = animatedEntity;
                System.err.println("Warning: Projectile-Type " + type + " is not Supported!");

        }
        return projectile;
    }
}
