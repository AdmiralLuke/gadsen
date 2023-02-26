package com.gats.simulation.action;

import com.gats.simulation.IntVector2;
import com.gats.simulation.Tile;

/**
 * Beschreibt ein {@link Action Ereigniss}, bei dem eine {@link Tile Box} zerstört wird, da sie unzureichende HP besitzt
 */
public final class TileDestroyAction extends TileAction{
    public TileDestroyAction(IntVector2 position) {
        super(position, 0.005f);
    }
}
