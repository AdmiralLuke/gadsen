package com.gats.simulation.weapons;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.gats.simulation.action.Action;
import com.gats.simulation.*;
import com.gats.simulation.action.ProjectileAction;

import java.util.ArrayList;

public class BaseProjectile implements Projectile{

    ProjType projType;
    int damage;
    int recoil;
    int knockback;

    Simulation sim;

    Vector2 startPos;

    Vector2 v;
    int range;
    Path path;

    float strength;

    ProjectileAction.ProjectileType type;
    float t = 0f;

    private final static float g = 9.81f * 8;

    BaseProjectile(Path path, int damage, int knockback, Simulation sim, ProjectileAction.ProjectileType type) {
        this.path = path;
        this.startPos = path.getPos(0);
        this.damage = damage;
        this.recoil = damage / 3;
        this.knockback = knockback;
        this.sim = sim;
        this.type = type;
        this.projType = type == ProjectileAction.ProjectileType.COOKIE ? ProjType.PARABLE : ProjType.LINEAR;
    }

    public BaseProjectile(int damage, int knockback, int recoil, Simulation sim, ProjectileAction.ProjectileType type) {

        this.damage = damage;
        this.recoil = recoil;
        this.knockback = knockback;
        this.sim = sim;
        this.type = type;
        this.projType = type == ProjectileAction.ProjectileType.COOKIE ? ProjType.PARABLE : ProjType.LINEAR;
        this.range = 900; // ToDo: check4weapon
    }


    @Override
    public Action shoot(Action head, Vector2 dir, float strength, Projectile dec) {
        this.strength = strength;
        return this.move(head, strength , dec);
    }

    @Override
    public Action move(Action head, float strength, Projectile dec) {
        Action log = null;
        while (log == null) {
            this.t += 0.1f;
            Vector2 pos = path.getPos(t);
            Vector2 posN = path.getPos(this.t + 0.1f);
            log = checkForHit(head, dec, pos, posN);
        }
        return log;

    }

    Action checkForHit(Action head, Projectile dec, Vector2 pos, Vector2 posN) {
        // Mittelpunkt des Kreises (pos + posN) / 2
        Vector2 m = pos.cpy().add(posN).cpy().scl(0.5f);

        // Radius
        float r = posN.cpy().sub(pos).len() / 2;
        Circle c = new Circle(m.x, m.y, r);

        Tile h = sim.getState().getTile((int)posN.x / 16, (int)posN.y / 16);
        if (h != null) {
            Tile tN = sim.getState().getTile((int)pos.x / 16, (int)pos.y / 16);
            while (tN == null || !tN.equals(h)) {
                this.t += 0.001f;
                pos = path.getPos(t);
                tN = sim.getState().getTile((int)pos.x / 16, (int)pos.y / 16);
            }
            return dec.hitWall(head, tN, dec, this);
        }

        if (t == path.getDuration()) return hitNothing(head);
        // ToDo: Character Hit


        return null;
    }


    @Override
    public Action hitCharacter(Action head, Character character, Projectile dec, BaseProjectile bsProj) {
        return null;
    }

    @Override
    public Action hitWall(Action head, Tile t, Projectile dec, BaseProjectile bsProj) {
        ProjectileAction projAction = generateAction();
        head.getChildren().add(projAction);
        return t.onDestroy(projAction); // ToDo: something is wrong
    }

    public Action hitNothing(Action head) {
        ProjectileAction prAc = generateAction();
        this.path.setDuration(t);
        head.addChild(prAc);
        return prAc;
    }

    @Override
    public void setPath(Path path) {
        this.path = path;
    }

    ProjectileAction generateAction() {
        return new ProjectileAction(0,type, path);
    }

}
