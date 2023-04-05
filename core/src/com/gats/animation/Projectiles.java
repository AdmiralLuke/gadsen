package com.gats.animation;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.gats.animation.entity.AnimatedEntity;
import com.gats.animation.entity.Entity;
import com.gats.simulation.action.ProjectileAction;
import com.gats.ui.assets.AssetContainer;
import org.lwjgl.Sys;

public class Projectiles {


    protected static Entity summon(ProjectileAction.ProjectileType type){
        Animation<TextureRegion> animation;
        //configuring of drawing properties [0]:AnimatedEntity.rotate [1]:AnimatedEntity.mirror
        boolean[] settings;
        switch (type){
            case COOKIE:
                animation = AssetContainer.IngameAssets.Cookie;
                settings = new boolean[]{/*rotate*/false,/*mirror*/false};
                break;
            case CANDY_CANE:
                animation = AssetContainer.IngameAssets.SugarCane;
                //flip the sprite so it also looks to the left
                //ToDo: Move this code to loading
                //ToDo declare a standard direction for Sprites to look in
                //preferrably right?
//                for (TextureRegion sprite:texture) {
//                    sprite.flip(true,false);
//
//                }
                settings = new boolean[]{/*rotate*/true,/*mirror*/true};
                break;
            default:
                settings = new boolean[]{/*rotate*/true,/*mirror*/false};
                animation = AssetContainer.IngameAssets.coolCat;
                System.err.println("Warning: Projectile-Type " + type + " is not Supported!");

        }
        TextureRegion firstFrame = animation.getKeyFrame(0);
        return new AnimatedEntity(animation, new Vector2(firstFrame.getRegionWidth(), firstFrame.getRegionHeight()), settings[0],settings[1]);
    }
}
