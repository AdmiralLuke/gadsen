package com.gats.simulation;
import com.gats.simulation.GameState;
import com.gats.simulation.Tile;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestGameState {

    private GameState state;

    @Before
    public void init() {
        state = new GameState(0, "map1");
    }

    /**
     * schaue ob etwas ins Board geladen werden kann
     */
    @Test
    public void testIfBoardFilled() {
        Assert.assertNull("Board sollte beim Erstellen null Objekte enthalten",state.getBoard()[0][0]);
        Tile testTile = new Tile(0,0,true, state);
        Assert.assertNotNull("Board sollte eine Tile enthalten, nachdem sie erstellt wurde",state.getBoard()[0][0]);
    }

}
