package com.gats.simulation;

/**
 * Beschreibt ein {@link Action Ereigniss}, bei dem die {@link GameCharacter Spielfigur} eine {@link Weapon Waffe} benutzt
 */
public final class CharacterShootAction extends CharacterAction{

    public CharacterShootAction(int team, int character) {
        super(team, character, 0);
    }
}
