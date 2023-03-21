package com.gats.simulation;

import com.badlogic.gdx.math.Vector2;

import java.security.InvalidParameterException;

/**
 * Stores a parabolic path.
 */
public class ParablePath implements Path {

    private final float duration;
    private final Vector2 startPosition;
    private final static float g = 9.81f * 16; //9.81 meter/s^2 * 16 pixel pro meter
    private final Vector2 startVelocity;

    /**
     * Creates a parabolic path, starting at the specified position with the specified velocity and is followed for the specified duration.
     *
     * @param startPosition
     * @param startVelocity
     * @param endPosition
     */
    public ParablePath(Vector2 startPosition, Vector2 endPosition, Vector2 startVelocity) {
        this.startPosition = startPosition;
        this.startVelocity = startVelocity;
        this.duration = -((startPosition.x - endPosition.x) / startVelocity.x);
    }

    //ToDo: Implement
    /**
     * Creates a parabolic path, that connects start while peaking at the specified height.
     * The specified peak must therefore be at least as high as both start- and end-point.
     *
     * @param start
     * @param end
     * @param peak  maximum y-value
     */
//    public ParablePath(Vector2 start, float peak, Vector2 end) {
//        if (peak < start.y || peak < end.y)
//            throw new IllegalArgumentException("The peak of a gravity bound parabola has to be at least as high as both its start and end point");
//        this.startPosition = start;
//        this.startVelocity = new Vector2(0,0);
//        // s_y(p) = peak | v_y(p) = 0
//        // p is the point in time where
//        float p = (float) Math.sqrt(2f/(3*g) * (start.y - peak));
//        startVelocity.y = 1/p *(start.y - p*p*g/2 - peak);
//
//
//    }

    /**
     * Creates a parabolic path, that begins at start and travels through anchor while peaking at the specified height.
     * The specified peak must therefore at least as high as both start and anchor.
     * This path will be followed with the specified velocity for the specified duration.
     *
     * @param duration
     * @param start
     * @param anchor
     * @param peak
     * @param velocity
     */
//    public ParablePath(float duration, Vector2 start, Vector2 anchor, float peak, float velocity) {
//        this.duration = duration;
//        this.startPosition = start;
//        this.startVelocity = startVelocity;
//    }

    /**
     * Returns the position for the specified time.
     * Will only give valid results between 0 and {@link #getDuration()} (inclusive).
     * @param t time in seconds
     * @return the position at time t
     */
    @Override
    public Vector2 getPos(float t) {
        float x = (startVelocity.x * t) + startPosition.x;
        float y = (((startVelocity.y * t) - ((g / 2) * t * t)) + startPosition.y);
        return new Vector2(x, y);
    }

    /**
     * Returns a tangent on the path at the specified time.
     * Will only give valid results between 0 and {@link #getDuration()} (inclusive).
     * @param t time in seconds
     * @return the movement direction at time t
     */
    public Vector2 getDir(float t) {
        return new Vector2(startVelocity.x, startVelocity.y - (g * t * t));
    }

    /**
     * @return the maximum valid input-time for this path in seconds
     */
    @Override
    public float getDuration() {
        return this.duration;
    }

    /**
     * @return the initial velocity as 2D vector
     */
    public Vector2 getStartVelocity() {
        return startVelocity;
    }
}
