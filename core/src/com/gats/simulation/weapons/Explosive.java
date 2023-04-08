package com.gats.simulation.weapons;

import com.badlogic.gdx.math.Vector2;
import com.gats.simulation.GameCharacter;
import com.gats.simulation.Path;
import com.gats.simulation.Tile;
import com.gats.simulation.action.Action;

public class Explosive implements Projectile {

    Projectile proj;
    int radius;

    public Explosive(Projectile proj, int radius) {
        this.proj = proj;
        this.radius = radius;
    }

    @Override
    public Action hitWall(Action head, Tile t, Projectile dec, BaseProjectile bsProj) {
        return proj.hitWall(head, t, dec, bsProj);
    }

    @Override
    public Action hitCharacter(Action head, GameCharacter character, Projectile dec, BaseProjectile bsProj) {
        return proj.hitCharacter(head, character, dec, bsProj);
    }

    @Override
    public Action hitNothing(Action head, Projectile dec, BaseProjectile bsProj) {
        return proj.hitNothing(head, dec, bsProj);
    }

    @Override
    public Action shoot(Action head, Vector2 dir, float strength, Projectile dec, GameCharacter character) {
        return proj.shoot(head, dir, strength, dec, character);
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
