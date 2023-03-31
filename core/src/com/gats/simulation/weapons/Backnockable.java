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
        return dec == this ? proj.hitWall(head,t, proj, bsProj) : proj.hitWall(head,t, dec, bsProj);
    }

    @Override
    public Action hitCharacter(Action head, GameCharacter character, Projectile dec, BaseProjectile bsProj) {
        Vector2 v = bsProj.path.getDir(bsProj.t);
        Path path = new ParablePath(character.getPlayerPos(),10, v);
        head = traverse(head, character, path, bsProj.sim);

        return proj.hitCharacter(head, character, dec, bsProj);
    }

    private Action traverse(Action head, GameCharacter character, Path path, Simulation sim) {
        float t = 0f;
        Vector2 newDir = path.getDir(0);
        newDir.x *= amount;
        newDir.y *= amount;
        while (t < path.getDuration()) {
            Vector2 pos = path.getPos(t);
            if (pos.y <= 0) {
                Action hAc = sim.getWrapper().setHealth(head, character.getTeam(), character.getTeamPos(), 0);
                path = new ParablePath(character.getPlayerPos(), pos, path.getDir(0));
                Action cmAc = new CharacterMoveAction(0f, character.getTeam(), character.getTeamPos(), path);
                cmAc.addChild(hAc);
                head.addChild(cmAc);
                return hAc;
            }
            if (sim.getState().getTile((int)pos.x / 16, (int)pos.y / 16) != null) {
                t -= 0.001f;
                break;
            }
            t += 0.001f;
        }
        path = new ParablePath(character.getPlayerPos(), path.getPos(t), path.getDir(0));
        Action cmAc = new CharacterMoveAction(0f, character.getTeam(), character.getTeamPos(), path);
        head.addChild(cmAc);
        return cmAc;
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
