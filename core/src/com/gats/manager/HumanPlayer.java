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
   private int turnDuration = 60;
   private long nanoTurnDuration = turnDuration *1000000000;
   private int turnEndWaitTime = 5;

   private long nanoStartTime;
   private long elapsedTime;
   private boolean turnInProgress;
    @Override
    protected String getName() {
        return null;
    }

    @Override
    protected void init(GameState state) {

    }

    /**
     *  Started den Zug des {@link HumanPlayer} und erlaubt es diesem mithilfe von Tasteneingaben, zu bewegen.
     *  Der Zug dauert {@link HumanPlayer#turnDuration} Sekunden, danach wird für
     *  {@link HumanPlayer#turnEndWaitTime} gewartet und dann die Methode beendet.
     * @param state Der {@link GameState Spielzustand} während des Zuges
     * @param characterController Der {@link GameCharacterController Controller}, Charakter gehört
     */

    @Override
    protected void executeTurn(GameState state, GameCharacterController characterController) {
        turnInProgress =true;
        nanoStartTime = System.nanoTime();
        elapsedTime = 0;
        while(elapsedTime<nanoTurnDuration&& turnInProgress) {
           moveCharacter(characterController);

           elapsedTime = System.nanoTime() - nanoStartTime;
       }
        endTurn();
    }

    /**
     * Setzt den {@link HumanPlayer} auf den Zustand vor dem Zug zurück.
     * Wartet für {@link HumanPlayer#turnEndWaitTime} Sekunden, sodass Aktionen, welche zum Ende des Zuges getätigt werden
     * um Inkonsistenzen zu vermeiden.
     */
    protected  void endTurn(){
        turnInProgress = false;
        nanoStartTime = System.nanoTime();
        resetControls();
      while(elapsedTime<turnEndWaitTime)  {
          elapsedTime = System.nanoTime()- nanoStartTime;
      }
      nanoStartTime = 0;
      elapsedTime = 0;
    }

    /**
     * Benutzt einen {@link GameCharacterController} um den dazugehörigen {@link com.gats.simulation.GameCharacter},
     * mithilfe der Eingaben durch {@link com.gats.ui.HudStage} zu bewegen.
     * @param characterController {@link com.gats.simulation.GameCharacterController} für den Charakter, welcher am Zug ist.
     */
    protected void moveCharacter(GameCharacterController characterController){
        //temporäre variable, Speed sollte woanders festgelegt werden
        int characterSpeed = 5;

        if(characterMoveLeftPressed){
            //characterController.move(-characterSpeed);

        } else if (characterMoveRightPressed) {
            //characterController.move(characterSpeed);

        } else if (characterShootPressed) {
          //  characterController.shoot();
            characterShootPressed = false;

        }

    }



    @Override
    protected PlayerType getType() {
        return PlayerType.Human;
    }

    /**
     * Setzt die boolean Werte, welche vom Input durch die {@link com.gats.ui.HudStage}
     * über set Methodenaufrufe verändert werden, auf false.
     */
    protected void resetControls(){
        characterMoveRightPressed = false;
        characterMoveLeftPressed = false;
        characterCycleWeapon = false;
        characterShootPressed = false;
        characterAimLeftPressed = false;
        characterAimRightPressed = false;
    }

}
