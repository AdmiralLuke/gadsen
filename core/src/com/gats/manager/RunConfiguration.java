package com.gats.manager;

import com.gats.ui.HudStage;

import java.util.ArrayList;

public class RunConfiguration {

    public int gameMode = 0;

    public boolean gui = false;

    public AnimationLogProcessor animationLogProcessor = null;

    //Generalize to Interface
    public HudStage hud = null;

    public String mapName;

    public int teamCount;
    public int teamSize;

    public ArrayList<Class<? extends Player>> players;
}
