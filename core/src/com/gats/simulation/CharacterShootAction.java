package com.gats.simulation;

/**
 * Beschreibt ein {@link Action Ereigniss}, bei dem die {@link GameCharacter Spielfigur} eine {@link Weapon Waffe} benutzt
 */
public final class CharacterShootAction extends CharacterAction{

    public CharacterShootAction(int team, int character) {
        super(team, character, 0);
    }

    @Override
    public String toString() {
        String output = "ShootAction: ";
            output +=super.toString();
            output+=" ";

            return output;
    }
}
