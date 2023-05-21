package com.gats.simulation.campaign;

import com.badlogic.gdx.math.Vector2;
import com.gats.manager.Controller;
import com.gats.simulation.GameState;
import com.gats.simulation.WeaponType;

public class Level3_2Bot extends StaticBot{

    @Override
    protected void executeTurn(GameState state, Controller controller) {
        super.executeTurn(state, controller);
        switch (turnCount){
            case 0:
                controller.selectWeapon(WeaponType.WATER_PISTOL);
                controller.aim(135, 1);
            default:
                controller.shoot();
                break;
        }
    }
}
