package com.gats.simulation.campaign;

import com.badlogic.gdx.math.Vector2;
import com.gats.manager.Controller;
import com.gats.simulation.GameState;
import com.gats.simulation.WeaponType;

public class LeftRightBot extends CampaignBot {

    @Override
    protected void executeTurn(GameState state, Controller controller) {
        super.executeTurn(state, controller);
        switch (turnCount){
            case 0:
                controller.selectWeapon(WeaponType.WATER_PISTOL);
            default:
                float enemyX = state.getCharacterFromTeams(0,0).getPlayerPos().x;
                float X = controller.getGameCharacter().getPlayerPos().x;
                boolean left = enemyX < X;
                if (enemyX == X) controller.move(-1);
                controller.aim(new Vector2(left?-1:1, 0f), 1);
                controller.shoot();
                break;
        }
    }
}
