package com.gats.manager.command;

import com.gats.simulation.GameCharacterController;

public abstract class Command {

    protected boolean isEndTurn = false;
    protected GameCharacterController controller;

    public Command(GameCharacterController controller) {
        this.controller = controller;
    }

    public abstract void run();

    public boolean isEndTurn() {
        return isEndTurn;
    }
}
