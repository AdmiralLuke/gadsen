package com.gats.manager;

public class RunConfiguration {

    public int gameMode = 0;

    public boolean gui = false;

    public AnimationLogProcessor animationLogProcessor = (log) -> {
    };

    public String mapName;

    public int teamCount;
    public int teamSize;
}
