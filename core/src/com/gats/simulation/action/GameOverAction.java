package com.gats.simulation.action;

public class GameOverAction extends Action{

    private int team;

    public GameOverAction(int team) {
        super(0f);
        this.team = team;
    }

    public int getTeam() {
        return team;
    }
}
