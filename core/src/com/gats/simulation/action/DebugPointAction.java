package com.gats.simulation.action;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class DebugPointAction extends Action{

    private final Vector2 pos;
    private final Color color;
    private final float duration;

    /**
     * Creates an Action that will place a point on screen for debugging-purposes.
     * Will be replaced by ParticleAction in the future
     *
     * @param delay     non-negative time-based offset to its parent in seconds
     * @param pos       position the point should appear at
     * @param color     the color of the point
     * @param duration  how long the point lasts in seconds
     */
    public DebugPointAction(float delay, Vector2 pos, Color color, float duration) {
        super(delay);
        this.pos = pos;
        this.color = color;
        this.duration = duration;
    }

    /**
     * @return position the point should appear at
     */
    public Vector2 getPos() {
        return pos;
    }
    /**
     * @return the color of the point
     */
    public Color getColor() {
        return color;
    }

    /**
     * @return how long the point lasts in seconds
     */
    public float getDuration() {
        return duration;
    }
}
