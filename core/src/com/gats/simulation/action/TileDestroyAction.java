package com.gats.simulation.action;

import com.gats.simulation.IntVector2;
import com.gats.simulation.Tile;

/**
 * Type of {@link TileAction} created, whenever a {@link Tile} is destroyed.
 */
public final class TileDestroyAction extends TileAction{
    /**
     * Stores the Event of a {@link Tile} being destroyed
     * @param tilePos   position of the related {@link Tile} in tile-coordinates
     */
    public TileDestroyAction(IntVector2 tilePos) {
        super(0f, tilePos);
    }


   @Override

    public String toString() {

        String output = "TileDestroy" + super.getPos().toString();

       return output;

    }
}
