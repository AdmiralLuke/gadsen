package com.gats.manager;

import com.gats.simulation.GameCharacterController;
import com.gats.simulation.GameState;

public class HumanPlayer extends Player{

   private boolean characterMoveLeftPressed = false;
   private boolean characterMoveRightPressed = false;
   private boolean characterShootPressed = false;
   private boolean characterAimLeftPressed = false;
   private boolean characterAimRightPressed = false;
   private boolean characterCycleWeapon = false;
    @Override
    protected String getName() {
        return null;
    }

    @Override
    protected void init(GameState state) {

    }

    @Override
    protected void executeTurn(GameState state, GameCharacterController characterController) {
        //while(true){
        moveCharacter(characterController);
        // }
    }

    /**
     * Uses the supplied {@link GameCharacterController} to move the respective {@link com.gats.simulation.GameCharacter} with the values supplied by the {@link com.gats.ui.HudStage}, wich
     * stored in the {@link HumanPlayer}
     * @param characterController
     */
    protected void moveCharacter(GameCharacterController characterController){
        if(characterMoveLeftPressed){
            characterController.moveLeft();

        } else if (characterMoveRightPressed) {
            characterController.moveRight();

        } else if (characterShootPressed) {
          //  characterController.shoot();

        }

    }



    @Override
    protected PlayerType getType() {
        return PlayerType.Human;
    }

}
