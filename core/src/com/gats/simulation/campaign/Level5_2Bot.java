package com.gats.simulation.campaign;

import com.gats.manager.Controller;
import com.gats.simulation.GameCharacter;
import com.gats.simulation.GameState;
import com.gats.simulation.WeaponType;

public class Level5_2Bot extends CampaignBot {


    @Override
    protected void executeTurn(GameState state, Controller controller) {
        super.executeTurn(state, controller);
        if (turnCount < 2) {
            controller.selectWeapon(WeaponType.WATER_PISTOL);
        }
        int target = state.getCharacterFromTeams(0, 0).getHealth() > 0 ? 0 : 1;
        float enemyX = state.getCharacterFromTeams(0, target).getPlayerPos().x;
        GameCharacter curCharacter = controller.getGameCharacter();
        float X = curCharacter.getPlayerPos().x;
        float Y = curCharacter.getPlayerPos().y;
        float d = X - enemyX;
        //Do not try to understand this code
        //It has been engineered to not be understandable, but apply in this very specific scenario
        //We want you to find you own solutions for aiming :)
        //(Like seriously don't. I wrote this code, and even I do not fully understand it myself.)
        controller.aim(90 + Math.signum(d) * (Math.abs(d)*0.99f + d*d/3000f - 20.0f + Math.max(0, Math.abs(d) - 440)) * 1 / 16f * 0.94f, 0.69f + Math.max(0, Math.abs(d) - 530)/2000f);
        controller.shoot();
        if (rnd.nextFloat() > 0.5f) {
            if (state.getTile(((int) X) / 16 - 1, ((int) Y) / 16 - 1) != null)
                controller.move(-16);
        } else {
            if (state.getTile(((int) X) / 16 + 1, ((int) Y) / 16 - 1) != null)
                controller.move(16);
        }

    }
}