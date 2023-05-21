package com.gats.manager;

import com.gats.simulation.GameState;
import com.gats.simulation.action.ActionLog;

public interface AnimationLogProcessor {

    void init(GameState state,String[] playerNames);

    void animate(ActionLog log);

    void awaitNotification();
}
