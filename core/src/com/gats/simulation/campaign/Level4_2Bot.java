package com.gats.simulation.campaign;

import com.gats.manager.Controller;
import com.gats.simulation.GameState;
import com.gats.simulation.WeaponType;

public class Level4_2Bot extends StaticBot{


    @Override
    protected void executeTurn(GameState state, Controller controller) {
        super.executeTurn(state, controller);
        if (turnCount == 0) {
            controller.selectWeapon(WeaponType.WATER_PISTOL);
        }
        float enemyX = state.getCharacterFromTeams(0, 0).getPlayerPos().x;
        float X = controller.getGameCharacter().getPlayerPos().x;
        float Y = controller.getGameCharacter().getPlayerPos().y;
        //Do not try to understand this code
        //It has been engineered to not be understandable, but apply in this very specific scenario
        //We want you to find you own solutions for aiming :)
        float d = X - enemyX;
        controller.aim(90 + (d + d*d/5000f - 9.7f) * 1 / 16f * 0.95f, 0.69f);
        controller.shoot();
        if (Math.random() > 0.5){
            if (state.getTile(((int)X)/16 -1 , ((int)Y)/16 -1)!= null)
                controller.move(-16);
        }else{
            if (state.getTile(((int)X)/16 +1 , ((int)Y)/16 -1)!= null)
                controller.move(16);
        }
    }
}
