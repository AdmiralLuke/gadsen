package com.gats.simulation;

import com.badlogic.gdx.math.Vector2;
import com.gats.simulation.action.Action;
import com.gats.simulation.action.ActionLog;
import com.gats.simulation.action.CharacterAimAction;

/**
 * Provides an access-controlled interface to send commands to players.
 * Allows Control over the corresponding Character
 * Is only active for a single turn and will be permanently deactivated afterward.
 */
public class GameCharacterController {
    private final GameCharacter gameCharacter;
    private final GameState state;
    boolean active = true;

    /**
     * Returns the root of the ActionLog that is currently recording in the simulation instance.
     */
    private Action getRoot(){
        return state.getSim().getActionLog().getRootAction();
    }

    /**
     * Signals the current command as completed and will reset the ActionLog for the next command
     * @return The ActionLog produced by the previously executed command
     */
    private ActionLog endCommand(){
        return state.getSim().clearAndReturnActionLog();
    }

    /**
     *
     * @param gameCharacter the Character this Controller will grant access to
     * @param state         the current state of the game
     */
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
     * Makes the Character walk |dx| steps into the direction specified by the sign of dx, where a positive value
     * indicates walking to the right and a negative value indicates walking to the left.
     * The Character may take fewer steps, if he collides with a wall or runs out of stamina, while walking.
     * If the Character encounters a gap, where he can fall down, he will do so and continue his remaining movement after the fall.
     *
     * @param dx   direction and amount of steps to take
     * @return the ActionLog produced when executing this command
     */
    public ActionLog walk(int dx) {
        gameCharacter.walk(dx, getRoot());
        return endCommand();
    }

    /**
     * Sets the aiming-attributes of this Character. Will normalize the direction and confine the strength to the interval [0.0, 1.0].
     * If the direction is a zero-vector, aiming straight up (direction of x=0 and y=1) will be assumed.
     *
     * @param angle    direction this Character is supposed to aim in
     * @param strength strength of the shot between 0.0 and 1.0
     * @return the ActionLog produced when executing this command
     */
    public ActionLog aim(Vector2 angle, float strength) {
        gameCharacter.aim(angle,strength, getRoot());
        return endCommand();
    }
    /**
     * Makes the Character equip the specified weapon.
     *
     * @param type Weapon-type as Enum
     * @return the ActionLog produced when executing this command
     */
    public ActionLog selectWeapon(WeaponType type) {
        gameCharacter.selectWeapon(type, getRoot());
        return endCommand();
    }
    /**
     * Makes the Character shoot its equipped {@link com.gats.simulation.weapons.Weapon}.
     * Will only trigger for the first Weapon each turn, that can currently be used by this Character
     *
     * @return the ActionLog produced when executing this command
     */
    public ActionLog shoot() {
        gameCharacter.shoot(getRoot());
        return endCommand();
    }

    public void deactivate() {
        active = false;
    }

    public boolean isActive() {
        return active;
    }
}
