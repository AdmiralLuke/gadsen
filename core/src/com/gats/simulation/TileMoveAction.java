package com.gats.simulation;

import com.badlogic.gdx.math.Vector2;

/**
 * Beschreibt ein {@link Action Ereignis}, bei dem eine {@link Tile Box} auf der Karte bewegt wird.
 * z.B. da sie nicht mehr mit einem Anker verbunden ist.
 */
public final class TileMoveAction extends TileAction{
    private LinearPath path;
    private Tile tile;

    public TileMoveAction(Vector2 posBef, Vector2 posAft) {
        super(posAft, 0);
        this.path = new LinearPath(posBef, posAft);
    }
}
