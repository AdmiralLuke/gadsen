package com.gats.simulation;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestDamage {

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

    @Test
    public void TestIfTileHitDamage() {
        state.getBoard()[0][0] = new Tile(0, 0, true, state);
        GameCharacter character = state.getCharacterFromTeams(0, 0);
        Assert.assertEquals("Leben sollte 100 sein ist aber " + character.getHealth(), character.getHealth(), 100);
        character.setPosX(0);
        character.setPosY(1);

        state.getBoard()[0][4] = new Tile(0, 4, true, state);
        state.getBoard()[0][5] = new Tile(0, 5, state);

        state.getBoard()[0][4].onDestroy();
        Assert.assertNull("die Tile sollte zerstört sein", state.getBoard()[0][4]);
        Assert.assertNull("die Tile sollte zerstört sein", state.getBoard()[0][5]);

        Assert.assertNotEquals("Leben sollte nicht mehr 100 sein ist aber 100", character.getHealth(), 100);
    }

    @Test
    public void TestIfProjectileHit() {
        init();
        state.getBoard()[0][0] = new Tile(0, 0, true, state);
        GameCharacter character = state.getCharacterFromTeams(0, 0);
        Assert.assertEquals("Leben sollte 100 sein ist aber " + character.getHealth(), character.getHealth(), 100);
        character.setPosX(0);
        character.setPosY(1);

        state.getBoard()[3][2] = new Tile(3, 2, true, state);
        GameCharacter character2 = state.getCharacterFromTeams(1, 0);
        character2.setPosX(3);
        character2.setPosY(3);

        Weapon wp = character2.getWeapon(1);
        wp.shoot(character.getPlayerPos().sub(character2.getPlayerPos()), 1);

        Assert.assertNotEquals("Leben sollte nicht mehr 100 sein ist aber 100", character.getHealth(), 100);
    }
}