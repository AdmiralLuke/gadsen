package com.gats.manager.command;

import com.gats.simulation.ActionLog;

public class EndTurnCommand extends Command{
    public EndTurnCommand() {
        super(null);
        isEndTurn = true;
    }

    @Override
    public ActionLog run() {
        return null;
    }

}
