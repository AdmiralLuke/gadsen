package com.gats.simulation.action;


import com.gats.simulation.IntVector2;
import com.gats.simulation.Tile;

/**
 * Beschreibt ein {@link Action Ereigniss}, bei dem eine neue {@link Tile Box} auf der Karte erscheint
 * z.B. bei einem Chest drop ToDo: Erstelle ein Chest Tile und füge hier die korrekte Referenz ein
 */
public final class TileSummonAction extends TileAction{
    public TileSummonAction(IntVector2 pos) {
        super(pos, 0);
    }
}
