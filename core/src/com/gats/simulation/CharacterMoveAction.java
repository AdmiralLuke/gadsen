package com.gats.simulation;

import com.badlogic.gdx.math.Vector2;
import com.gats.simulation.*;

/**
 * Beschreibt ein {@link Action Erigniss}, bei dem die {@link GameCharacter Spielfigur} mittels der Laufmechanik bewegt wird
 */
public final class CharacterMoveAction extends CharacterAction{

    private LinearPath path;
    private static double v = 0.0001;


    /**
     * erstellt einen Lienaren Pfad, wenn ein Charakter sich bewegt
     * @param posBef Anfangs Position
     * @param posAft End Position nach Movement
     * @param team Team-Nummer in der der Charakter ist
     * @param character Nummer im Team des Charakters
     * @param delay Wartezeit nach Ausführung
     */
    CharacterMoveAction(Vector2 posBef, Vector2 posAft, int team, int character, long delay) {
        super(team, character, delay);
        this.path = new LinearPath(posBef, posAft);
    }


    /**
     * gibt den Pfad einer CharacterMoveAction zurück
     * @return Linearer Pfad
     */
    public LinearPath getPath() {
        return path;
    }

}
