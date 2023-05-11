package com.gats.simulation;

import com.gats.simulation.action.Action;
import com.gats.simulation.action.InitAction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import com.gats.simulation.weapons.*;

public class TestDamage {

    private GameCharacter character;
    private GameState state;
    private Simulation sim;

    @Before
    public void init() {
        sim = new Simulation(GameState.GameMode.Normal, "map1", 2, 1);
        state = sim.getState();
        sim.getActionLog().getRootAction().addChild(new InitAction());
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
        state.getBoard()[0][0] = new Tile(0, 0, state, true);
        GameCharacter character = state.getCharacterFromTeams(0, 0);
        Assert.assertEquals("Leben sollte 100 sein ist aber " + character.getHealth(), character.getHealth(), 100);
        character.setPosX(0);
        character.setPosY(1);

        state.getBoard()[0][4] = new Tile(0, 4, state, true);
        state.getBoard()[0][5] = new Tile(0, 5, state);

        state.getBoard()[0][4].onDestroy(new VoidAction());
        Assert.assertNull("die Tile sollte zerstört sein", state.getBoard()[0][4]);
        Assert.assertNull("die Tile sollte zerstört sein", state.getBoard()[0][5]);

        Assert.assertNotEquals("Leben sollte nicht mehr 100 sein ist aber 100", character.getHealth(), 100);
    }

    @Test
    public void TestIfProjectileHit() {
        init();
        state.getBoard()[0][0] = new Tile(0, 0, state, true);
        GameCharacter character = state.getCharacterFromTeams(0, 0);
        Assert.assertEquals("Leben sollte 100 sein ist aber " + character.getHealth(), character.getHealth(), 100);
        character.setPosX(0);
        character.setPosY(17);

        state.getBoard()[3][2] = new Tile(3, 2, state, true);
        GameCharacter character2 = state.getCharacterFromTeams(1, 0);
        character2.setPosX(3*16 + 1);
        character2.setPosY(3*16 + 1);

        Weapon wp = character2.getWeapon(1);
        sim.getWrapper().shoot(new VoidAction(), wp, character.getPlayerPos().sub(character2.getPlayerPos()), 1, character.getPlayerPos(), character);

        Assert.assertNotEquals("Leben sollte nicht mehr 100 sein ist aber 100", character.getHealth(), 100);
    }
}
