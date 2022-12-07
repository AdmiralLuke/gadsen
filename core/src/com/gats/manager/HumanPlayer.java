package com.gats.manager;

import com.badlogic.gdx.Input;
import com.gats.simulation.GameCharacterController;
import com.gats.simulation.GameState;

public class HumanPlayer extends Player{

    final  int KEY_CHARACTER_MOVE_LEFT = Input.Keys.A;
    final  int KEY_CHARACTER_MOVE_RIGHT = Input.Keys.D;
    final  int KEY_CHARACTER_SHOOT_ACTION = Input.Keys.SPACE;
    final  int KEY_CHARACTER_AIM_LEFT = Input.Keys.Q;
    final  int KEY_CHARACTER_AIM_RIGHT = Input.Keys.E;
    final  int KEY_CHARACTER_INCREASE_STRENGTH = Input.Keys.W;
    final  int KEY_CHARACTER_DECREASE_STRENGTH = Input.Keys.S;
    final int KEY_CHARACTER_CYCLE_WEAPON = Input.Keys.TAB;

    final int characterIncreaseStrength = Input.Keys.R;
    final int characterDecreaseStrength = Input.Keys.F;

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
        return "";
    }

    @Override
    protected void init(GameState state) {

    }

    /**
     *  Started den Zug des {@link HumanPlayer} und erlaubt es diesem mithilfe von Tasteneingaben, zu bewegen.
     *  Der Zug dauert {@link HumanPlayer#turnDuration} Sekunden, danach wird für
     *  {@link HumanPlayer#turnEndWaitTime} gewartet und dann die Methode beendet.
     * @param state Der {@link GameState Spielzustand} während des Zuges
     * @param controller Der {@link Controller Controller}, der zum Charakter gehört
     */

    @Override
    protected void executeTurn(GameState state, Controller controller) {
        turnInProgress =true;
        nanoStartTime = System.nanoTime();
        elapsedTime = 0;
        while(elapsedTime<nanoTurnDuration&& turnInProgress) {
           moveCharacter(controller);

           elapsedTime = System.nanoTime() - nanoStartTime;
       }
        endTurn();
    }

    public void processKeyDown(int keycode){

        switch(keycode){
            // Qund E für rotieren/zielen mit den Waffen
            case KEY_CHARACTER_AIM_LEFT:
                //currentPlayer.toggleAimLeft();
                break;
            case KEY_CHARACTER_AIM_RIGHT:
                break;
            case KEY_CHARACTER_MOVE_LEFT:
                break;
            case KEY_CHARACTER_MOVE_RIGHT:
                break;
            case KEY_CHARACTER_SHOOT_ACTION:
                break;
            case KEY_CHARACTER_CYCLE_WEAPON:
                break;
            case KEY_CHARACTER_INCREASE_STRENGTH:
                break;
            case KEY_CHARACTER_DECREASE_STRENGTH:
                break;
        }
    }
    public void processKeyUp(int keycode){


        switch(keycode){
            // Qund E für rotieren/zielen mit den Waffen
            case KEY_CHARACTER_AIM_LEFT:
                //currentPlayer.toggleAimLeft();
                break;
            case KEY_CHARACTER_AIM_RIGHT:
                break;
            case KEY_CHARACTER_MOVE_LEFT:
                break;
            case KEY_CHARACTER_MOVE_RIGHT:
                break;
            case KEY_CHARACTER_SHOOT_ACTION:
                break;
            case KEY_CHARACTER_CYCLE_WEAPON:
                break;
            case KEY_CHARACTER_INCREASE_STRENGTH:
                break;
            case KEY_CHARACTER_DECREASE_STRENGTH:
                break;
        }
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
     * @param controller {@link com.gats.manager.Controller Controller} für den Charakter, welcher am Zug ist.
     */
    protected void moveCharacter(Controller controller){
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
