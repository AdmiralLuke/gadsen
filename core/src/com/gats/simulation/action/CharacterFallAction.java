package com.gats.simulation.action;

import com.badlogic.gdx.math.Vector2;
import com.gats.simulation.LinearPath;
import com.gats.simulation.Path;


/**
 * Type of {@link CharacterAction} created, whenever a Character is falling
 * ToDo: integrate into a CharacterMoveAction and decide animation dynamically in character Move
 */
public class CharacterFallAction extends CharacterAction {
    private final LinearPath path;
    private final float duration;

    /**
     * Stores the event of a Character falling
     * @param delay     non-negative time-based offset to its parent in seconds
     * @param team      team index of the Character
     * @param character Characters index within its team
     * @param posBef    start-position of the fall in world-coordinates
     * @param posAft    end-position of the fall in world-coordinates
     */
    public CharacterFallAction(float delay, int team, int character, Vector2 posBef, Vector2 posAft) {
        super(delay, team, character);
        this.path = new LinearPath(posBef, posAft, 40f);
        this.duration = path.getDuration();
    }

    /**
     * @return A {@link Path} that returns the Characters position in world-coordinates for every timestamp between 0 and duration
     */
    public Path getPath() {
        return path;
    }

    /**
     * @return The duration of the event in seconds
     */
    public float getDuration() {
        return duration;
    }
    @Override
    public String toString() {

       String output = super.toString();

       output += path.toString();

       output += " ";

       return output + "[FALL]";

    }

}
