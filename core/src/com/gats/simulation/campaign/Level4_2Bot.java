package com.gats.simulation.campaign;

import com.gats.manager.Controller;
import com.gats.simulation.GameState;
import com.gats.simulation.WeaponType;

public class Level4_2Bot extends CampaignBot {


    @Override
    protected void executeTurn(GameState state, Controller controller) {
        super.executeTurn(state, controller);
        if (turnCount == 0) {
            controller.selectWeapon(WeaponType.WATER_PISTOL);
        }
        float enemyX = state.getCharacterFromTeams(0, 0).getPlayerPos().x;
        float X = controller.getGameCharacter().getPlayerPos().x;
        float Y = controller.getGameCharacter().getPlayerPos().y;
        float d = X - enemyX;
        //Do not try to understand this code
        //It has been engineered to not be understandable, but apply in this very specific scenario
        //We want you to find your own solutions for aiming :)
        //(Like seriously don't. I wrote this code, and even I do not fully understand it myself)
        controller.aim(90 + (d*0.99f + d*d/3000f - 20.0f) * 1 / 16f * 0.94f, 0.69f);
        controller.shoot();
        if (rnd.nextFloat() > 0.5f){
            if (state.getTile(((int)X)/16 -1 , ((int)Y)/16 -1)!= null)
                controller.move(-16);
        }else{
            if (state.getTile(((int)X)/16 +1 , ((int)Y)/16 -1)!= null)
                controller.move(16);
        }
    }
}
