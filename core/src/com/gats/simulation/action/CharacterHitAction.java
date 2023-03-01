package com.gats.simulation.action;

/**
 * Type of {@link CharacterAction} created, whenever a Character receives damage.
 * (Except eeeemooootional daaaamaage)
 */
public class CharacterHitAction extends CharacterAction{
    private final int healthBef;
    private final int healthAft;

    /**
     * Stores the event of a Character receiving damage
     * @param team      team index of the Character
     * @param character Characters index within its team
     * @param healthBef Characters health before the hit
     * @param healthAft Characters health after the hit
     */
    public CharacterHitAction(int team, int character, int healthBef, int healthAft) {
        super(0.01f, team, character);
        this.healthBef = healthBef;
        this.healthAft = healthAft;
    }

    /**
     * @return Characters health after the hit
     */
    public int getHealthAft() {
        return healthAft;
    }

    /**
     * ToDo: might be deprecated, discuss removal
     * @return Characters health before the hit
     */
    public int getHealthBef() {
        return healthBef;
    }
}
