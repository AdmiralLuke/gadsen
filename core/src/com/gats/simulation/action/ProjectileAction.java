package com.gats.simulation.action;

import com.badlogic.gdx.math.Vector2;
import com.gats.simulation.ParablePath;
import com.gats.simulation.Path;
import com.gats.simulation.Projectile;

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


    public ProjectileAction(Path path, ProjectileType type, float duration, Vector2 posAft) {
        super(0);
        this.type = type;
        this.path = path;
        if (ProjectileType.COOKIE == type) {
            this.duration = -((path.getPos(0).x - posAft.x) / ((ParablePath)path).getV().x);;
        } else {
            this.duration = duration / 100;
        }
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
