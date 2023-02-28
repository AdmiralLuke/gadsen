package com.gats.simulation.weapons;

import com.badlogic.gdx.math.Vector2;
import com.gats.simulation.Action;
import com.gats.simulation.ActionLog;

public class Weapon implements Projectile{

    Projectile projectile;
    ProjType projType;
    int damage;
    int recoil;
    int knockback;

    Vector2 startPos;
    Vector2 pos;
    Vector2 dir;
    Weapon(Vector2 pos, int damage, int knockback) {
        this.startPos = pos;
        this.pos = pos;
        this.damage = damage;
        this.recoil = damage / 3;
        this.knockback = knockback;
    }

    Weapon(Vector2 pos, int damage, int knockback, int recoil) {
        this.startPos = pos;
        this.pos = pos;
        this.damage = damage;
        this.recoil = recoil;
        this.knockback = knockback;
    }

    ActionLog shoot(Action head, Vector2 v, int t) {
        this.move();
        ActionLog log = this.checkForHit();
        if (log.isEmpty()) {
            move();
        }
        return null;
    }

    ActionLog move() {
        if (projType == ProjType.PARABLE) {
            // ToDo: do parable stuff
            return null;
        } else if (projType == ProjType.LINEAR || projType == ProjType.LASER) {
            return null;
        } else {
            // Dieser Fall sollte nie eintreffen, wenn die Decorator funktioniere
            return null;
        }

    }

    ActionLog checkForHit() {
        return null;
    }
    @Override
    public ActionLog hitCharacter(Action head) {
        return null;
    }

    @Override
    public ActionLog hitWall(Action head) {
        return null;
    }
}
