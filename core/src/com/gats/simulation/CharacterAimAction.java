package com.gats.simulation;


import com.badlogic.gdx.math.Vector2;

/**
 * Character Action created whenever a character changes its aiming values
 */
public class CharacterAimAction extends CharacterAction {

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

    @Override
    public String toString() {

        String output = super.toString();
        String angleDegree = "" +angle.angleDeg();

        output += "Angle: " + angle.toString() + " Deg: "+ angleDegree;
        output += " ";
        output += "Strength: " + strength;
        output += " ";
        return output;
    }
}
