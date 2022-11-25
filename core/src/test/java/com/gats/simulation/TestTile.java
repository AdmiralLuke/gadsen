package com.gats.simulation;

import com.gats.simulation.GameState;
import com.gats.simulation.Tile;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestTile {

    private GameState state;

    @Before
    public void init() {
        // Frischen GameState
        state = new GameState(0, "map1");
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
    public void testIfAnchored() {
        // Ein paar Tiles sollen eingefügt werden
        Tile anchorTile = new Tile(1,1, true, state);
        Assert.assertNotNull("Der Anker sollte beim erstellen nicht gelöscht werden und sich im Board befinden", state.getBoard()[1][1]);

        // diese Tiles sollte im Board bleiben, da sie verankert sind
        Tile testTile1 = new Tile(2, 1, state);
        Tile testTile2 = new Tile(3,1, state);
        Assert.assertNotNull("An der Stelle [2,1] sollte das Tile verankert sein und sich im Board befinden", state.getBoard()[2][1]);
        Assert.assertNotNull("An der Stelle [3,1] sollte das Tile verankert sein und sich im Board befinden", state.getBoard()[3][1]);


        //Tiles mit 360 Grad Akzeptanz
        Tile anchorTileMiddle = new Tile(20,20,true, state);
        Assert.assertNotNull("Der Anker sollte beim erstellen nicht gelöscht werden und sich im Board befinden"
                , state.getBoard()[20][20]);

        Tile testTileEast = new Tile(21, 20, state);
        Assert.assertNotNull("An der Stelle [21,20] sollte das Tile verankert sein und sich im Board befinden"
                , state.getBoard()[21][20]);

        Tile testTileSouth = new Tile(20, 19, state);
        Assert.assertNotNull("An der Stelle [20,19] sollte das Tile verankert sein und sich im Board befinden"
                , state.getBoard()[20][19]);

        Tile testTileWest = new Tile(19, 20, state);
        Assert.assertNotNull("An der Stelle [19,20] sollte das Tile verankert sein und sich im Board befinden"
                , state.getBoard()[19][20]);

        Tile testTileNorth = new Tile(20, 21, state);
        Assert.assertNotNull("An der Stelle [20,21] sollte das Tile verankert sein und sich im Board befinden"
                , state.getBoard()[20][21]);


        // dieses Tile sollte NICHT im Board bleiben, da es nicht verankert ist
        Tile testTile3 = new Tile(5,10, state);
        Assert.assertNull("An der Stelle [5,10] sollte sich KEIN TIle befinden, da dort keine Verankerung ist", state.getBoard()[5][10]);
    }

    @Test
    public void testIfCloned() {
        Tile testTile = new Tile(20, 20, state);
        Assert.assertNotEquals("ein Clone sollte unterschiedliche Referenzen haben", testTile.returnClone(), testTile);
    }

    @Test
    public void testIfDestroyed() {
        clearMap();
        Tile testTile = new Tile(10, 10, true, state);
        for (int i = 0; i < 9; i++) {
            Tile tmpTile = new Tile(10 - i, 9, state);
            tmpTile = new Tile(10, 9-i, state);
        }
        testTile.onDestroy(state);
        for (int i = 0; i < state.getBoardSizeX(); i++) {
            for (int j = 0; j < state.getBoardSizeY(); j++) {
                Assert.assertNull("Alle Objekte in Map sollten null sein", state.getTile(i, j));
            }
        }
    }
}
