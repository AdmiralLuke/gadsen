package com.gats.simulation.weapons;

import com.badlogic.gdx.math.Vector2;
import com.gats.simulation.Path;
import com.gats.simulation.action.Action;
import com.gats.simulation.Tile;

enum ProjType {
    PARABLE,
    LINEAR,
    LASER,
    EXPLOSIVE
}

public interface Projectile {
    Action hitWall(Action head, Tile t, Projectile dec, BaseProjectile bsProj);


    Action hitCharacter(Action head,Character character, Projectile dec, BaseProjectile bsProj);

    /**
     *
     * @param head
     * @param dir
     * @param strength
     * @return
     */
    Action shoot(Action head, Vector2 dir, float strength, Projectile dec);

    void setPath(Path path);
    Action move(Action head, float strength, Projectile dec);
}
