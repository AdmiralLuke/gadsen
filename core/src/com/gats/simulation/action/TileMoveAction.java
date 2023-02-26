package com.gats.simulation.action;

import com.gats.simulation.IntVector2;
import com.gats.simulation.LinearPath;
import com.gats.simulation.Path;
import com.gats.simulation.Tile;

/**
 * Beschreibt ein {@link Action Ereignis}, bei dem eine {@link Tile Box} auf der Karte bewegt wird.
 * z.B. da sie nicht mehr mit einem Anker verbunden ist.
 */
public final class TileMoveAction extends TileAction{
    private Path path;

    private float duration;
    private IntVector2 posAft;

    public TileMoveAction(IntVector2 posBef, IntVector2 posAft, float duration) {
        super(posBef, 0);
        this.posAft = posAft;
        this.path = new LinearPath(posBef.toFloat().scl(Tile.TileSizeX), posAft.toFloat().scl(Tile.TileSizeY), 0.05f);
        this.duration = path.getEndTime();
    }




    public Path getPath() {
        return path;
    }

    public float getDuration() {
        return duration;
    }

    public IntVector2 getPosAfter() {
        return posAft;
    }
}
