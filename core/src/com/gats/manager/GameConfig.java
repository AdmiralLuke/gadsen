package com.gats.manager;

import com.gats.ui.hud.UiMessenger;

import java.util.ArrayList;
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

    public ArrayList<Class<? extends Player>> players;

    public static <T> ArrayList<ArrayList<T>> subsets(T[] elements, int subSetSize) {
        ArrayList<ArrayList<T>> results = new ArrayList<>();

        int size = elements.length;

        int curDecided = 0;
        boolean[] inclusionMask = new boolean[size];

        int inclusionCount = 0;

        while (inclusionCount < subSetSize) {
            while (curDecided < subSetSize) {
                if (size - curDecided + inclusionCount == subSetSize) {
                    ArrayList<T> subSet = new ArrayList<>(subSetSize);
                    for (int i = 0; i < subSetSize; i++) {
                        if (i < curDecided) {
                            if (inclusionMask[i]) subSet.add(elements[i]);
                        } else subSet.add(elements[i]);
                    }
                    results.add(subSet);
                }
            }

        }


        return results;
    }
}
