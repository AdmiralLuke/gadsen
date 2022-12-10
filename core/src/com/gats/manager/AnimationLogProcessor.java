package com.gats.manager;

import com.gats.simulation.ActionLog;

public interface AnimationLogProcessor {

    void animate(ActionLog log);

    void notifyWhenComplete(Object toNotify);
}
