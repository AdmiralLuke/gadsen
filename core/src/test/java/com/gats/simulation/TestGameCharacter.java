package com.gats.simulation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class TestGameCharacter {

    private GameCharacter character;
    private GameState state;
    private Simulation sim;

    @Before
    public void init() {
        sim = new Simulation(0, "map1", 2, 1);
        state = sim.getState();
        character = new GameCharacter(0,0,state);
    }

    @Test
    public void testCharacterConst() {
        Assert.assertEquals("Spieler sollte auf Position 0 starten", 0, character.getPlayerPos().x, 0.0);
        Assert.assertEquals("Spieler sollte auf Position 0 starten", 0, character.getPlayerPos().y, 0.0);

        character = new GameCharacter(5,3, state);
        Assert.assertTrue("Spielerposition an einer bestimmten Stelle", character.getPlayerPos().x == 5 && character.getPlayerPos().y == 3);
    }

    @Test
    public void testMovement() {
        character = new GameCharacter(0,0, state);
        // ToDo: rewrite Test
    }

}
