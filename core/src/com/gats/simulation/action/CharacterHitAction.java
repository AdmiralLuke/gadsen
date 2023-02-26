package com.gats.simulation.action;

public class CharacterHitAction extends CharacterAction{
    private final int healthBef;
    private final int healthAft;
    public CharacterHitAction(int team, int character, int healthBef, int healthAft) {
        super(team, character, 0.01f);
        this.healthBef = healthBef;
        this.healthAft = healthAft;
    }

    public int getHealthAft() {
        return healthAft;
    }

    public int getHealthBef() {
        return healthBef;
    }
}
