package com.gats.animation;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.gats.animation.entity.AnimatedEntity;
import com.gats.animation.entity.Entity;
import com.gats.simulation.WeaponType;
import com.gats.ui.assets.AssetContainer;

public class Weapons {


    protected static Entity summon(WeaponType type){
        Animation<TextureRegion> animation;
        Vector2 size;
        Vector2 offset;
        float angle = 0;
        Vector2 scale = new Vector2(1, 1);
        //configuring of drawing properties [0]:AnimatedEntity.rotate [1]:AnimatedEntity.mirror
        boolean[] settings;
        switch (type){
            default:
                offset = new Vector2(-2, -2);
                settings = new boolean[]{/*rotate*/true,/*mirror*/false};
                animation = AssetContainer.IngameAssets.coolCat;
                TextureRegion firstFrame = animation.getKeyFrame(0);
                size = new Vector2(firstFrame.getRegionWidth(), firstFrame.getRegionHeight()).scl(0.25f);
                angle=-90;
                System.err.println("Warning: Weapon-Type " + type + " is not Supported!");
        }

        AnimatedEntity weapon = new AnimatedEntity(animation, size, settings[0],settings[1]);
        weapon.setRelPos(offset);
        weapon.setScale(scale);
        weapon.setRotationAngle(angle);
        return weapon;
    }
}
