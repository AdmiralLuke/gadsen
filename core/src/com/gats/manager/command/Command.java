package com.gats.manager.command;

import com.gats.simulation.ActionLog;
import com.gats.simulation.GameCharacterController;

public abstract class Command {

    protected boolean isEndTurn = false;
    protected GameCharacterController controller;

    public Command(GameCharacterController controller) {
        this.controller = controller;
    }

    public abstract ActionLog run();

    public boolean isEndTurn() {
        return isEndTurn;
    }
}
