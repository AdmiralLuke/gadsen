package com.gats.simulation;

import com.badlogic.gdx.math.Vector2;

/**
 * Provides an access-controlled interface to send commands to players
 *
 * Ermöglicht die Kontrolle eines bestimmten Charakters.
 * Ist nur für einen einzelnen Zug gültig und deaktiviert sich nach Ende des aktuellen Zuges.
 */
public class GameCharacterController {
    private GameCharacter gameCharacter;
    private GameState state;
    private Vector2 aim_angle = new Vector2(0, 0);
    private float aim_strength = 0;

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
    public void move(int dx) {
        gameCharacter.move(dx);
    }

    public void aim(Vector2 angle, float strength) {
        this.aim_angle = angle;
        this.aim_strength = strength;
    }

    public void selectWeapon(WeaponType type) {
        gameCharacter.selectWeapon(type);
    }

    public void shoot() {
        gameCharacter.shoot(aim_angle, aim_strength);
    }

}
