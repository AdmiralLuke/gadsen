package com.gats.simulation;

import com.badlogic.gdx.math.Vector2;

public class ProjectileMoveAction extends ProjectileAction {
    private Path path;
    private Projectile projectile;

    public ProjectileMoveAction(Path path, Projectile projectile) {
        this.path = path;
        this.projectile = projectile;
    }
}
