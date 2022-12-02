package com.gats.simulation;

import com.badlogic.gdx.math.Vector2;

/**
 * Oberklasse für alle {@link Action Ereignise}, die sich auf ein {@link Projectile Projektil} beziehen
 */
abstract class ProjectileAction extends Action{

    enum ProjectileType {
        COOKIE,
        CANDY_CANE
    }

    private ProjectileType type;

    public ProjectileAction(long delay, ProjectileType type) {
        super(delay);
        this.type = type;
    }
}
