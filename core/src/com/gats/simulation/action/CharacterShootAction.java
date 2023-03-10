package com.gats.simulation.action;

import com.gats.simulation.GameCharacter;
import com.gats.simulation.Weapon;

/**
 * Beschreibt ein {@link Action Ereigniss}, bei dem die {@link GameCharacter Spielfigur} eine {@link Weapon Waffe} benutzt
 */
public final class CharacterShootAction extends CharacterAction{

    public CharacterShootAction(int team, int character) {
        super(0, team, character);
    }
}
