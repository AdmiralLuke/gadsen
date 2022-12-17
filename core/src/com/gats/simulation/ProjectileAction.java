package com.gats.simulation;

import org.lwjgl.Sys;

/**
 * {@link Action Ereignis}, das den Weg während der Lebensdauer eines {@link Projectile Projektils} beschreibt
 */
public class ProjectileAction extends Action{

    public enum ProjectileType {
        COOKIE,
        CANDY_CANE
    }

    private ProjectileType type;

    private Path path;

    private static double v = 0.0001;
    private float duration;


    public ProjectileAction(Path path, ProjectileType type, float duration) {
        super(0);
        System.out.println("Creating Projectile Action");
        this.type = type;
        this.path = path;
        this.duration = duration / 100;

        System.out.println("Shooting " + (path.getPos(0.001).cpy().sub(path.getPos(0))));
        System.out.println("Shooting " + (path.getPos(1).cpy().sub(path.getPos(0))));
    }


    public Path getPath() {
        return path;
    }

    public float getDuration() {
        return duration;
    }

    public ProjectileType getType() {
        return type;
    }
}
