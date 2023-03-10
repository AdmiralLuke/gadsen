package com.gats.simulation.weapons;

import com.badlogic.gdx.math.Vector2;
import com.gats.simulation.*;

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
    public Action hitCharacter(Action head,Character character, Projectile dec, BaseProjectile bsProj) {
        return dec == this ? proj.hitCharacter(head,character, proj, bsProj) : proj.hitCharacter(head,character, dec, bsProj);
    }


    @Override
    public Action hitWall(Action head, Tile t, Projectile dec, BaseProjectile bsProj) {
        if (leftBounces == 0) {
            leftBounces = bounces;
            return proj.hitWall(head, t, proj, bsProj);
        } else {
            this.dir = bsProj.dir.cpy();
            // get the corner-points of a tile
            /*
              B -- C
              |    |
              A -- D
             */
            Vector2 posA = t.getWorldPosition();
            Vector2 posB = new Vector2(posA.x, posA.y + 15);
            Vector2 posC = new Vector2(posA.x + 15, posA.y + 15);
            Vector2 posD = new Vector2(posA.x + 15, posA.y);

            // for all 4 sides, i hope we'll never get tiles with more sides
            for (int i = 0; i < 4; i++) {
                Vector2 sideStart = null;
                Vector2 sideEnd = null;
                switch (i) {
                    case 0: // side A -> B
                        if (dir.x >= 0) {
                            sideStart = posA;
                            sideEnd = posB;
                        } else {
                            sideStart = posD;
                            sideEnd = posC;
                        }
                        break;
                    case 1: // side B -> C
                        if (dir.y < 0) {
                            sideStart = posB;
                            sideEnd = posC;
                        } else {
                            sideStart = posA;
                            sideEnd = posD;
                        }
                        break;
                    case 2: // side C -> D
                        if (dir.x < 0) {
                            sideStart = posA;
                            sideEnd = posB;
                        } else {
                            sideStart = posD;
                            sideEnd = posC;
                        }
                        break;
                    case 3: // side D -> A
                        if (dir.y >= 0) {
                            sideStart = posB;
                            sideEnd = posC;
                        } else {
                            sideStart = posA;
                            sideEnd = posD;
                        }
                        break;
                }

                Vector2 side = sideEnd.cpy().sub(sideStart);
                // weird warning, but it should be passed as an argument
                Vector2 normal = new Vector2(side.y, -side.x).nor();
                Vector2 startToSide = sideStart.cpy().sub(dir);

                // will it hit the side?
                if (startToSide.dot(normal) <= 0) {
                    // correct the projectile position to tile boarder for good-looking animation

                    // calculate the Intersection between the side and the direction

                    float dotProduct = dir.cpy().dot(normal);

                    float c = (sideStart.cpy().sub(dir)).dot(normal) / dotProduct;

                    bsProj.pos = dir.cpy().scl(c).add(sideStart);

                    // generate a path

                    ProjectileAction prAc = bsProj.generateAction();
                    head.getChildren().add(prAc);
                    ParticleAction pAc = new ParticleAction(bsProj.pos, ParticleType.BOUNCE);
                    prAc.getChildren().add(pAc);
                    bsProj.startPos = bsProj.pos;

                    switch (i % 2) {
                        case 0: // side AB or side CD, just flip x
                            this.dir = new Vector2(dir.x * (-1), dir.y);
                            break;
                        case 1: // side BC or AC, just flip y
                            this.dir = new Vector2(dir.x, dir.y * (-1));
                            break;
                    }

                    bounces--;
                    return bsProj.shoot(pAc, this.dir.cpy(), bsProj.strength, this);
                }
            }
            return proj.hitWall(head, t, proj, bsProj);
        }
    }


    @Override
    public Action shoot(Action head, Vector2 dir, float strength, Projectile dec) {
        return this.proj.shoot(head, dir, strength, dec);
    }

    @Override
    public void setPos(Vector2 pos) {
        proj.setPos(pos);
    }

    @Override
    public Action move(Action head, float strength, int t, Projectile dec) {
        return this.proj.move(head, strength, t, dec);
    }
}
