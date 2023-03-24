package com.gats.simulation.action;

/**
 * Special type of {@link Action} that indicates the start of a new turn. May replace an {@link InitAction} as root of an {@link ActionLog}.
 */
public class TurnStartAction extends CharacterAction {

    /**
     * Stores the event of a new turn beginning. Declares which Character may execute commands during the new turn.
     *
     * @param delay     non-negative time-based offset to its parent in seconds
     * @param team      team index of the Character
     * @param character Characters index within its team
     */
    public TurnStartAction(long delay, int team, int character) {
        super(delay, team, character);
    }


    @Override
    public String toString() {
        return "TurnStart: " + getTeam()+","+getCharacter();
    }


}
