package com.gats.simulation;
import com.badlogic.gdx.math.Vector2;
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
        clearMap();
    }

    private void clearMap() {
        for (int i = 0; i < state.getBoardSizeX(); i++) {
            for (int j = 0; j < state.getBoardSizeY(); j++) {
                state.getBoard()[i][j] = null;
            }
        }
    }

    /*

    @Test
    public void testMovement() {
        // generate a field of the form         x
        //                                  xxxxxxx
        state.getBoard()[0][0] = new Tile(0,0,true,state);
        for (int i = 0; i < 5; i++) {
            state.getBoard()[i + 1][0] = new Tile(i + 1, 0, state);
        }

        for (int i = 0; i < 6; i++) {
            Assert.assertNotNull("(" + i + ", 0) sollte Tile enthalten", state.getTile(i, 0));
        }

        state.getBoard()[4][1] = new Tile(4, 1, state);
        Assert.assertNotNull("(4,  1) sollte Tile enthalten", state.getTile(4, 1));

        GameCharacter character = state.getCharacterFromTeams(0,0);
        character.setPosX(3);
        character.setPosY(1);

        Assert.assertTrue("Character sollte bei (3,1) starten", character.getPlayerPos().x == 3 && character.getPlayerPos().y == 1);


        // move it
        character.moveDX(-3);
        Assert.assertTrue("Character sollte bei (0,1) bleiben", character.getPlayerPos().x == 0 && character.getPlayerPos().y == 1);

        // move it forward one step
        character.moveDX(1);
        Assert.assertTrue("Character sollte bei (1,1) sein", character.getPlayerPos().x == 1 && character.getPlayerPos().y == 1);

        // run it through a wall? i dont think so
        character.moveDX(4);
        Assert.assertTrue("Character sollte bei (3,1) sein ist aber bei " + character.getPlayerPos(), character.getPlayerPos().x == 3 && character.getPlayerPos().y == 1);
    }

    @Test
    public void testFalling() {
        init();
        state.getBoard()[0][5] = new Tile(0, 5, true, state);
        GameCharacter character = state.getCharacterFromTeams(0,0);
        character.setPosX(0);
        character.setPosY(6);

        Assert.assertTrue("Character sollte bei (0,6) starten", character.getPlayerPos().x == 0 && character.getPlayerPos().y == 6);
        Assert.assertNull("Da sollte keine Tile sein...", state.getTile(1, 5));
        character.move(1);
        Assert.assertTrue("Character sollte bei (1,0) sein, ist aber bei" + character.getPlayerPos(), character.getPlayerPos().x == 1 && character.getPlayerPos().y == 0);
        Assert.assertTrue("Character sollte 0 Leben haben, hat aber " + character.getHealth(), character.getHealth() == 0);
    }

    */

}
