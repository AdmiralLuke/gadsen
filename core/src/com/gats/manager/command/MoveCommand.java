package com.gats.manager.command;

import com.gats.simulation.action.ActionLog;
import com.gats.simulation.GameCharacterController;

public class MoveCommand extends Command{
    private int dx;

    public MoveCommand(GameCharacterController controller, int dx) {
        super(controller);
        this.dx = dx;
    }

    @Override
    public ActionLog onExecute() {
        return controller.walk(dx);
    }
}
