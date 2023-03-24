package com.gats.simulation.action;

import com.gats.simulation.IntVector2;
import com.gats.simulation.LinearPath;
import com.gats.simulation.Path;
import com.gats.simulation.Tile;


/**
 * Type of {@link TileAction} created, whenever a {@link Tile} is moved around the map.
 */
public final class TileMoveAction extends TileAction{
    private final Path path;

    private final float duration;
    private final IntVector2 posAft;

    /**
     * Stores the event of a {@link Tile} being moved linearly, from one tile-coordinate to another.
     * Works similarly to the {@link ProjectileAction}, as in that it stores the event of a Tile being removed from the tile-map at the start-position,
     * being moved along a path and finally being returned to the tile-map at the end-position
     * ToDo: cleanup either remove duration or move getEndTime out of path interface
     * @param posBef    start-position of the Tile in tile-coordinates
     * @param posAft    end-position of the Tile in tile-coordinates
     * @param duration  The duration of the event in seconds
     */
    public TileMoveAction(IntVector2 posBef, IntVector2 posAft, float duration) {
        super(0, posBef);
        this.posAft = posAft;
        this.path = new LinearPath(posBef.toFloat().scl(Tile.TileSizeX), posAft.toFloat().scl(Tile.TileSizeY), 0.05f);
        this.duration = path.getEndTime();
    }



    /**
     * @return A {@link Path} that returns the Tiles position in world-coordinates for every timestamp between 0 and duration
     */
    public Path getPath() {
        return path;
    }

    /**
     * @return The duration of the event in seconds
     */
    public float getDuration() {
        return duration;
    }

    /**
     * @return end-position of the Tile in tile-coordinates
     */
    public IntVector2 getPosAfter() {
        return posAft;
    }



       @Override

    public String toString() {

        String output = "TileMove: "+ path.toString();

        return output;

    }
}
