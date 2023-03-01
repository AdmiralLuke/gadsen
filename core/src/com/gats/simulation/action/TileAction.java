package com.gats.simulation.action;

import com.gats.simulation.IntVector2;
import com.gats.simulation.Tile;

/**
 * Super class for all {@link Action Actions}, that relate to a {@link Tile}
 */
abstract class TileAction extends Action{


    private final IntVector2 pos;

    /**
     * Super class for the Action of a {@link Tile}
     * @param delay     non-negative time-based offset to its parent in seconds
     * @param tilePos   position of the related {@link Tile} in tile-coordinates
     */
    protected TileAction(float delay, IntVector2 tilePos) {
        super(delay);
        this.pos = tilePos;
    }

    /**
     * @return position of the related {@link Tile} in tile-coordinates
     */
    public IntVector2 getPos() {
        return pos;
    }
}
