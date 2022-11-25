package com.gats.simulation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class TestGameCharacter {

    private GameCharacter character;
    private ActionLog actions;
    private GameState state;

    @Before
    public void init() {
        actions = new ActionLog();
        state = new GameState(0, "map1");
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
        character.move(10, 15, actions, state);
        Assert.assertTrue("Spieler sollte sich nun bei (10, 15) befinden", character.getPlayerPos().x == 10 && character.getPlayerPos().y == 15);
        character.move(-5, -3, actions, state);
        Assert.assertTrue("Spieler sollte sich nun bei (5, 12) befinden", character.getPlayerPos().x == 5 && character.getPlayerPos().y == 12);

        // edge cases
        character.move(-12, 3, actions, state);
        Assert.assertTrue("Spieler sollte sich nun bei (0, 15) befinden", character.getPlayerPos().x == 0 && character.getPlayerPos().y == 15);
        character.move(state.getBoardSizeX() - 2, -18, actions, state);
        Assert.assertTrue("Spieler sollte sich nun bei (" + (state.getBoardSizeX() - 2) + ", 0) befinden", character.getPlayerPos().x == state.getBoardSizeX() - 2 && character.getPlayerPos().y == 0);

        character.move(3, 10, actions, state);
        Assert.assertTrue("Spieler sollte sich nun bei (" + (state.getBoardSizeX() - 1) + ", 10) befinden", character.getPlayerPos().x == state.getBoardSizeX() - 1 && character.getPlayerPos().y == 10);
        character.move(-(state.getBoardSizeX() - 1), state.getBoardSizeY(), actions, state);
        Assert.assertTrue("Spieler sollte sich nun bei (0, " + (state.getBoardSizeY() - 1) +") befinden", character.getPlayerPos().x == 0 && character.getPlayerPos().y == state.getBoardSizeY() - 1);
    }

}
