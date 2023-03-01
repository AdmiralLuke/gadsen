package com.gats.simulation.action;


import com.badlogic.gdx.math.Vector2;

/**
 * Type of {@link CharacterAction} created, whenever a Character changes its aiming-direction and/or -strength
 */
public class CharacterAimAction extends CharacterAction {

    private final Vector2 angle;
    private final float strength;

    /**
     * Stores the event of altering a Characters aiming-attributes
     * @param team team index of the Character
     * @param character Characters index within its team
     * @param angle the new aiming-direction
     * @param strength the new aiming-strength
     */
    public CharacterAimAction(int team, int character, Vector2 angle, float strength) {
        super(0, team, character);
        this.angle = angle;
        this.strength = strength;
    }

    /**
     * @return the new aiming-direction
     */
    public Vector2 getAngle() {
        return this.angle;
    }

    /**
     * @return the new aiming-strength
     */
    public float getStrength() {
        return strength;
    }
}
