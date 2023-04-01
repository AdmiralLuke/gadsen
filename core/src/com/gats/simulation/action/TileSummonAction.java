package com.gats.simulation.action;


import com.gats.simulation.IntVector2;
import com.gats.simulation.Tile;


/**
 * Type of {@link TileAction} created, whenever a new {@link Tile} is summoned
 */
public final class TileSummonAction extends TileAction{

    private final TileType type;

    //ToDo: declare different tile types
    public enum TileType{

    }


    /**
     * Stores the event of a Tile appearing on the tile-map
     * @param pos   position of the tile in tile-coordinates
     * @param type  type of the Tiles appearance
     */
    public TileSummonAction(IntVector2 pos, TileType type) {
        super(0, pos);
        this.type = type;
    }

    /**
     * @return  type of the Tiles appearance
     */
    public TileType getType() {
        return type;
    }


   @Override

    public String toString() {

        return "TileSummon: " + super.getPos().toString();

    }
}
