package com.gats.simulation.weapons;

import com.badlogic.gdx.math.Vector2;
import com.gats.simulation.GameCharacter;
import com.gats.simulation.Path;
import com.gats.simulation.Tile;
import com.gats.simulation.action.Action;

/**
 * Simulates the knockback, a player will get when hit by this {@link Projectile Projectile}
 * Part of the Projectile Decorator
 */
public class Backnockable implements Projectile {

    private Projectile proj;
    private float amount;

    public Backnockable(Projectile proj, float amount) {
        this.proj = proj;
        this.amount = amount;
    }

    @Override
    public Action hitWall(Action head, Tile t, Projectile dec, BaseProjectile bsProj) {
        return dec == this ? proj.hitWall(head,t, proj, bsProj) : proj.hitWall(head,t, dec, bsProj);
    }

    @Override
    public Action hitCharacter(Action head, GameCharacter character, Projectile dec, BaseProjectile bsProj) {
        return proj.hitCharacter(head, character, dec, bsProj);
    }

    @Override
    public Action shoot(Action head, Vector2 dir, float strength, Projectile dec) {
        return proj.shoot(head, dir, strength, dec);
    }

    @Override
    public void setPath(Path path) {
        proj.setPath(path);
    }

    @Override
    public Action move(Action head, float strength, Projectile dec) {
        return proj.move(head, strength, dec);
    }
}
