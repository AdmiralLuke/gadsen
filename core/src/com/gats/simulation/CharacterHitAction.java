package com.gats.simulation;

public class CharacterHitAction extends CharacterAction{
    private int healthBef;
    private int healthAft;
    public CharacterHitAction(int team, int character, int healthBef, int healthAft) {
        super(team, character, 0.01f);
    }

    public int getHealthAft() {
        return healthAft;
    }

    public int getHealthBef() {
        return healthBef;
    }
}
