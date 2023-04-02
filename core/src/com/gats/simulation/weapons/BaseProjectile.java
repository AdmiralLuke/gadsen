package com.gats.simulation.weapons;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.gats.simulation.action.Action;
import com.gats.simulation.*;
import com.gats.simulation.action.DebugPointAction;
import com.gats.simulation.action.ProjectileAction;

import java.util.ArrayList;

/**
 * Base Projectile for the {@link Projectile Projectile-Decorator}
 * simulates the basic behavior from a Projectile, like Movement, basic tile destruction and character hit actions
 */
public class BaseProjectile implements Projectile{

    ProjType projType;
    int damage;
    int recoil;
    int knockback;
    Tile lastTile;
    GameCharacter lastCharacter;

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
    }


    @Override
    public Action shoot(Action head, Vector2 dir, float strength, Projectile dec, GameCharacter character) {
        this.t = 0f;
        this.strength = strength;
        return this.move(head, strength , dec);
    }

    @Override
    public Action move(Action head, float strength, Projectile dec) {
        Action log = null;
        while (log == null) {
            Vector2 pos = path.getPos(t);
            Vector2 posN = path.getPos(this.t + 0.001f);
            // DebugPointAction dbAc = new DebugPointAction(0, pos, Color.BLUE, 0.1f, true);
            // head.addChild(dbAc);
            log = checkForHit(head, dec, pos, posN);
            if (log == null) this.t += 0.001f;
        }
        return log;
    }

    Action checkForHit(Action head, Projectile dec, Vector2 pos, Vector2 posN) {
        Tile h = sim.getState().getTile((int)posN.x / 16, (int)posN.y / 16);
        if (h != null) {
            if (lastTile == null || !lastTile.equals(h)) {
                Tile tN = sim.getState().getTile((int) pos.x / 16, (int) pos.y / 16);
                while (tN == null || !tN.equals(h)) {
                    this.t += 0.0001f;
                    pos = path.getPos(t);
                    if (lastTile != null && !h.equals(lastTile)) lastTile = h;
                    tN = sim.getState().getTile((int) pos.x / 16, (int) pos.y / 16);
                }
                lastTile = h;
                return dec.hitWall(head, tN, dec, this);
            }
        } else {
            lastTile = null;
        }

        if (t >= path.getDuration() || pos.y < 0) return dec.hitNothing(head, dec, this);


        for (int i = 0; i < sim.getState().getTeamCount(); i++) {
            for (int j = 0; j < sim.getState().getCharactersPerTeam(); j++) {
                GameCharacter character = sim.getState().getCharacterFromTeams(i, j);
                if (character != null) {
                    if (((int) character.getPlayerPos().x / 16 == (int) pos.x / 16) && ((int) character.getPlayerPos().y / 16 == (int) pos.y / 16)) {
                        if (this.t == 0f) {
                            lastCharacter = character;
                            return null;
                        }
                        if (lastCharacter == null) {
                            lastCharacter = character;
                            return dec.hitCharacter(head, character, dec, this);
                        }
                        if (lastCharacter == character) return null;
                    }
                }
            }
        }

        lastCharacter = null;

        return null;
    }


    @Override
    public Action hitCharacter(Action head, GameCharacter character, Projectile dec, BaseProjectile bsProj) {
        int health = character.getHealth();
        int i = character.getTeam();
        int j = character.getTeamPos();
        Action pAc = generateAction();
        head.addChild(pAc);
        return sim.getWrapper().setHealth(pAc, i, j, health - damage);
    }

    @Override
    public Action hitWall(Action head, Tile t, Projectile dec, BaseProjectile bsProj) {
        ProjectileAction projAction = generateAction();
        head.getChildren().add(projAction);
        return t.onDestroy(projAction);
    }

    @Override
    public Action hitNothing(Action head, Projectile dec, BaseProjectile bsProj) {
        t = Math.min(t, this.path.getDuration());
        ProjectileAction prAc = generateAction();
        head.addChild(prAc);
        return prAc;
    }

    @Override
    public void setPath(Path path) {
        this.path = path;
    }

    ProjectileAction generateAction() {
        Path path = null;
        if (projType == ProjType.PARABLE) path = new ParablePath(this.path.getPos(0), this.path.getPos(t), this.path.getDir(0));
        if (projType == ProjType.LINEAR) path = new LinearPath(this.path.getPos(0), this.path.getPos(t), 100);
        return new ProjectileAction(0,type, path);
    }

}
