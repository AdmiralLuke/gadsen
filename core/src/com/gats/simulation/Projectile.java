package com.gats.simulation;

import com.badlogic.gdx.math.Vector2;

/**
 * Speichert die Eigenschaften und legt die Verhaltensweise eines Projektils fest,
 * welches durch das Benutzen einer {@link Weapon Waffe} mittels eines {@link GameCharacter Spielfigur} erzeugt wird
 */
public class Projectile {

    private double damage;
    private double damageLoss;
    private double range;
    private double livingTime;
    private Vector2 pos;
    private Vector2 dir;

    private Path path;


    // ToDo: find the best settings for gravity
    private final static float g = 9.81f;

    enum Type {
        LINEAR,
        PARABLE
    }

    private Type type;

    Projectile(double damage, double damageLoss, double range, Vector2 pos, Vector2 dir, Type type) {
        this.damage = damage;
        this.damageLoss = damageLoss;
        this.range = range;
        this.livingTime = 0;
        this.pos = pos;
        this.dir = dir;
        this.type = type;
    }

    Projectile(double damage, double range, Vector2 pos, Vector2 dir, Type type) {
        new Projectile(damage, 0, range, pos, dir, type);
    }

    private ActionLog move() {
        ActionLog moveLog = new ActionLog();
        if (this.type == Type.LINEAR) {
            // moveLog.addAction();
        } else if (this.type == Type.PARABLE) {
            if (livingTime < range) {
                Vector2 oldPos = pos.cpy();
                pos.x = (float)(dir.y * livingTime);
                dir.y = (float)(dir.y - (g * livingTime));
                pos.y = (float)((dir.y * livingTime) - ((g / 2) * livingTime * livingTime));
                livingTime++;
                moveLog.addAction(new ProjectileMoveAction(this.path, this));
            }
        }
        return moveLog;
    }

    public static void main(String[] args) {
        Projectile proj = new Projectile(0, 0, 10, new Vector2(1,1), new Vector2(1,1), Type.PARABLE);
        for (int i = 0; i < 10; i++) {
            ActionLog testLog = proj.move();
            System.out.println(testLog.getNextAction());
        }
    }
}
