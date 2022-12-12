package com.gats.simulation;


import com.badlogic.gdx.math.Vector2;

/**
 * Character Action created whenever a character changes its aiming values
 */
public class CharacterAimAction extends CharacterAction {
    //Todo: Adjust strength value that will be translated to the scaling and it will change size
    private float angle;
    private float strength;
    public CharacterAimAction(int team, int character, float angle, float strength) {
        super(team, character, 0);
        this.angle = angle;
        this.strength = strength;
    }

    public float getAngle() {
        return this.angle;
    }

    public float getStrength() {
        return strength;
    }
}
