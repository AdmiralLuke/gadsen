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

    public static <T> List<List<T>> subsetK(List<T> list, int subSetSize) {
        ArrayList<List<T>> results = new ArrayList<>();

        int listSize = list.size();
        if (subSetSize > listSize) return results;
        if (subSetSize <= 0) return results;
        if (subSetSize == listSize) {
            results.add(list);
            return results;
        }

        T head = list.remove(0);

        List<List<T>> results1 = subsetK(new ArrayList<>(list), subSetSize);
        results.addAll(results1);
        if (subSetSize == 1) {
            List<T> elemList = new ArrayList<>();
            elemList.add(head);
            results.add(elemList);
        }else {
            List<List<T>> results2 = subsetK(list, subSetSize - 1);
            for (List<T> cur :
                    results2) {
                cur.add(head);
                results.add(cur);
            }
        }
        return results;
    }
}
