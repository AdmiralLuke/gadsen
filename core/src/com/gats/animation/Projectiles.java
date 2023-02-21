package com.gats.animation;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.gats.animation.entity.AnimatedEntity;
import com.gats.animation.entity.Entity;
import com.gats.animation.entity.SpriteEntity;
import com.gats.simulation.ProjectileAction;
import com.gats.ui.assets.AssetContainer;

public class Projectiles {


    protected static Entity summon(ProjectileAction.ProjectileType type){
        Array<TextureAtlas.AtlasRegion> texture;
        //configuring of drawing properties [0]:AnimatedEntity.rotate [1]:AnimatedEntity.mirror
        boolean[] settings;
        switch (type){
            case COOKIE:
                texture = AssetContainer.IngameAssets.Cookie;
                settings = new boolean[]{/*rotate*/false,/*mirror*/false};
                break;
            case CANDY_CANE:
                texture = AssetContainer.IngameAssets.SugarCane;
                //flip the sprite so it also looks to the left
                //ToDo declare a standard direction for Sprites to look in
                //preferrably right?
                for (TextureRegion sprite:texture) {
                    sprite.flip(true,false);

                }
                settings = new boolean[]{/*rotate*/true,/*mirror*/true};
                break;
            default:
                throw new RuntimeException("Type " + type + " is not Supported!");

        }
        Animation<TextureRegion> animation = new Animation<>(1/8f, texture);
        animation.setPlayMode(Animation.PlayMode.LOOP);
        return new AnimatedEntity(animation, new Vector2(texture.get(0).getRegionWidth(), texture.get(0).getRegionHeight()),settings[0],settings[1]);
    }
}
