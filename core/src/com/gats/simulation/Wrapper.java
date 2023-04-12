package com.gats.simulation;

import com.gats.simulation.action.Action;

public class Wrapper {

    private GameCharacter[][] team;
    Wrapper(GameCharacter[][] team) {
        this.team = team;
    }

    public Action setHealth(Action head, int team, int gchar, int newHealth) {
        return this.team[team][gchar].setHealth(newHealth, head);
    }

    public void setPosition(int team, int gchar, int x, int y) {
        this.team[team][gchar].setPosX(x);
        this.team[team][gchar].setPosY(y);
    }

    public Action fall(Action head, int team, int gchar) {
        return this.team[team][gchar].fall(head);
    }

    public void tileSetAnchored(Tile t) {
        t.setAnchor(true);
    }
}
