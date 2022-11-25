package com.gats.simulation;

import com.badlogic.gdx.math.Vector2;

/**
 * Beschreibt ein {@link Action Erigniss}, bei dem die {@link GameCharacter Spielfigur} mittels der Laufmechanik bewegt wird
 */
public final class CharacterMoveAction extends CharacterAction{

    private LinearPath path;
    private int team;
    private int character;

    CharacterMoveAction(Vector2 posBef, Vector2 posAft, int team, int character) {
        this.path = new LinearPath(posBef, posAft);
        this.team = team;
        this.character = character;
    }

    public int getCharacter() {
        return character;
    }

    public int getTeam() {
        return team;
    }

    public LinearPath getPath() {
        return path;
    }

}
