package com.gats.simulation;


/**
 * Oberklasse für alle {@link Action Erignisse}, die sich auf eine Spielfigur beziehen
 */
abstract class CharacterAction extends Action{
    private int team;
    private int character;


    /**
     * Oberklasse für Aktionen eines Characters
     * @param team Team-Nummer des Characters
     * @param character Nummer im Team
     * @param delay Wartezeit nach Aktion
     */
    public CharacterAction(int team, int character, long delay) {
        super(delay);
        this.team = team;
        this.character = character;
    }

    public int getCharacter() {
        return character;
    }

    public int getTeam() {
        return team;
    }
}
