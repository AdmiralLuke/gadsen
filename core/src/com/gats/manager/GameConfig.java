package com.gats.manager;

import com.gats.ui.hud.UiMessenger;

import java.util.ArrayList;

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
    public int gameMode = 0;

    public boolean gui = false;

    public AnimationLogProcessor animationLogProcessor = null;
    public InputProcessor inputProcessor = null;
    public UiMessenger uiMessenger = null;

    public String mapName;

    public int teamCount;
    public int teamSize;

    public ArrayList<Class<? extends Player>> players;
}
