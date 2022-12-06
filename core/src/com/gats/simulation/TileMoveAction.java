package com.gats.simulation;

import com.badlogic.gdx.math.Vector2;

/**
 * Beschreibt ein {@link Action Ereignis}, bei dem eine {@link Tile Box} auf der Karte bewegt wird.
 * z.B. da sie nicht mehr mit einem Anker verbunden ist.
 */
public final class TileMoveAction extends TileAction{
    private Path path;

    private static double v = 0.0001;
    private float duration;
    private IntVector2 posAft;

    public TileMoveAction(IntVector2 posBef, IntVector2 posAft, float duration) {
        super(posBef, 0);
        this.posAft = posAft;
        this.path = new LinearPath(posBef.toFloat().scl(Tile.tileSize.toFloat()), posAft.toFloat().scl(Tile.tileSize.toFloat()));
        this.duration = duration;
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
