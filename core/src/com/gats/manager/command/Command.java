package com.gats.manager.command;

import com.gats.simulation.action.ActionLog;
import com.gats.simulation.GameCharacterController;

public abstract class Command {

    protected boolean isEndTurn = false;
    protected GameCharacterController controller;

    public Command(GameCharacterController controller) {
        this.controller = controller;
    }

    public abstract ActionLog onExecute();

    public ActionLog run(){
        if (controller.isActive()) return onExecute();
        return null;
    }

    public boolean isEndTurn() {
        return controller.isActive() && isEndTurn;
    }
}
