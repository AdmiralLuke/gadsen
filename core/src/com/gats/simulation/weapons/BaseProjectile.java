package com.gats.simulation.weapons;

import com.badlogic.gdx.math.Vector2;
import com.gats.simulation.action.Action;
import com.gats.simulation.*;
import com.gats.simulation.action.ProjectileAction;

public class BaseProjectile implements Projectile{
    ProjType projType;
    int damage;
    int recoil;
    int knockback;

    Simulation sim;

    Vector2 startPos;
    Vector2 pos;
    Vector2 dir;
    Vector2 v;

    float strength;

    ProjectileAction.ProjectileType type;

    private final static float g = 9.81f * 8;

    BaseProjectile(Vector2 pos, int damage, int knockback, Simulation sim, ProjectileAction.ProjectileType type) {
        this.startPos = pos.cpy();
        this.pos = pos.cpy();
        this.damage = damage;
        this.recoil = damage / 3;
        this.knockback = knockback;
        this.sim = sim;
        this.type = type;
        this.projType = type == ProjectileAction.ProjectileType.COOKIE ? ProjType.PARABLE : ProjType.LINEAR;
    }

    public BaseProjectile(Vector2 pos, int damage, int knockback, int recoil, Simulation sim, ProjectileAction.ProjectileType type) {
        this.startPos = pos.cpy();
        this.pos = pos.cpy();
        this.damage = damage;
        this.recoil = recoil;
        this.knockback = knockback;
        this.sim = sim;
        this.type = type;
        this.projType = type == ProjectileAction.ProjectileType.COOKIE.COOKIE ? ProjType.PARABLE : ProjType.LINEAR;
    }


    @Override
    public Action shoot(Action head, Vector2 dir, float strength, Projectile dec) {
        this.dir = dir;
        this.strength = strength;
        return this.move(head, strength , 0, dec);
    }

    @Override
    public Action move(Action head, float strength, int x, Projectile dec) {
        if (x == 0) {
            dir.nor();
            this.v = new Vector2((dir.x * strength) * 400, (dir.y * strength) * 400);
        }
        if (x == 900) {
            ProjectileAction action = generateAction();
            head.addChild(action);
            return action;
        }
        Action log = checkForHit(head, dec);
        if (projType == ProjType.PARABLE) {
            if (log == null) {
                pos.x += dir.x > 0 ? 1 : -1;
                pos.y = ((v.y * ((-this.startPos.x + pos.x) / v.x)) - (g/2) * (float)Math.pow(((-this.startPos.x + pos.x) / v.x), 2)) + this.startPos.y;
                return move(head, strength, x + 1, dec);
            } else {
                return log;
            }
        } else if (projType == ProjType.LINEAR || projType == ProjType.LASER) {
            if (log == null) {
                pos.add(dir.cpy().scl(strength));
                return move(head, strength, x + 1, dec);
            } else {
                return log;
            }
        } else {
            // Dieser Fall sollte nie eintreffen, wenn die Decorator funktionieren würden
            // return new ActionLog(head);
            return null;
        }
    }

    Action checkForHit(Action head, Projectile dec) {
        // checks if the Projectile hits a tile / wall
        int n = sim.getState().getBoardSizeX() * 16;
        int m = sim.getState().getBoardSizeY() * 16;

        Tile t = null;

        if ((this.pos.x >= 0 && this.pos.x < n) && (this.pos.y >= 0 && this.pos.y < m))
            t = sim.getState().getTile((int)(this.pos.x / 16), (int)(this.pos.y) / 16);
        if (t != null) {
            if (dec != this) {
                return dec.hitWall(head, t, dec, this);
            } else {
                ProjectileAction ac = generateAction();
                head.getChildren().add(ac);
                return t.onDestroy(ac);
            }
        }

        // check if hits character
        n = sim.getState().getTeamCount();
        m = sim.getState().getCharactersPerTeam();

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                GameCharacter character = sim.getState().getCharacterFromTeams(i, j);
                // ToDo: check4Hit
            }
        }


        return null;
    }
    @Override
    public Action hitCharacter(Action head, Character character, Projectile dec, BaseProjectile bsProj) {
        return null;
    }

    @Override
    public Action hitWall(Action head, Tile t, Projectile dec, BaseProjectile bsProj) {
        Path path = null;
        if (this.projType == ProjType.PARABLE) {
            path = new ParablePath(this.startPos, this.v);
        } else if (this.projType == ProjType.LINEAR) {
            path = new LinearPath(startPos, pos, 0.1f);
        }
        ProjectileAction projAction = new ProjectileAction(path, type, this.pos.cpy().sub(this.startPos).len(), this.pos);
        head.getChildren().add(projAction);
        return t.onDestroy(projAction);
    }

    public Action hitNothing(Action head) {
        return null;
    }

    public void setPos(Vector2 pos) {
        this.pos = pos.cpy();
        this.startPos = pos.cpy();
    }

    ProjectileAction generateAction() {
        Path path = null;
        float duration = 0;
        if (this.projType == ProjType.PARABLE) {
            path = new ParablePath(this.pos.cpy().sub(this.startPos).len(), this.startPos, this.v);
            duration = -((path.getPos(0).x - this.pos.x) / ((ParablePath)path).getStartVelocity().x);
        } else if (this.projType == ProjType.LINEAR) {
            path = new LinearPath(startPos, pos, 0.1f);
        }
        return new ProjectileAction(path, type, this.pos);
    }
}
