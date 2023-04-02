package com.gats.simulation.weapons;

import com.badlogic.gdx.math.Vector2;
import com.gats.simulation.*;
import com.gats.simulation.action.Action;
import com.gats.simulation.action.CharacterHitAction;
import com.gats.simulation.action.CharacterMoveAction;

/**
 * Simulates the knockback, a GameCharacter will get when hit by this {@link Projectile Projectile}
 * Part of the Projectile Decorator
 */
public class Backnockable implements Projectile {

    private Projectile proj;
    private float amount;
    private Vector2 peak;

    public Backnockable(Projectile proj, float amount) {
        this.proj = proj;
        this.amount = amount;
    }

    @Override
    public Action hitWall(Action head, Tile t, Projectile dec, BaseProjectile bsProj) {
        return proj.hitWall(head,t, dec, bsProj);
    }

    @Override
    public Action hitCharacter(Action head, GameCharacter character, Projectile dec, BaseProjectile bsProj) {
        head = proj.hitCharacter(head, character, dec, bsProj);
        Vector2 dir = bsProj.path.getDir(bsProj.t);
        dir.nor();
        Vector2 v = new Vector2((dir.x * (bsProj.strength)) * 200, (dir.y * (bsProj.strength)) * 200);
        Path path = new ParablePath(character.getPlayerPos(),15, v);
        Action log = traverse(head, character, path, bsProj.sim);
        return log;
    }

    private Action traverse(Action head, GameCharacter character, Path path, Simulation sim) {
        float t = 0.05f;

        while (t < path.getDuration()) {
            Vector2 pos = path.getPos(t);
            if (pos.y <= 0) {

                path = new ParablePath(character.getPlayerPos(), pos, path.getDir(0));
                Action cmAc = new CharacterMoveAction(0f, character.getTeam(), character.getTeamPos(), path);
                Action hAc = sim.getWrapper().setHealth(cmAc, character.getTeam(), character.getTeamPos(), 0);
                head.addChild(cmAc);
                return hAc;
            }
            if (sim.getState().getTile((int)pos.x / 16, (int)pos.y / 16) != null) {
                t -= 0.001f;
                break;
            }
            t += 0.001f;
        }
        Vector2 pos = path.getPos(t);
        path = new ParablePath(character.getPlayerPos(), path.getPos(t), path.getDir(0));
        Action cmAc = new CharacterMoveAction(0f, character.getTeam(), character.getTeamPos(), path);
        sim.getWrapper().setPosition(character.getTeam(), character.getTeamPos(), (int)pos.x, (int)pos.y);
        Action fAc = sim.getWrapper().fall(cmAc, character.getTeam(), character.getTeamPos());
        Action hAc = sim.getWrapper().setHealth(fAc, character.getTeam(), character.getTeamPos(), character.getHealth() - (int)path.getDuration());
        head.addChild(cmAc);
        return hAc;
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

    public Action hitNothing(Action head, Projectile dec, BaseProjectile bsProj) {
        return proj.hitNothing(head, dec, bsProj);
    }
}
