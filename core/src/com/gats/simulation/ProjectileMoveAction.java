package com.gats.simulation;

import com.badlogic.gdx.math.Vector2;

public class ProjectileMoveAction extends ProjectileAction {
    private Path path;

    public ProjectileMoveAction(Path path, ProjectileType type) {
        super(0, type);
        this.path = path;
    }
}
