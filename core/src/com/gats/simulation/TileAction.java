package com.gats.simulation;

import com.badlogic.gdx.math.Vector2;

/**
 * Oberklasse für alle {@link Action Ereignisse}, die sich auf eine {@link Tile Box} beziehen
 */
abstract class TileAction extends Action{


    private IntVector2 pos;
    protected TileAction(IntVector2 tilePos, long delay) {
        super(delay);
        this.pos = tilePos;
    }

    public IntVector2 getPos() {
        return pos;
    }
}
