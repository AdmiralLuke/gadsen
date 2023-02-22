package com.gats.simulation;

public class CharacterHitAction extends CharacterAction{
    private int healthBef;
    private int healthAft;
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


    @Override
    public String toString() {
       String output = super.toString();
       output += "healthBef: " + healthBef;
       output += " ";
       output += "healthAft: " + healthAft;
       return output;
    }
}
