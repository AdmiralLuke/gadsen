package com.gats.simulation.action;

import com.gats.simulation.GameCharacter;

/**
 * Beschreibt ein {@link Action Ereigniss}, bei dem die {@link GameCharacter Spielfigur} eine {@link com.gats.simulation.weapons.Weapon Waffe} benutzt
 */
public final class CharacterShootAction extends CharacterAction{

    public CharacterShootAction(int team, int character) {
        super(0, team, character);
    }

    @Override
    public String toString() {
        String output = "ShootAction: ";
            output +=super.toString();
            output+=" ";

            return output;
    }
}
