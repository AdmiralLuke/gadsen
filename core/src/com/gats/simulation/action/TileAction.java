package com.gats.simulation.action;

import com.gats.simulation.IntVector2;
import com.gats.simulation.Tile;

/**
 * Oberklasse für alle {@link Action Ereignisse}, die sich auf eine {@link Tile Box} beziehen
 */
abstract class TileAction extends Action{


    private IntVector2 pos;

    protected TileAction(IntVector2 tilePos, float delay) {
        super(delay);
        this.pos = tilePos;
    }

    public IntVector2 getPos() {
        return pos;
    }
}
