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
        this.path = new LinearPath(posBef, posAft);
        this.duration = path.getEndTime();
    }

    public Path getPath() {
        return path;
    }

    public float getDuration() {
        return duration;
    }
}
