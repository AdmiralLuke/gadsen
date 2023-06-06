package com.gats.simulation.campaign;

import com.badlogic.gdx.math.Vector2;
import com.gats.manager.Controller;
import com.gats.simulation.GameState;
import com.gats.simulation.WeaponType;

public class Level5_1Bot extends CampaignBot {


    @Override
    protected void executeTurn(GameState state, Controller controller) {
        super.executeTurn(state, controller);
        if (turnCount == 0) {
            controller.selectWeapon(WeaponType.MIOJLNIR);
        }
        Vector2 pos = state.getCharacterFromTeams(1, 0).getPlayerPos();
        Vector2 enemyPos = state.getCharacterFromTeams(0, 0).getPlayerPos();
        controller.aim(enemyPos.sub(pos), 1f);
        controller.shoot();
    }
}
