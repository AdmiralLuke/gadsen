package com.gats.simulation.action;

import com.badlogic.gdx.math.Vector2;
import com.gats.simulation.LinearPath;
import com.gats.simulation.Path;

public class CharacterFallAction extends CharacterAction {
    private final LinearPath path;
    private final float duration;
    public CharacterFallAction(Vector2 posBef, Vector2 posAft, int team, int character, float delay) {
        super(team, character, delay);
        this.path = new LinearPath(posBef, posAft, 0.1f);
        this.duration = path.getEndTime();
    }

    public Path getPath() {
        return path;
    }

    public float getDuration() {
        return duration;
    }
}
