package com.gats.simulation.action;

import com.badlogic.gdx.math.Vector2;
import com.gats.simulation.ParablePath;
import com.gats.simulation.Path;
import com.gats.simulation.Projectile;

/**
 * Type of {@link Action} that describes a {@link Projectile} during its life-time
 */
public class ProjectileAction extends Action{

    /**
     * The possible types of Projectiles-appearances
     */
    public enum ProjectileType {
        COOKIE,
        CANDY_CANE
    }

    private final ProjectileType type;

    private final Path path;
    private final float duration;

    /**
     * Stores the event of a certain {@link Projectile} being created, travelling along a certain path and finally being destroyed.
     *
     * @param delay     non-negative time-based offset to its parent in seconds
     * @param type      type of the projectiles appearance
     * @param path      A {@link Path} that returns the Projectiles position in world-coordinates for every timestamp between 0 and duration
     * @param duration  The duration of the event in seconds
     */
    public ProjectileAction(float delay, ProjectileType type, Path path, float duration, Vector2 posAft) {
        super(delay);
        this.type = type;
        this.path = path;
        //ToDo: remove redundant posAfter, duration should be determined by simulation and posAfter should equal path(duration)
        if (ProjectileType.COOKIE == type) {
            this.duration = -((path.getPos(0).x - posAft.x) / ((ParablePath)path).getStartVelocity().x);
        } else {
            this.duration = duration / 100;
        }
    }

    /**
     * @return A {@link Path} that returns the Projectiles position in world-coordinates for every timestamp between 0 and duration
     */
    public Path getPath() {
        return path;
    }

    /**
     * @return The duration of the event in seconds
     */
    public float getDuration() {
        return duration;
    }

    /**
     * @return type of the projectiles appearance
     */
    public ProjectileType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "ProjectileAction{" +
                       "type=" + type +
                       ", path=" + path +
                       ", duration=" + duration +
                       '}';
    }
}
