package com.gats.simulation.campaign;

import com.badlogic.gdx.math.Vector2;
import com.gats.manager.Controller;
import com.gats.simulation.GameState;
import com.gats.simulation.WeaponType;

public class Level1Bot extends StaticBot {

    @Override
    protected void executeTurn(GameState state, Controller controller) {
        switch (turnCount){
            case 1:
                controller.aim(new Vector2(-1, 1), 1);
                controller.selectWeapon(WeaponType.WATER_PISTOL);
                controller.shoot();
                break;
        }
    }
}
