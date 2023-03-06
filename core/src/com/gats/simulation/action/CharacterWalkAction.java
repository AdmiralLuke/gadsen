package com.gats.simulation.action;

import com.badlogic.gdx.math.Vector2;
import com.gats.simulation.LinearPath;
import com.gats.simulation.Path;


/**
 * Special type of {@link CharacterAction} created, whenever a Character is moving as the result of a move-command
 */
public final class CharacterWalkAction extends CharacterAction{

    private final Path path;

    /**
     * Base walking speed of all characters in world-coordinates per second
     */
    private final static float V = 10;
    private final float duration;

    /**
     * Stores the event of a Character walking
     *
     * @param delay     non-negative time-based offset to its parent in seconds
     * @param team      team index of the Character
     * @param character Characters index within its team
     * @param posBef    start-position of the character in world-coordinates
     * @param posAft    end-position of the character in world-coordinates
     */

    public CharacterWalkAction(float delay, int team, int character, Vector2 posBef, Vector2 posAft) {
        super(delay, team, character);
        LinearPath linPath = new LinearPath(posBef, posAft, V);
        this.path = linPath;
        this.duration = linPath.getEndTime();
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
}
