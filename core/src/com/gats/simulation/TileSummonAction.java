package com.gats.simulation;


import com.badlogic.gdx.math.Vector2;

/**
 * Beschreibt ein {@link Action Erigniss}, bei dem eine neue {@link Tile Box} auf der Karte erscheint
 * z.B. bei einem Chest drop ToDo: Erstelle ein Chest Tile und füge hier die korrekte Referenz ein
 */
public final class TileSummonAction extends TileAction{
    public TileSummonAction(Vector2 pos) {
        super(pos, 0);
    }
}
