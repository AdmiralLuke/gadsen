package com.gats.simulation.action;

public class ScoreAction extends CharacterAction{

    private final float newScore;

    public ScoreAction(float delay, int team, int character, float newScore) {
        super(delay, team, character);
        this.newScore = newScore;
    }

    public float getNewScore() {
        return newScore;
    }
}
