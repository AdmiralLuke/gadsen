package com.gats.manager.command;

import com.gats.simulation.GameCharacterController;
import com.gats.simulation.WeaponType;

public class WeaponSelectCommand extends Command{

    private WeaponType type;

    public WeaponSelectCommand(GameCharacterController controller, WeaponType type) {
        super(controller);
        this.type = type;
        System.out.println("Selecting " + type);
    }

    @Override
    public void run() {
        controller.selectWeapon(type);
    }
}
