package com.gats.simulation.weapons;

import com.badlogic.gdx.math.Vector2;
import com.gats.simulation.Action;
import com.gats.simulation.ActionLog;

public class Bounceable extends Weapon implements Projectile {

    int bounces;
    int leftBounces;
    Vector2 dir;

    Bounceable(Weapon weapon, int bounces) {
        super(null, 0, 0, 0); // ToDo: fill constructor...what do I need?
        this.bounces = bounces;
        this.leftBounces = bounces;
    }

    @Override
    public ActionLog hitCharacter(Action head) {
        return super.hitCharacter(head);
    }

    @Override
    public ActionLog hitWall(Action head) {
        if (leftBounces == 0) {
            return super.hitWall(head);
        } else {
            // Do bouncy Stuff
        }
        return null;
    }

    @Override
    ActionLog move() {
        return null;
    }

    @Override
    ActionLog shoot(Action head, Vector2 v, int t) {
        return super.shoot(head, v, t);
    }


}
