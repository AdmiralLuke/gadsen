package com.gats.simulation;


import com.badlogic.gdx.math.Vector2;

/**
 * Character Action created whenever a character changes its aiming values
 */
public class CharacterAimAction extends CharacterAction {
    //Todo: Adjust strength value that will be translated to the scaling and it will change size
    private Vector2 angle;
    private float strength;
    public CharacterAimAction(int team, int character, Vector2 angle, float strength) {
        super(team, character, 0);
        this.angle = angle;
        this.strength = strength;
    }

    public Vector2 getAngle() {
        return this.angle;
    }

    public float getStrength() {
        return strength;
    }
}
