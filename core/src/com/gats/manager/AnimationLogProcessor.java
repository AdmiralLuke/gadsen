package com.gats.manager;

import com.gats.simulation.action.ActionLog;

public interface AnimationLogProcessor {

    void animate(ActionLog log);

    void awaitNotification();
}
