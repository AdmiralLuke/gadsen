package com.gats.simulation;

import com.badlogic.gdx.math.Vector2;

/**
 * Stores a linear path from a start to an end point
 */
public class LinearPath implements Path {

    private final Vector2 start;
    private final Vector2 end;
    private float endTime;
    // start time = 0
    private final Vector2 dir;


    /**
     * Creates a linear path from start to end with the specified duration
     *
     * @param duration duration in seconds
     * @param start    Start point
     * @param end      End point
     */
    public LinearPath(float duration, Vector2 start, Vector2 end) {
        this.start = start;
        this.end = end;
        this.endTime = duration;
        this.dir = end.cpy().sub(start);
    }

    /**
     * Creates a linear path from start to end that will be travelled with the specified velocity.
     * The velocity has to be larger than zero.
     *
     * @param start Start-Vektor
     * @param end   Ziel-Vektor
     * @param v     velocity
     */
    public LinearPath(Vector2 start, Vector2 end, float v) {
        this.start = start;
        this.end = end;
        this.endTime = 0;
        this.dir = end.cpy().sub(start);
        endTime = dir.len() / v;
    }

    /**
     * Returns the position for the specified time, using linear interpolation between start and end
     * Will only give valid results between 0 and {@link #getEndTime()} (inclusive).
     * @param t time in seconds
     * @return the position at time t
     */
    @Override
    public Vector2 getPos(float t) {
        if (endTime == 0) return end.cpy();
        double step = t / endTime;
        Vector2 addV = new Vector2((float) (dir.x * step), (float) (dir.y * step));
        return start.cpy().add(addV);
    }

    /**
     * @return the duration
     */
    public float getEndTime() {
        return endTime;
    }

    /**
     * Returns the direction of this path.
     * Since this is a linear path it will be constant for all times t
     *
     * @param t time in seconds
     * @return the movement direction
     */
    @Override
    public Vector2 getDir(float t) {
        return dir;
    }

    /**
     * @return the start position of the path
     */
    protected Vector2 getStart() {
        return start;
    }

    /**
     * @return the end position of the path
     */
    protected Vector2 getEnd() {
        return end;
    }

    @Override
    public String toString() {
        return "from: " + getPos(0) + "to: " +getPos(endTime);
    }
}
