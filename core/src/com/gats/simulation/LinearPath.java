package com.gats.simulation;

import com.badlogic.gdx.math.Vector2;

/**
 * Klasse für einen Linearen-Weg zum Interpolieren in Abhängigkeit der Zeit. Verwendet das {@link Path} Interface
 */
public class LinearPath implements Path{

    private Vector2 s;
    private Vector2 g;
    private float endTime;
    // start time = 0
    private Vector2 dir;

    public static final float V = 10; // px/s

    /**
     * Erstellt einen Linearen Pfad anhand 2 Vektoren und einer Dauer des Weges
     * @param s Start-Vektor
     * @param g Ziel-Vektor
     * @param endTime Dauer des Weges von s nach g
     */
    public LinearPath(Vector2 s, Vector2 g, float endTime, float v) {
        this.s = s;
        this.g = g;
        this.endTime = endTime * v;
        this.dir = g.cpy().sub(s);
    }

    /**
     * Erstellt einen Linearen Pfad anhand 2 Vektoren, wobei die Dauer des Weges = Länge des Weges
     * @param s Start-Vektor
     * @param g Ziel-Vektor
     */
    public LinearPath(Vector2 s, Vector2 g, float v) {
        this.s = s;
        this.g = g;
        this.endTime = 0;
        this.dir = g.cpy().sub(s);
        try {
            endTime = dir.len() / V;
        } catch (NullPointerException e) {
            System.err.println("Fehler beim bestimmen der Länge eines Vektors -> LinearPath");
        }
        endTime *= v;
    }

    /**
     * gibt die Position mittels grundlegender Interpolation aus
     * @param t Zeit an der die Position ermittelt wird
     * @return Position des Objektes in Abhängigkeit der Zeit
     */
    @Override
    public Vector2 getPos(double t) {
        if (endTime == 0) return g.cpy();
        double step = t / endTime;
        Vector2 addV = new Vector2((float)(dir.x *  step), (float)(dir.y *  step));
        return s.cpy().add(addV);
    }

    public float getEndTime() {
        return endTime;
    }
    @Override
    public Vector2 getDir(double t) {
        return dir;
    }

    public Vector2 getG() {
        return g;
    }

    public Vector2 getS() {
        return s;
    }
}
