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
    float modifier;

    private Projectile proj;

    public Bounceable(Projectile proj, int bounces, float modifier) {
        this.proj = proj;
        this.leftBounces = bounces;
        this.bounces = bounces;
        this.modifier = modifier % 1.01f;
    }

    @Override
    public Action hitCharacter(Action head, GameCharacter character, Projectile dec, BaseProjectile bsProj) {
        return proj.hitCharacter(head,character, dec, bsProj);
    }


    @Override
    public Action hitWall(Action head, Tile t, Projectile dec, BaseProjectile bsProj) {
        if (leftBounces <= 0) {
            return proj.hitWall(head, t, dec, bsProj);
        } else {
            leftBounces--;
            Vector2 pos = bsProj.path.getPos(bsProj.t).cpy();
            Vector2 dir = bsProj.path.getDir(bsProj.t).cpy();
            Action prAct = null;


            dir = bounce(pos, dir, t, bsProj.sim.getState());

            // DebugPointAction dbAc = new DebugPointAction(0, pos, Color.CORAL, 5, true);
            // head.addChild(dbAc);

            prAct = bsProj.generateAction();
            head.addChild(prAct);
            // new Path
            dir.nor();
            Path path = null;
            bsProj.strength *= modifier;
            if (bsProj.strength < 0.05) {
                return bsProj.hitWall(head, t, dec, bsProj);
            }
            Vector2 v = new Vector2((dir.x * (bsProj.strength)) * 400, (dir.y * (bsProj.strength)) * 400);
            if (bsProj.type == ProjectileAction.ProjectileType.WOOL || bsProj.type == ProjectileAction.ProjectileType.WATER || bsProj.type == ProjectileAction.ProjectileType.GRENADE) path = new ParablePath(pos.cpy(), 10, v);
            else if(bsProj.type == ProjectileAction.ProjectileType.MIOJLNIR || bsProj.type == ProjectileAction.ProjectileType.WATERBOMB || bsProj.type == ProjectileAction.ProjectileType.CLOSE_COMB) path = new LinearPath(pos.cpy(), dir.cpy(), 10, 40);
            dec.setPath(path);
            bsProj.t = 0f;
            return bsProj.move(prAct, bsProj.strength, dec);
        }
    }


    @Override
    public Action shoot(Action head, Vector2 dir, float strength, Projectile dec, GameCharacter character) {
        leftBounces = bounces;
        return this.proj.shoot(head, dir, strength, dec, character);
    }

    @Override
    public void setPath(Path path) {
        proj.setPath(path);
    }

    @Override
    public Action move(Action head, float strength, Projectile dec) {
        return this.proj.move(head, strength, dec);
    }

    public Vector2 bounce(Vector2 pos, Vector2 dir, Tile t, GameState state) {
        /* this is a tile represented as corner-coordinates
                   3
                B --- C
              0 |     | 2
                A --- D
                   1
             */

        // which side was hitted?
        int i = 0;

        Vector2 posA = t.getWorldPosition();
        Vector2 posB = new Vector2(posA.x, posA.y + 15);
        Vector2 posC = new Vector2(posA.x + 15, posA.y + 15);
        Vector2 posD = new Vector2(posA.x + 15, posA.y);

        if (Math.floor(pos.x) == Math.floor(posA.x) && pos.y <= posB.y && pos.y >= posA.y) i = 0;
        if (Math.floor(pos.y) == Math.floor(posA.y) && pos.x <= posD.x && pos.x >= posA.x) i = 1;
        if (Math.floor(pos.y) == Math.floor(posB.y) && pos.x <= posC.x && pos.x >= posB.x) i = 3;
        if (Math.floor(pos.x) == Math.floor(posD.x) && pos.y <= posC.y && pos.y >= posD.y) i = 2;



        /* Possible Neighbour Tiles
          + --- + --- + --- +
          | lu  |  u  | ru  |
          + --- B --- C --- +
          |  l  |     |  r  |
          + --- A --- D --- +
          | ld  |  d  | rd  |
          + --- + --- + --- +
         */
        IntVector2 posToTile = new IntVector2((int)pos.x / 16, (int)pos.y / 16);


        Tile l = state.getTile(posToTile.x - 1, posToTile.y);
        Tile d = state.getTile(posToTile.x, posToTile.y - 1);
        Tile u = state.getTile(posToTile.x, posToTile.y + 1);
        Tile r = state.getTile(posToTile.x + 1, posToTile.y);


        Tile lu = state.getTile(posToTile.x - 1, posToTile.y + 1);
        Tile ld = state.getTile(posToTile.x - 1,  posToTile.y - 1);
        Tile ru = state.getTile(posToTile.x + 1, posToTile.y + 1);
        Tile rd = state.getTile(posToTile.x + 1, posToTile.y - 1);

        // directly hit Corner A
        if (Math.floor(pos.x) == Math.floor(posA.x) && Math.floor(pos.y) == Math.floor(posA.y)) {
            // we can have two Neighbored Tiles


            if (l == null && d != null) {
                i = 1;
            } else if (l != null && d != null) {
                i = 4; // back to where it comes from
            } else if (l != null && d == null) {
                i = 2;
            }

            if (ld != null) i = 4;
        }

        // directly hit corner B

        if (Math.floor(pos.x) == Math.floor(posB.x) && Math.floor(pos.y) == Math.floor(posB.y)) {
            if (r == null && u != null) {
                i = 2;
            } else if (r != null && u != null) {
                i = 4;
            } else if (r != null && u == null) {
                i = 1;
            }

            if (lu != null) i = 4;
        }

        // directly hit corner C

        if (Math.floor(pos.x) == Math.floor(posC.x) && Math.floor(pos.y) == Math.floor(posC.y)) {
            if (l == null && u != null) {
                i = 2;
            } else if (l != null && u != null) {
                i = 4;
            } else if (l != null && u == null) {
                i = 1;
            }

            if (ru != null) i = 4;
        }

        // directly hit corner D

        if (Math.floor(pos.x) == Math.floor(posD.x) && Math.floor(pos.y) == Math.floor(posD.y)) {
            if (r == null && d != null) {
                i = 1;
            } else if (r != null && d != null) {
                i = 4; // back to where it comes from
            } else if (r != null && d == null) {
                i = 2;
            }

            if (rd != null) i = 4;
        }

        // if hits

        if (i % 2 == 0) dir.x *= -1;
        if (i % 2 != 0) dir.y *= -1;
        if (i == 4) {
            dir.y *= -1;
        }
        return dir;
    }

    public Action hitNothing(Action head, Projectile dec, BaseProjectile bsProj) {
        return proj.hitNothing(head, dec, bsProj);
    }
}
