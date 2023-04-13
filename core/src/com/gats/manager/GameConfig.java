package com.gats.manager;

import com.gats.ui.hud.UiMessenger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import com.gats.simulation.GameState.GameMode;

class GameConfig {

    public GameConfig() {
    }

    public GameConfig(RunConfiguration runConfiguration) {
        gui = runConfiguration.gui;
        animationLogProcessor = runConfiguration.animationLogProcessor;
        inputProcessor = runConfiguration.inputProcessor;
        uiMessenger = runConfiguration.uiMessenger;
        gameMode = runConfiguration.gameMode;
        players = runConfiguration.players;
        mapName = runConfiguration.mapName;
        teamSize = runConfiguration.teamSize;
        teamCount = runConfiguration.teamCount;
    }

    //Todo add default values
    public GameMode gameMode = null;

    public boolean gui = false;

    public AnimationLogProcessor animationLogProcessor = null;
    public InputProcessor inputProcessor = null;
    public UiMessenger uiMessenger = null;

    public String mapName;

    public int teamCount;
    public int teamSize;

    public List<Class<? extends Player>> players;

}
