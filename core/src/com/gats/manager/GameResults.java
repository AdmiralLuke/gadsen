package com.gats.manager;

import com.gats.simulation.GameState;
import com.gats.simulation.action.ActionLog;

import java.io.Serializable;
import java.util.ArrayList;

public class GameResults implements Serializable {
    private final ArrayList<ActionLog> actionLogs = new ArrayList<>();
    private final GameConfig config;
    private GameState initialState;
    private float[] scores;

    protected GameResults(GameConfig config) {
        this.config = config;
    }

    protected void setInitialState(GameState initialState) {
        this.initialState = initialState.copy();
    }

    public void addActionLog(ActionLog log){
        actionLogs.add(log);
    }

    public ArrayList<ActionLog> getActionLogs() {
        return actionLogs;
    }

    public GameConfig getConfig() {
        return config;
    }

    public GameState getInitialState() {
        return initialState;
    }

    public float[] getScores() {
        return scores;
    }
}
