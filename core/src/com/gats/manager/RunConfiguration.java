package com.gats.manager;

import java.util.ArrayList;

public class RunConfiguration {

    public int gameMode = 0;

    public boolean gui = false;

    public AnimationLogProcessor animationLogProcessor = (log) -> {
    };

    public String mapName;

    public int teamCount;
    public int teamSize;

    public ArrayList<Class<? extends Player>> players;
}
