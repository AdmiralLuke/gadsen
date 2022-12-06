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

   //amount of time in seconds, the turn of the human player will take
    //if the time limit is reached, the execute turn will wait for turnOverhead seconds
    // to make sure everything is calculated and no GameState inconsistency is created
   private int turnDuration;
   private int turnOverhead;
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
            characterMoveLeftPressed = false;

        } else if (characterMoveRightPressed) {
            characterController.moveRight();
            characterMoveRightPressed = false;

        } else if (characterShootPressed) {
          //  characterController.shoot();
            characterShootPressed = false;

        }

    }



    @Override
    protected PlayerType getType() {
        return PlayerType.Human;
    }

}
