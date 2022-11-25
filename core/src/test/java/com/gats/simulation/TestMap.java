package com.gats.simulation;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestMap {

    private GameState state;

    @Before
    public void init() {
        // Frischen GameState oder so, frei aus TestTile übernommen
        state = new GameState(0, "map1");
    }

    @Test
    public void testMapBanana () {

        JsonReader reader = new JsonReader();
        JsonValue map = reader.parse(getClass().getClassLoader().getResourceAsStream("maps/map1.json"));
        int width = map.get("width").asInt();
        int height = map.get("height").asInt();
        Tile[][] bananaBoard = new Tile[width][height];

        JsonValue tileData = map.get("layers").get(0).get("data");

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int type = tileData.get(i + (height - j - 1) * width).asInt();
                switch (type) {
                    case 0:
                        Assert.assertNull("Hier [" + i + "," + j + "] sollte sich kein Tile befinden.", state.getBoard()[i][j]);
                        break;
                    case 1:
                        Assert.assertNotNull("Hier [" + i + "," + j + "] sollte sich eine Anker-Box befinden, aber es ist keine Box da.", state.getBoard()[i][j]);
                        Assert.assertTrue("Hier [" + i + "," + j + "] sollte sich eine Anker-Box befinden, aber die Box ist kein Anker.", state.getBoard()[i][j].getType() == 1);
                        break;
                    case 2:
                        Assert.assertNotNull("Hier [" + i + "," + j + "] sollte sich eine normale Box befinden.", state.getBoard()[i][j]);
                        break;
                }
            }
        }
    }
}
