package com.gats.simulation.action;


/**
 * Super class for all {@link Action Actions}, that relate to a {@link com.gats.simulation.GameCharacter Character}
 */
public abstract class CharacterAction extends Action{
    private final int team;
    private final int character;


    /**
     * Super class for the Action of a Character
     *
     * @param delay     non-negative time-based offset to its parent in seconds
     * @param team      team index of the Character
     * @param character Characters index within its team
     */
    public CharacterAction(float delay, int team, int character) {
        super(delay);
        this.team = team;
        this.character = character;
    }

    /**
     * @return Characters index within its team
     */
    public int getCharacter() {
        return character;
    }

    /**
     * @return team index of the Character
     */
    public int getTeam() {
        return team;
    }

        @Override

    public String toString() {

       String output = super.toString();

       output += "Team: " + team;

       output += " ";

       output += "Character: " + character;

        output += " ";

       return output;

    }
}
