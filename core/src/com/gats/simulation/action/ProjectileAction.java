package com.gats.simulation.action;

import com.gats.simulation.Path;

/**
 * Type of {@link Action} that describes a {@link com.gats.simulation.weapons.Projectile} during its life-time
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


    /**
     * Stores the event of a certain {@link com.gats.simulation.weapons.Projectile} being created, travelling along a certain path and finally being destroyed.
     *
     * @param delay     non-negative time-based offset to its parent in seconds
     * @param type      type of the projectiles appearance
     * @param path      A {@link Path} that returns the Projectiles position in world-coordinates for every timestamp between 0 and duration
     */
    public ProjectileAction(float delay, ProjectileType type, Path path) {
        super(delay);
        this.type = type;
        this.path = path;
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
        return path.getDuration();
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
