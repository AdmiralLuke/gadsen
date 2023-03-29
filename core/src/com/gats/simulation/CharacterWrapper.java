package com.gats.simulation;

import com.gats.simulation.action.Action;

public class CharacterWrapper {

    private GameCharacter[][] team;
    CharacterWrapper(GameCharacter[][] team) {
        this.team = team;
    }

    public Action setHealth(Action head, int team, int gchar, int newHealth) {
        return this.team[team][gchar].setHealth(newHealth, head);
    }
}
