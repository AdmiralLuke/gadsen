package com.gats.manager;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.gats.simulation.GameState;
import com.gats.simulation.WeaponType;
import com.gats.ui.hud.UiMessenger;

import java.time.Clock;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Arrays;

public class HumanPlayer extends Player {

    private static final float NO_TICK = -10000.0f;

    enum Key {
        KEY_CHARACTER_MOVE_LEFT,
        KEY_CHARACTER_MOVE_RIGHT,
        KEY_CHARACTER_SHOOT_ACTION,
        KEY_CHARACTER_AIM_LEFT,
        KEY_CHARACTER_AIM_RIGHT,
        KEY_CHARACTER_INCREASE_STRENGTH,
        KEY_CHARACTER_DECREASE_STRENGTH,
        KEY_CHARACTER_CYCLE_WEAPON,

        KEY_CHARACTER_END_TURN
    }


    final int KEY_CHARACTER_MOVE_LEFT = Input.Keys.A;
    final int KEY_CHARACTER_MOVE_RIGHT = Input.Keys.D;
    final int KEY_CHARACTER_SHOOT_ACTION = Input.Keys.SPACE;
    final int KEY_CHARACTER_AIM_LEFT = Input.Keys.Q;
    final int KEY_CHARACTER_AIM_RIGHT = Input.Keys.E;
    final int KEY_CHARACTER_INCREASE_STRENGTH = Input.Keys.W;
    final int KEY_CHARACTER_DECREASE_STRENGTH = Input.Keys.S;
    final int KEY_CHARACTER_CYCLE_WEAPON = Input.Keys.TAB;

    final int KEY_CHARACTER_END_TURN = Input.Keys.X;


    private float[] lastTick = new float[Key.values().length];

    private static final float[] tickspeed = new float[Key.values().length]; // in Hz

    {
        tickspeed[Key.KEY_CHARACTER_MOVE_LEFT.ordinal()] = 0.1f;
        tickspeed[Key.KEY_CHARACTER_MOVE_RIGHT.ordinal()] = 0.1f;
        tickspeed[Key.KEY_CHARACTER_SHOOT_ACTION.ordinal()] = 0.1f;
        tickspeed[Key.KEY_CHARACTER_AIM_LEFT.ordinal()] = 0.1f;
        tickspeed[Key.KEY_CHARACTER_AIM_RIGHT.ordinal()] = 0.1f;
        tickspeed[Key.KEY_CHARACTER_INCREASE_STRENGTH.ordinal()] = 0.1f;
        tickspeed[Key.KEY_CHARACTER_DECREASE_STRENGTH.ordinal()] = 0.1f;
        tickspeed[Key.KEY_CHARACTER_CYCLE_WEAPON.ordinal()] = 0.1f;

        tickspeed[Key.KEY_CHARACTER_END_TURN.ordinal()] = 0.1f;
    }

    //amount of time in seconds, the turn of the human player will take
    //if the time limit is reached, the execute turn will wait for turnOverhead seconds
    // to make sure everything is calculated and no GameState inconsistency is created
    private int turnDuration = 20;
    private int turnEndWaitTime = 5;


    private int angle = 0;

    private float strength = 0.5f;

    private Queue<WeaponType> weaponTypeStack = new LinkedList<>();

    {
        weaponTypeStack.addAll(Arrays.asList(WeaponType.values()));
    }

    private boolean turnInProgress;
    private GameState state;
    private Controller controller;

    private UiMessenger uiMessenger;
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
        for (int i = 0; i < lastTick.length; i++) lastTick[i] = NO_TICK;

        //Todo add 5 seconds time  between turns
        //setup timer for updating the ui Time
      if(uiMessenger!=null) {
          uiMessenger.setTurnTimeLeft(turnDuration);
          uiMessenger.startTurnTimer();

      }
        synchronized (this) {
            try {
                this.wait(turnDuration* 1000L);
                uiMessenger.stopTurnTimer();
            } catch (InterruptedException ignored) {
//                System.out.println("Turn has been ended preemptively");

            }
        }
    }

    /**
     * Ends the current turn of the player preemptively.
     * Callen when pressing {@link HumanPlayer#KEY_CHARACTER_END_TURN}.
     * notifies itself, so the wait will end.
     */
    protected void endCurrentTurn() {
        synchronized (this) {
            this.notify();
        }
    }

    public void processKeyDown(int keycode) {
//        System.out.println("Received Key: " + keycode);
        switch (keycode) {
            case KEY_CHARACTER_AIM_LEFT:
                lastTick[Key.KEY_CHARACTER_AIM_LEFT.ordinal()] = -tickspeed[Key.KEY_CHARACTER_AIM_LEFT.ordinal()];
                execute(Key.KEY_CHARACTER_AIM_LEFT);
                break;
            case KEY_CHARACTER_AIM_RIGHT:
                lastTick[Key.KEY_CHARACTER_AIM_RIGHT.ordinal()] = -tickspeed[Key.KEY_CHARACTER_AIM_RIGHT.ordinal()];
                execute(Key.KEY_CHARACTER_AIM_RIGHT);
                break;
            case KEY_CHARACTER_MOVE_LEFT:
                lastTick[Key.KEY_CHARACTER_MOVE_LEFT.ordinal()] = -tickspeed[Key.KEY_CHARACTER_MOVE_LEFT.ordinal()];
                execute(Key.KEY_CHARACTER_MOVE_LEFT);
                break;
            case KEY_CHARACTER_MOVE_RIGHT:
                lastTick[Key.KEY_CHARACTER_MOVE_RIGHT.ordinal()] = -tickspeed[Key.KEY_CHARACTER_MOVE_RIGHT.ordinal()];
                execute(Key.KEY_CHARACTER_MOVE_RIGHT);
                break;
            case KEY_CHARACTER_SHOOT_ACTION:
                //lastTick[Key.KEY_CHARACTER_SHOOT_ACTION.ordinal()] = true;
                execute(Key.KEY_CHARACTER_SHOOT_ACTION);
                break;
            case KEY_CHARACTER_CYCLE_WEAPON:
                //lastTick[Key.KEY_CHARACTER_CYCLE_WEAPON.ordinal()] = true;
                execute(Key.KEY_CHARACTER_CYCLE_WEAPON);
                break;
            case KEY_CHARACTER_INCREASE_STRENGTH:
                lastTick[Key.KEY_CHARACTER_INCREASE_STRENGTH.ordinal()] = -tickspeed[Key.KEY_CHARACTER_INCREASE_STRENGTH.ordinal()];
                execute(Key.KEY_CHARACTER_INCREASE_STRENGTH);
                break;
            case KEY_CHARACTER_DECREASE_STRENGTH:
                lastTick[Key.KEY_CHARACTER_DECREASE_STRENGTH.ordinal()] = -tickspeed[Key.KEY_CHARACTER_DECREASE_STRENGTH.ordinal()];
                execute(Key.KEY_CHARACTER_DECREASE_STRENGTH);
                break;
            case KEY_CHARACTER_END_TURN:
                //lastTick[Key.KEY_CHARACTER_END_TURN.ordinal()] = true;
                execute(Key.KEY_CHARACTER_END_TURN);
                break;
        }
    }

    private void execute(Key key) {
//        System.out.println("Commanding Controller: " + controller);
        switch (key) {
            // Qund E für rotieren/zielen mit den Waffen
            case KEY_CHARACTER_AIM_LEFT:
                angle += 1;
                angle = angle % 360;
                controller.aim(angle, strength);
                break;
            case KEY_CHARACTER_AIM_RIGHT:
                angle -= 1;
                if (angle < 0) angle += 360;
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
            case KEY_CHARACTER_DECREASE_STRENGTH:
                strength -= 0.025;
                if (strength < 0) strength = 0;
                controller.aim(angle, strength);
                break;
            case KEY_CHARACTER_INCREASE_STRENGTH:
                strength += 0.025;
                if (strength > 1) strength = 1;
                controller.aim(angle, strength);
                break;
            case KEY_CHARACTER_END_TURN:
                this.endCurrentTurn();
                break;
        }
    }

    public void tick(float delta) {
        for (Key key : Key.values()) {
            int index = key.ordinal();
            if (lastTick[index] > (NO_TICK/2)) {
                lastTick[index] += delta;
                while (lastTick[index] >= 0.0f) {
                    lastTick[index] -= tickspeed[index];
                    execute(key);
                }
            }
        }
        //ToDo implement holding keys
    }

    public void aim(int angle,float strength){
        this.angle = angle;
        this.strength = strength;
       controller.aim(angle,strength);
    }

    /**
     * Makes the controller aim to the provided position in regards to the center of the Character.
     * @param target position to aim towards.
     */
    public void aimToVector(Vector2 target){
      Vector2 playerPos = controller.getGameCharacter().getPlayerPos();
      //Todo get center from character sprite?
        //add characterCenteroffset currently center is at(8,8) because 16x16 sprite size
      playerPos.add(new Vector2(8.5f,8));
      target.sub(playerPos);

      //maximum length before strength begins to decrease
      float maxStrengthDistance = 64; //64=4*16= 4tiles derzeit
      float currentDistance = target.len();
      currentDistance = currentDistance>maxStrengthDistance?maxStrengthDistance:currentDistance;
     //call aim with the targetAngle and strength ratio
     this.aim((int) target.angleDeg(),currentDistance/maxStrengthDistance);
    }

    public void processKeyUp(int keycode) {


        switch (keycode) {
            // Qund E für rotieren/zielen mit den Waffen
            case KEY_CHARACTER_AIM_LEFT:
                lastTick[Key.KEY_CHARACTER_AIM_LEFT.ordinal()] = NO_TICK;
                //currentPlayer.toggleAimLeft();
                break;
            case KEY_CHARACTER_AIM_RIGHT:
                lastTick[Key.KEY_CHARACTER_AIM_RIGHT.ordinal()] = NO_TICK;
                break;
            case KEY_CHARACTER_MOVE_LEFT:
                lastTick[Key.KEY_CHARACTER_MOVE_LEFT.ordinal()] = NO_TICK;
                break;
            case KEY_CHARACTER_MOVE_RIGHT:
                lastTick[Key.KEY_CHARACTER_MOVE_RIGHT.ordinal()] = NO_TICK;
                break;
            case KEY_CHARACTER_SHOOT_ACTION:
                lastTick[Key.KEY_CHARACTER_SHOOT_ACTION.ordinal()] = NO_TICK;
                break;
            case KEY_CHARACTER_CYCLE_WEAPON:
                lastTick[Key.KEY_CHARACTER_CYCLE_WEAPON.ordinal()] = NO_TICK;
                break;
            case KEY_CHARACTER_INCREASE_STRENGTH:
                lastTick[Key.KEY_CHARACTER_INCREASE_STRENGTH.ordinal()] = NO_TICK;
                break;
            case KEY_CHARACTER_DECREASE_STRENGTH:
                lastTick[Key.KEY_CHARACTER_DECREASE_STRENGTH.ordinal()] = NO_TICK;
                break;
        }
    }

    @Override
    protected PlayerType getType() {
        return PlayerType.Human;
    }

    public void setUiMessenger(UiMessenger uiMessenger){
       this.uiMessenger = uiMessenger;
    }
}
