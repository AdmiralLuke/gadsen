package com.gats.simulation.action;

import com.badlogic.gdx.math.Vector2;
import com.gats.simulation.GameCharacter;
import com.gats.simulation.LinearPath;
import com.gats.simulation.Path;

/**
 * Beschreibt ein {@link Action Ereigniss}, bei dem die {@link GameCharacter Spielfigur} mittels der Laufmechanik bewegt wird
 */
public final class CharacterMoveAction extends CharacterAction{

    private final Path path;
    private final static float V = 10;
    private final float duration;

    /**
     * erstellt einen Linearen Pfad, wenn ein Charakter sich bewegt
     * @param posBef Anfangs Position
     * @param posAft End Position nach Movement
     * @param team Team-Nummer in welcher der Charakter ist
     * @param character Nummer im Team des Charakters
     * @param delay Wartezeit nach Ausführung
     */

    public CharacterMoveAction(Vector2 posBef, Vector2 posAft, int team, int character, float delay) {
        super(team, character, delay);
        LinearPath linPath = new LinearPath(posBef, posAft, V);
        this.path = linPath;
        this.duration = linPath.getEndTime();
    }


    /**
     * gibt den Pfad einer CharacterMoveAction zurück
     * @return Pfad den der Character zuruecklegt
     */
    public Path getPath() {
        return path;
    }

    public float getDuration() {
        return duration;
    }
}
