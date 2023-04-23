package com.gats.manager.command;

import com.gats.simulation.GameCharacterController;
import com.gats.simulation.action.ActionLog;

public class EndTurnCommand extends Command{
    public EndTurnCommand(GameCharacterController controller) {
        super(controller);
        isEndTurn = true;
    }

    @Override
    public ActionLog onExecute() {
        return null;
    }

}
