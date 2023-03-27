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
            leftBounces--;
            Vector2 pos = bsProj.path.getPos(bsProj.t).cpy();
            Vector2 dir = bsProj.path.getDir(0).cpy();
            Action prAct = null;

            /* this is a tile represented as corner-coordinates
                   2
                B --- C
              1 |     | 3
                A --- D
                   4
             */

            // which side was hitted?
            int i = 0;

            Vector2 posA = t.getWorldPosition();
            Vector2 posB = new Vector2(posA.x, posA.y + 15);
            Vector2 posC = new Vector2(posA.x + 15, posA.y + 15);
            Vector2 posD = new Vector2(posA.x + 15, posA.y);

            if (Math.floor(pos.x) == Math.floor(posA.x) && pos.y <= posB.y) i = 0;
            if (Math.floor(pos.y) == Math.floor(posA.y) && pos.x <= posD.x) i = 1;
            if (Math.floor(pos.y) == Math.floor(posB.y) && pos.x <= posC.x) i = 2;
            if (Math.floor(pos.x) == Math.floor(posD.x) && pos.y <= posC.y) i = 3;

            // if hits
            DebugPointAction dbAc = new DebugPointAction(0, pos, Color.CORAL, 5, true);
            head.addChild(dbAc);
            if (i % 2 == 0) dir.x *= -1;
            if (i % 2 != 0) dir.y *= -1;
            prAct = bsProj.generateAction();
            head.addChild(prAct);
            // new Path
            dir.nor();
            Path path = null;
            Vector2 v = new Vector2((dir.x * bsProj.strength) * 400, (dir.y * bsProj.strength) * 400);
            if (bsProj.type == ProjectileAction.ProjectileType.COOKIE) path = new ParablePath(pos.cpy(), 10, v);
            else if(bsProj.type == ProjectileAction.ProjectileType.CANDY_CANE) path = new LinearPath(pos.cpy(), dir.cpy(), 10, 40);
            dec.setPath(path);
            bsProj.t = 0.011f;
            return bsProj.move(prAct, bsProj.strength, dec);
        }
    }


    @Override
    public Action shoot(Action head, Vector2 dir, float strength, Projectile dec) {
        leftBounces = bounces;
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
