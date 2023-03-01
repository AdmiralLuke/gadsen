package com.gats.simulation;

import com.badlogic.gdx.math.Vector2;
import com.gats.simulation.action.Action;
import com.gats.simulation.action.ActionLog;

/**
 * Provides an access-controlled interface to send commands to players
 * Ermöglicht die Kontrolle eines bestimmten Charakters.
 * Ist nur für einen einzelnen Zug gültig und deaktiviert sich nach Ende des aktuellen Zuges.
 */
public class GameCharacterController {
    private final GameCharacter gameCharacter;
    private final GameState state;

    private Action getHead(){
        return state.getSim().getActionLog().getRootAction(); //ToDo: verify correctness
    }

    private ActionLog endCommand(){
        return state.getSim().clearAndReturnActionLog();
    }

    protected GameCharacterController(GameCharacter gameCharacter, GameState state) {
        this.gameCharacter = gameCharacter;
        this.state = state;
    }

    /**
     * @return Den Charakter, welcher durch diesen Controller gesteuert wird.
     */
    public GameCharacter getGameCharacter() {
        return gameCharacter;
    }

    /**
     * Veranlasst den Charakter unter Aufwendung von Stamina einen dx Schritt zu tätigen.
     * Positive Werte bewegen den Charakter nach rechts, Negative nach links.
     */
    public ActionLog move(int dx) {
        gameCharacter.walk(dx, getHead());
        return endCommand();
    }

    public ActionLog aim(Vector2 angle, float strength) {
        gameCharacter.aim(angle,strength, getHead());
        return endCommand();
    }

    public ActionLog selectWeapon(WeaponType type) {
        gameCharacter.selectWeapon(type, getHead());
        return endCommand();
    }

    public ActionLog shoot() {
        gameCharacter.shoot(getHead());
        return endCommand();
    }

}
