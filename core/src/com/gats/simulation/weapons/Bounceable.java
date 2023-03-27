package com.gats.simulation.weapons;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.gats.simulation.action.Action;
import com.gats.simulation.*;
import com.gats.simulation.action.DebugPointAction;
import com.gats.simulation.action.ParticleAction;
import com.gats.simulation.action.ProjectileAction;

public class Bounceable implements Projectile {

    int bounces;
    int leftBounces;
    Vector2 dir;

    private Projectile proj;

    public Bounceable(Projectile proj, int bounces) {
        this.proj = proj;
        this.leftBounces = bounces;
        this.bounces = bounces;
    }

    @Override
    public Action hitCharacter(Action head, Character character, Projectile dec, BaseProjectile bsProj) {
        return dec == this ? proj.hitCharacter(head,character, proj, bsProj) : proj.hitCharacter(head,character, dec, bsProj);
    }


    @Override
    public Action hitWall(Action head, Tile t, Projectile dec, BaseProjectile bsProj) {
        if (leftBounces <= 0) {
            leftBounces = bounces;
            return proj.hitWall(head, t, proj, bsProj);
        } else {
            return proj.hitWall(head, t, proj, bsProj);
        }
    }


    @Override
    public Action shoot(Action head, Vector2 dir, float strength, Projectile dec) {
        return this.proj.shoot(head, dir, strength, dec);
    }

    @Override
    public void setPath(Path path) {
        proj.setPath(path);
    }

    @Override
    public Action move(Action head, float strength, Projectile dec) {
        return this.proj.move(head, strength, dec);
    }
}
