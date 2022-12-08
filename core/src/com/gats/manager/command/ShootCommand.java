package com.gats.manager.command;

import com.gats.simulation.GameCharacterController;

public class ShootCommand extends Command{

    public ShootCommand(GameCharacterController controller) {
        super(controller);
    }

    @Override
    public void run() {
        controller.shoot();
    }
}
