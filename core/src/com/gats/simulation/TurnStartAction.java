package com.gats.simulation;

public class TurnStartAction extends CharacterAction {

    public TurnStartAction(int team, int character, long delay) {
        super(team, character, delay);
    }

    @Override
    public String toString() {
        return "TurnStart: " + getTeam()+","+getCharacter();
    }
}
