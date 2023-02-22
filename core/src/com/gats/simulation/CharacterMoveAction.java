package com.gats.simulation;

import com.badlogic.gdx.math.Vector2;

/**
 * Beschreibt ein {@link Action Ereigniss}, bei dem die {@link GameCharacter Spielfigur} mittels der Laufmechanik bewegt wird
 */
public final class CharacterMoveAction extends CharacterAction{

    private Path path;
    private static double v = 0.0001;
    private float duration;

    /**
     * erstellt einen Linearen Pfad, wenn ein Charakter sich bewegt
     * @param posBef Anfangs Position
     * @param posAft End Position nach Movement
     * @param team Team-Nummer in welcher der Charakter ist
     * @param character Nummer im Team des Charakters
     * @param delay Wartezeit nach Ausführung
     */

    CharacterMoveAction(Vector2 posBef, Vector2 posAft, int team, int character, float delay) {
        super(team, character, delay);
        LinearPath linPath = new LinearPath(posBef, posAft, 1f);
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

    @Override
    public String toString() {
        String output=  super.toString();

        output += path.toString();
        output += " ";
        return output;
    }
}
