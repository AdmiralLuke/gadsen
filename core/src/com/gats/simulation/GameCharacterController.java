package com.gats.simulation;

/**
 * Provides an access-controlled interface to send commands to players
 *
 * Ermöglicht die Kontrolle eines bestimmten Charakters.
 * Ist nur für einen einzelnen Zug gültig und deaktiviert sich nach Ende des aktuellen Zuges.
 */
public class GameCharacterController {
    private GameCharacter gameCharacter;
    private GameState state;
    private boolean valid = true;

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
     * Veranlasst den Charakter unter aufwendung von Stamina einen Schritt nach Links zu tätigen
     */
    public void moveLeft() {
        // if (valid) gameCharacter.moveLeft(state);
    }


    /**
     * Veranlasst den Charakter unter aufwendung von Stamina einen Schritt nach Rechts zu tätigen
     */
    public void moveRight() {

        //if (valid) gameCharacter.moveLeft(state);
    }

    /**
     * Used to Invalidate this controller.
     * Ein Aufruf deaktiviert diesen Controller permanent.
     */
    public void invalidate(){
        valid = false;
    }
}
