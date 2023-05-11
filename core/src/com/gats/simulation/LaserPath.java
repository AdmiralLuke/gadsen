package com.gats.simulation;

import com.badlogic.gdx.math.Vector2;

/**
 * Special implementation of a path where a linear path is traveled instantaneously.
 */
public class LaserPath extends LinearPath{


    float duration;
    /**
     * Creates the path from start to end that is travelled instantaneously. E.g. the position will be equal to end for any time larger 0.
     * @param startPos the start position of the path
     * @param endPos the end position of the path
     */
    public LaserPath(Vector2 startPos, Vector2 endPos, float duration) {
        super(startPos, endPos, 1);
        this.duration = duration;
    }

    @Override
    public Vector2 getPos(float t) {
        return t <= 0 ? getStart() : getEnd();
    }

    /**
     * Returns a small value larger than zero, as the path is travelled instantaneously.
     * @return the duration
     */
    @Override
    public float getDuration() {
        return duration;
    }
    public String toString() {
        return super.toString();
    }
}
