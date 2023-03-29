package com.gats.simulation.weapons;

import com.badlogic.gdx.math.Vector2;
import com.gats.simulation.GameCharacter;
import com.gats.simulation.Path;
import com.gats.simulation.action.Action;
import com.gats.simulation.Tile;

enum ProjType {
    PARABLE,
    LINEAR,
    LASER,
    EXPLOSIVE
}

/**
 * Projectile Decorator interface, providing all needed Methodes for the chaining
 */
public interface Projectile {

    /**
     * is called, when a Projectile collides with a tile
     * @param head head-Action for chaining
     * @param t tile which is hitted (or collided)
     * @param dec decorator Projectile for back-tracking chaining
     * @param bsProj base projectile with all important attributes and information
     * @return chained Action for Animator
     */
    Action hitWall(Action head, Tile t, Projectile dec, BaseProjectile bsProj);


    /**
     * is called, when a Projectile collides with a character
     * @param head head-Action for chaining
     * @param character character which is hitted (or collided)
     * @param dec decorator Projectile for back-tracking chaining
     * @param bsProj base projectile with all important attributes and information
     * @return chained Action for Animator
     */
    Action hitCharacter(Action head, GameCharacter character, Projectile dec, BaseProjectile bsProj);

    /**
     * initial shoot command to init some variables
     * @param head head-Action for chaining
     * @param dir direction of the projectile
     * @param strength strength of the projectile between 0 and 1
     * @return chained Action for Animator
     */
    Action shoot(Action head, Vector2 dir, float strength, Projectile dec);

    /**
     * initial path-chaining
     * @param path Path for the Projectile
     */
    void setPath(Path path);

    /**
     * defines the movement of a Projectile (mostly the {@link BaseProjectile BaseProjectile} will do that)
     * @param head head-action for chaining
     * @param strength strength of the proj between 0 and 1
     * @param dec decorator projectile for back-tracking chaining
     * @return chained action for Animator
     */
    Action move(Action head, float strength, Projectile dec);
}
