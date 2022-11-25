package com.gats.simulation;

/**
 * Oberklasse für alle {@link Action Erignisse}, die sich auf eine {@link Tile Box} beziehen
 */
abstract class TileAction extends Action{
    private Tile tile;
    public TileAction(Tile tile) {
        this.tile = tile;
    }

}
