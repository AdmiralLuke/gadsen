package com.gats.simulation;

import com.badlogic.gdx.math.Vector2;

/**
 * Beschreibt ein {@link Action Ereigniss}, bei dem eine {@link Tile Box} zerstört wird, da sie unzureichende HP besitzt
 */
public final class TileDestroyAction extends TileAction{
    public TileDestroyAction(IntVector2 position) {
        super(position, 5);
    }
}
