package com.gats.simulation;

import com.badlogic.gdx.math.Vector2;

/**
 * Klasse für einen Parabel-Förmigen-Weg zum Interpolieren in Abhängigkeit der Zeit. Verwendet das {@link Path} Interface
 */
public class ParablePath implements Path {

    private final Vector2 s;
    private final static double g = 9.81 * 8;
    private final Vector2 v;
    private float endTime;

    public ParablePath(Vector2 s, Vector2 v) {
        this.s = s;
        this.v = v;
    }

    @Override
    public Vector2 getPos(double t) {
        double x = (v.x * t) + s.x;
        double y = (((v.y * t) - ((g / 2) * t * t)) + s.y);
        return new Vector2((float)x, (float)y);
    }

    @Override
    public float getEndTime() {
        return 0;
    }

    public Vector2 getV() {
        return v;
    }
}
