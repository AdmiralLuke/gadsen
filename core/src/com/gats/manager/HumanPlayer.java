package com.gats.manager;

import com.badlogic.gdx.Input;
import com.gats.simulation.GameCharacterController;
import com.gats.simulation.GameState;
import com.gats.simulation.WeaponType;
import com.sun.jmx.remote.internal.ArrayQueue;
import org.lwjgl.Sys;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class HumanPlayer extends Player {

    enum Key {
        KEY_CHARACTER_MOVE_LEFT,
        KEY_CHARACTER_MOVE_RIGHT,
        KEY_CHARACTER_SHOOT_ACTION,
        KEY_CHARACTER_AIM_LEFT,
        KEY_CHARACTER_AIM_RIGHT,
        KEY_CHARACTER_INCREASE_STRENGTH,
        KEY_CHARACTER_DECREASE_STRENGTH,
        KEY_CHARACTER_CYCLE_WEAPON
    }


        final int KEY_CHARACTER_MOVE_LEFT = Input.Keys.A;
        final int KEY_CHARACTER_MOVE_RIGHT = Input.Keys.D;
        final int KEY_CHARACTER_SHOOT_ACTION = Input.Keys.SPACE;
        final int KEY_CHARACTER_AIM_LEFT = Input.Keys.Q;
        final int KEY_CHARACTER_AIM_RIGHT = Input.Keys.E;
        final int KEY_CHARACTER_INCREASE_STRENGTH = Input.Keys.W;
        final int KEY_CHARACTER_DECREASE_STRENGTH = Input.Keys.S;
        final int KEY_CHARACTER_CYCLE_WEAPON = Input.Keys.TAB;



    private boolean[] isDown = new boolean[Key.values().length];

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
    private long nanoTurnDuration = turnDuration * 1000000000;
    private int turnEndWaitTime = 5;

    private long nanoStartTime;
    private long elapsedTime;

    private int angle = 0;

    private float strength = 0.5f;

    private Queue<WeaponType> weaponTypeStack= new LinkedList<>();
    {
        weaponTypeStack.add(WeaponType.COOKIE);
        weaponTypeStack.add(WeaponType.SUGAR_CANE);

        WeaponType nextType = weaponTypeStack.poll();
        weaponTypeStack.add(nextType);
    }

    private boolean turnInProgress;
    private GameState state;
    private Controller controller;

    @Override
    public String getName() {
        return "Human";
    }

    @Override
    protected void init(GameState state) {

    }

    /**
     * Started den Zug des {@link HumanPlayer} und erlaubt es diesem mithilfe von Tasteneingaben, zu bewegen.
     * Der Zug dauert {@link HumanPlayer#turnDuration} Sekunden, danach wird für
     * {@link HumanPlayer#turnEndWaitTime} gewartet und dann die Methode beendet.
     *
     * @param state      Der {@link GameState Spielzustand} während des Zuges
     * @param controller Der {@link Controller Controller}, der zum Charakter gehört
     */

    @Override
    protected void executeTurn(GameState state, Controller controller) {
        this.state = state;
        this.controller = controller;
        synchronized (this){
            try {
                wait(5000);
            } catch (InterruptedException ignored) {
                //Turn has been ended preemptively
            }
        }
    }

    public void processKeyDown(int keycode) {
        System.out.println("Received Key: " + keycode);
        switch (keycode) {
            // Qund E für rotieren/zielen mit den Waffen
            case KEY_CHARACTER_AIM_LEFT:
                isDown[Key.KEY_CHARACTER_AIM_LEFT.ordinal()] = true;
                execute(Key.KEY_CHARACTER_AIM_LEFT);
                //currentPlayer.toggleAimLeft();
                break;
            case KEY_CHARACTER_AIM_RIGHT:
                isDown[Key.KEY_CHARACTER_AIM_RIGHT.ordinal()] = true;
                execute(Key.KEY_CHARACTER_AIM_RIGHT);
                break;
            case KEY_CHARACTER_MOVE_LEFT:
                isDown[Key.KEY_CHARACTER_MOVE_LEFT.ordinal()] = true;
                execute(Key.KEY_CHARACTER_MOVE_LEFT);
                break;
            case KEY_CHARACTER_MOVE_RIGHT:
                isDown[Key.KEY_CHARACTER_MOVE_RIGHT.ordinal()] = true;
                execute(Key.KEY_CHARACTER_MOVE_RIGHT);
                break;
            case KEY_CHARACTER_SHOOT_ACTION:
                isDown[Key.KEY_CHARACTER_SHOOT_ACTION.ordinal()] = true;
                execute(Key.KEY_CHARACTER_SHOOT_ACTION);
                break;
            case KEY_CHARACTER_CYCLE_WEAPON:
                isDown[Key.KEY_CHARACTER_CYCLE_WEAPON.ordinal()] = true;
                execute(Key.KEY_CHARACTER_CYCLE_WEAPON);
                break;
            case KEY_CHARACTER_INCREASE_STRENGTH:
                isDown[Key.KEY_CHARACTER_INCREASE_STRENGTH.ordinal()] = true;
                execute(Key.KEY_CHARACTER_INCREASE_STRENGTH);
                break;
            case KEY_CHARACTER_DECREASE_STRENGTH:
                isDown[Key.KEY_CHARACTER_DECREASE_STRENGTH.ordinal()] = true;
                execute(Key.KEY_CHARACTER_DECREASE_STRENGTH);
                break;
        }
    }

    private void execute(Key key) {
        switch (key) {
            // Qund E für rotieren/zielen mit den Waffen
            case KEY_CHARACTER_AIM_LEFT:
                angle -= 5;
                if(angle<0) angle+=360;
                controller.aim(angle, strength);
                break;
            case KEY_CHARACTER_AIM_RIGHT:
                angle += 5;
                angle = angle % 360;
                controller.aim(angle, strength);
                break;
            case KEY_CHARACTER_MOVE_LEFT:
                controller.move(-1);
                break;
            case KEY_CHARACTER_MOVE_RIGHT:
                controller.move(1);

                break;
            case KEY_CHARACTER_SHOOT_ACTION:
                controller.shoot();
                break;
            case KEY_CHARACTER_CYCLE_WEAPON:
                WeaponType nextType = weaponTypeStack.poll();
                weaponTypeStack.add(nextType);
                controller.selectWeapon(nextType);
                break;
            case KEY_CHARACTER_INCREASE_STRENGTH:
                strength -= 0.05;
                if (strength <0) strength = 0;
                controller.aim(angle, strength);
                break;
            case KEY_CHARACTER_DECREASE_STRENGTH:
                strength += 0.05;
                if (strength >1) strength = 1;
                controller.aim(angle, strength);
                break;
        }
    }

    public void tick() {
        //ToDo implement holding keys
    }


    public void processKeyUp(int keycode) {



        switch (keycode) {
            // Qund E für rotieren/zielen mit den Waffen
            case KEY_CHARACTER_AIM_LEFT:
                isDown[Key.KEY_CHARACTER_AIM_LEFT.ordinal()] = false;
                //currentPlayer.toggleAimLeft();
                break;
            case KEY_CHARACTER_AIM_RIGHT:
                isDown[Key.KEY_CHARACTER_AIM_RIGHT.ordinal()] = false;
                break;
            case KEY_CHARACTER_MOVE_LEFT:
                isDown[Key.KEY_CHARACTER_MOVE_LEFT.ordinal()] = false;
                break;
            case KEY_CHARACTER_MOVE_RIGHT:
                isDown[Key.KEY_CHARACTER_MOVE_RIGHT.ordinal()] = false;
                break;
            case KEY_CHARACTER_SHOOT_ACTION:
                isDown[Key.KEY_CHARACTER_SHOOT_ACTION.ordinal()] = false;
                break;
            case KEY_CHARACTER_CYCLE_WEAPON:
                isDown[Key.KEY_CHARACTER_CYCLE_WEAPON.ordinal()] = false;
                break;
            case KEY_CHARACTER_INCREASE_STRENGTH:
                isDown[Key.KEY_CHARACTER_INCREASE_STRENGTH.ordinal()] = false;
                break;
            case KEY_CHARACTER_DECREASE_STRENGTH:
                isDown[Key.KEY_CHARACTER_DECREASE_STRENGTH.ordinal()] = false;
                break;
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
    protected void resetControls() {
        characterMoveRightPressed = false;
        characterMoveLeftPressed = false;
        characterCycleWeapon = false;
        characterShootPressed = false;
        characterAimLeftPressed = false;
        characterAimRightPressed = false;
    }

}
