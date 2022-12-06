package com.gats.animation;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.gats.animation.entity.Entity;
import com.gats.animation.entity.SpriteEntity;

public class Projectiles {

    protected static TextureAtlas projectileAtlas;

    protected static Entity summon(){//ToDo take weapon type enum
        return new SpriteEntity(projectileAtlas.findRegion("present"));
    }
}
