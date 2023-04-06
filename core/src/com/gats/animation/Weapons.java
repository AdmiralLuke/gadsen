package com.gats.animation;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.gats.simulation.WeaponType;
import com.gats.ui.assets.AssetContainer;

public class Weapons {


    protected static Weapon summon(WeaponType type){
        Animation<TextureRegion> holdAnimation;
        Animation<TextureRegion> carryAnimation = AssetContainer.IngameAssets.EMPTY_ANIMATION;
        Vector2 offset;
        float angle = 0;
        Vector2 scale = new Vector2(1, 1);
        //configuring of drawing properties [0]:AnimatedEntity.rotate [1]:AnimatedEntity.mirror
        boolean[] settings;
        switch (type){
            default:
                offset = new Vector2(-2, -2);
                holdAnimation = AssetContainer.IngameAssets.coolCat;
                TextureRegion firstFrame = holdAnimation.getKeyFrame(0);
                scale = new Vector2(0.25f, 0.25f);
                angle=-90;
                System.err.println("Warning: Weapon-Type " + type + " is not Supported!");
        }

        Weapon weapon = new Weapon(holdAnimation, carryAnimation);
        weapon.setRelPos(offset);
        weapon.setScale(scale);
        weapon.setRotationAngle(angle);
        return weapon;
    }
}
