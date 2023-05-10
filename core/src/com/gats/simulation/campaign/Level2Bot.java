package com.gats.simulation.campaign;

import com.badlogic.gdx.math.Vector2;
import com.gats.manager.Controller;
import com.gats.simulation.GameState;
import com.gats.simulation.WeaponType;

public class Level2Bot extends StaticBot {

    @Override
    protected void executeTurn(GameState state, Controller controller) {
        super.executeTurn(state, controller);
        switch (turnCount){
            case 0:
                controller.aim(new Vector2(-1, 0.1f), 1);
                controller.selectWeapon(WeaponType.WATER_PISTOL);
            default:
                controller.shoot();
                break;
        }
    }
}
