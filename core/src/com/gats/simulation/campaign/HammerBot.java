package com.gats.simulation.campaign;

import com.gats.manager.Controller;
import com.gats.simulation.GameState;
import com.gats.simulation.WeaponType;

public class HammerBot extends CampaignBot {

    @Override
    protected void executeTurn(GameState state, Controller controller) {
        super.executeTurn(state, controller);
        switch (turnCount) {
            case 0:
                controller.selectWeapon(WeaponType.MIOJLNIR);
            default:
                float enemyX = state.getCharacterFromTeams(0, 0).getPlayerPos().x;
                float X = controller.getGameCharacter().getPlayerPos().x;
                boolean left = enemyX < X;
                if (enemyX == X) controller.move(-1);
                controller.aim(state.getCharacterFromTeams(0, 0).getPlayerPos().sub(controller.getGameCharacter().getPlayerPos()), 1);
                controller.shoot();
                controller.move((int) Math.min(30, Math.max(392 - X, -30)));
                break;
        }
    }

}
