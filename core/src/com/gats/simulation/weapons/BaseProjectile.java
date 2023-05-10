package com.gats.simulation.weapons;

import com.badlogic.gdx.math.Vector2;
import com.gats.simulation.action.Action;
import com.gats.simulation.*;
import com.gats.simulation.action.CharacterMoveAction;
import com.gats.simulation.action.ProjectileAction;

import java.util.ArrayList;

/**
 * Base Projectile for the {@link Projectile Projectile-Decorator}
 * simulates the basic behavior from a Projectile, like Movement, basic tile destruction and character hit actions
 */
public class BaseProjectile implements Projectile{

    ProjType projType;
    int damage;
    float recoil;
    float knockback;
    Tile lastTile;
    ArrayList<GameCharacter> activeCollisions = new ArrayList<>();

    Simulation sim;

    Vector2 startPos;

    Vector2 v;
    int range;
    Path path;

    float strength;

    ProjectileAction.ProjectileType type;

    GameCharacter shootedBy;
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
        if (type == ProjectileAction.ProjectileType.WOOL || type == ProjectileAction.ProjectileType.WATER || type == ProjectileAction.ProjectileType.GRENADE || type == ProjectileAction.ProjectileType.WATERBOMB) this.projType = ProjType.PARABLE;
        else if(type == ProjectileAction.ProjectileType.MIOJLNIR  || type == ProjectileAction.ProjectileType.CLOSE_COMB) this.projType = ProjType.LINEAR;
    }

    public BaseProjectile(int damage, float knockback, float recoil, Simulation sim, ProjectileAction.ProjectileType type) {
        this.damage = damage;
        this.recoil = recoil;
        this.knockback = knockback;
        this.sim = sim;
        this.type = type;
        if (type == ProjectileAction.ProjectileType.WOOL || type == ProjectileAction.ProjectileType.WATER || type == ProjectileAction.ProjectileType.GRENADE || type == ProjectileAction.ProjectileType.WATERBOMB ) this.projType = ProjType.PARABLE;
        else if(type == ProjectileAction.ProjectileType.MIOJLNIR || type == ProjectileAction.ProjectileType.CLOSE_COMB) this.projType = ProjType.LINEAR;
    }


    @Override
    public Action shoot(Action head, Vector2 dir, float strength, Projectile dec, GameCharacter character) {
        this.t = 0f;
        if (this.activeCollisions == null) {
            activeCollisions = new ArrayList<>();
        }
        this.activeCollisions.add(character);
        this.lastTile = null;
        this.strength = strength;
        this.shootedBy = character;
        // recoil
        if (recoil > 0) {
            Vector2 v = new Vector2((dir.x * (-1) * (0.1f * recoil)) * 400, (dir.y * (-1) * (0.1f * recoil)) * 400);
            Path path = new ParablePath(character.getPlayerPos(), 15, v);
            head = traverse(head, character, path, sim, this);
        }
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
            if (log == null) this.t += 0.00001f;
        }
        return log;
    }

    Action checkForHit(Action head, Projectile dec, Vector2 pos, Vector2 posN) {
        Action lastAc = null;
        Tile h = sim.getState().getTile((int)posN.x / 16, (int)posN.y / 16);
        if (h != null) {
            if (lastTile == null || !lastTile.equals(h)) {
                Tile tN = sim.getState().getTile((int) pos.x / 16, (int) pos.y / 16);
                while (tN == null || !tN.equals(h)) {
                    this.t += 0.000001f;
                    pos = path.getPos(t);
                    if (lastTile != null && !h.equals(lastTile)) lastTile = h;
                    tN = sim.getState().getTile((int) pos.x / 16, (int) pos.y / 16);
                }
                lastTile = h;
                activeCollisions = null;
                return dec.hitWall(head, tN, dec, this);
            }
        } else {
            lastTile = null;
        }

        if (t >= path.getDuration() || pos.y < 0) {
            return dec.hitNothing(head, dec, this);
        }

        ArrayList<GameCharacter> newCollisions = new ArrayList<>();

        for (int i = 0; i < sim.getState().getTeamCount(); i++) {
            for (int j = 0; j < sim.getState().getCharactersPerTeam(); j++) {
                GameCharacter character = sim.getState().getCharacterFromTeams(i, j);
                if (character != null && character.isAlive()) {
                    if (((int) character.getPlayerPos().x / 16 == (int) pos.x / 16) && ((int) character.getPlayerPos().y / 16 == (int) pos.y / 16)) {
                        newCollisions.add(character);
                        if (!activeCollisions.contains(character)) {
                            lastAc =  dec.hitCharacter(head, character, dec, this);
                        }
                    }
                }
            }
        }

        activeCollisions = newCollisions;
        // lastCharacter = null;


        return lastAc;
    }


    @Override
    public Action hitCharacter(Action head, GameCharacter character, Projectile dec, BaseProjectile bsProj) {
        int health = character.getHealth();
        int i = character.getTeam();
        int j = character.getTeamPos();
        Action pAc = generateAction();
        head.addChild(pAc);
        sim.getWrapper().setHealth(pAc, i, j, health - damage, false);
        Vector2 dir = bsProj.path.getDir(bsProj.t);
        dir.nor();
        int offset = 0;
        if (dir.x > 0) offset = 16;
        Vector2 v = new Vector2((dir.x * (0.8f * knockback)) * 400, (dir.y * (0.8f * knockback)) * 400);
        Vector2 pos = character.getPlayerPos().cpy();
        pos.x += offset;
        Path path = new ParablePath(pos,300, v);
        return traverse(pAc, character, path, this.sim, bsProj);
    }

    static Action traverse(Action head, GameCharacter character, Path path, Simulation sim, BaseProjectile bsProj) {
        if (bsProj.knockback == 0) return head;
        if (path.getDir(0).x == 0 && path.getDir(0).y == 0) return head;
        float t = 0f;
        float offset = 0;
        if (path.getDir(0).x < 0.00002f && path.getDir(0).x > -0.00002f) return head;
        if (path.getDir(0).x > 0) offset = 16;

        while (t < path.getDuration()) {
            Vector2 pos = path.getPos(t);
            if (pos.y <= 0) {
                path = new ParablePath(character.getPlayerPos(), pos, path.getDir(0));
                Action cmAc = new CharacterMoveAction(0f, character.getTeam(), character.getTeamPos(), path);
                Action hAc = sim.getWrapper().setHealth(cmAc, character.getTeam(), character.getTeamPos(), 0, true);
                head.addChild(cmAc);
                return hAc;
            }
            if (sim.getState().getTile((int)pos.x / 16, (int)pos.y / 16) != null) {
                if (bsProj.lastTile == null || !sim.getState().getTile((int)pos.x / 16, (int)pos.y / 16).equals(bsProj.lastTile)) {
                    t -= 0.001f;
                    break;
                }
            }
            if (t > 0.005) bsProj.lastTile = null;
            t += 0.001f;
        }

        Vector2 posN = path.getPos(t + 0.001f);
        Vector2 pos = path.getPos(t);
        Tile tile = sim.getState().getTile((int)posN.x / 16, (int)posN.y / 16);
        Vector2 tPos = tile.getWorldPosition();
        Vector2 b = new Vector2(tPos.x, tPos.y + 15);
        Vector2 c = new Vector2(b.x + 15, b.y);

        // b --- c

        path = new ParablePath(character.getPlayerPos(), path.getPos(t), path.getDir(0));
        if (path.getDuration() < 0.01f) return head;
        Action cmAc = new CharacterMoveAction(0f, character.getTeam(), character.getTeamPos(), path);
        sim.getWrapper().setPosition(character.getTeam(), character.getTeamPos(), (int)pos.x - (int)offset, (int)pos.y);
        head.addChild(cmAc);
        if (!(Math.floor(posN.y) == Math.floor(b.y) && Math.floor(posN.x) <= c.x && Math.floor(posN.x) >= b.x)) {
            cmAc = sim.getWrapper().fall(cmAc, character.getTeam(), character.getTeamPos());
        }
        Action hAc = sim.getWrapper().setHealth(cmAc, character.getTeam(), character.getTeamPos(), character.getHealth() - (int)path.getDuration(), true);
        return hAc;
    }

    @Override
    public Action hitWall(Action head, Tile t, Projectile dec, BaseProjectile bsProj) {
        ProjectileAction projAction = generateAction();
        head.getChildren().add(projAction);
        return sim.getWrapper().destroyTile(t, shootedBy, projAction);
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
        if (projType == ProjType.PARABLE) path = new ParablePath(this.path.getPos(0), this.path.getPos(t),  this.path.getDir(0));
        if (projType == ProjType.LINEAR) path = new LinearPath(this.path.getPos(0), this.path.getPos(t), 100);
        return new ProjectileAction(0,type, path);
    }
}
