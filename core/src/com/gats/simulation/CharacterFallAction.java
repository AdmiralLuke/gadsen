package com.gats.simulation;

import com.badlogic.gdx.math.Vector2;

public class CharacterFallAction extends CharacterAction {
    private Vector2 posBef;
    private Vector2 posAft;
    private LinearPath path;
    private float duration;
    public CharacterFallAction(Vector2 posBef, Vector2 posAft, int team, int character, float delay) {
        super(team, character, delay);
        this.posBef = posBef;
        this.posAft = posAft;
        this.path = new LinearPath(posBef, posAft, 0.1f);
        this.duration = path.getEndTime();
    }

    public Path getPath() {
        return path;
    }

    public float getDuration() {
        return duration;
    }


    @Override
    public String toString() {
       String output = super.toString();

       output += "posBef: " +posBef;
       output += " ";
       output += "posAft: " + posAft;
       output += " ";
       return output;

    }
}
