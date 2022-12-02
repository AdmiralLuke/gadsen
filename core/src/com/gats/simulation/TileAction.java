package com.gats.simulation;

import com.badlogic.gdx.math.Vector2;

/**
 * Oberklasse für alle {@link Action Erignisse}, die sich auf eine {@link Tile Box} beziehen
 */
abstract class TileAction extends Action{
    private Vector2 position;
    public TileAction(Vector2 position, long delay) {
        super(delay);
        this.position = position;
    }

}
