package com.gats.manager.command;

import com.gats.simulation.action.ActionLog;
import com.gats.simulation.GameCharacterController;

public class ShootCommand extends Command{

    public ShootCommand(GameCharacterController controller) {
        super(controller);
    }

    @Override
    public ActionLog run() {
        return controller.shoot();
    }
}
