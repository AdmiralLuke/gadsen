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
        Vector2 offset;
        //configuring of drawing properties [0]:AnimatedEntity.rotate [1]:AnimatedEntity.mirror
        boolean[] settings;
        switch (type){
            default:
                offset = new Vector2(0, 5);
                settings = new boolean[]{/*rotate*/true,/*mirror*/false};
                animation = AssetContainer.IngameAssets.coolCat;
                System.err.println("Warning: Weapon-Type " + type + " is not Supported!");
        }

        TextureRegion firstFrame = animation.getKeyFrame(0);
        AnimatedEntity weapon = new AnimatedEntity(animation, new Vector2(firstFrame.getRegionWidth(), firstFrame.getRegionHeight()), settings[0],settings[1]);
        weapon.setRelPos(offset);
        return weapon;
    }
}
