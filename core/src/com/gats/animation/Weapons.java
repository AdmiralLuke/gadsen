package com.gats.animation;

import com.badlogic.gdx.math.Vector2;
import com.gats.animation.entity.AnimatedEntity;
import com.gats.simulation.WeaponType;
import com.gats.ui.assets.AssetContainer;

public class Weapons {


    protected static Weapon summon(WeaponType type){
        Weapon weapon;
        Vector2 offset;
        float angle = 0;
        Vector2 scale = new Vector2(1, 1);
        //configuring of drawing properties [0]:AnimatedEntity.rotate [1]:AnimatedEntity.mirror
        boolean[] settings;
        switch (type){
            case WATER_PISTOL:
                //ToDo: Rendering Weapon on back needs to happen before Outline
                AnimatedEntity carryEntity = new AnimatedEntity(AssetContainer.IngameAssets.EMPTY_ANIMATION);
                carryEntity.setRelRotationAngle(90);
                carryEntity.setScale(new Vector2(1, -1));
                carryEntity.setRelPos(new Vector2(-6, 0));
                weapon = new Weapon(AssetContainer.IngameAssets.WaterPistol, carryEntity);
                weapon.setRelPos(new Vector2(-2.5f, -6.5f));
                break;
            default:
                weapon = new Weapon(AssetContainer.IngameAssets.coolCat);
                weapon.setRelPos(new Vector2(-2, -2));
                weapon.setScale(new Vector2(0.25f, 0.25f));
                weapon.setRelRotationAngle(-90);
                System.err.println("Warning: Weapon-Type " + type + " is not Supported!");
        }

        return weapon;
    }
}
