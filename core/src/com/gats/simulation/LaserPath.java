package com.gats.simulation;

import com.badlogic.gdx.math.Vector2;

public class LaserPath implements Path{

    private final Vector2 startPos;
    private final Vector2 endPos;

    public LaserPath(Vector2 startPos, Vector2 endPos) {
        this.startPos = startPos;
        this.endPos = endPos;
    }

    @Override
    public Vector2 getPos(double t) {
        return t == 0 ? startPos : endPos;
    }

    /**
     * Not implemented.
     * @param t
     * @return
     */
    @Override
    public Vector2 getDir(double t){
        return Vector2.Zero;
    }

    public Vector2 getEndPos() {
        return endPos;
    }

    public Vector2 getStartPos() {
        return startPos;
    }

    @Override
    public float getEndTime() {
        return 0;
    }
}
