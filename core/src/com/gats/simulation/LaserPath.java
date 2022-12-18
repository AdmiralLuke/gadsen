package com.gats.simulation;

import com.badlogic.gdx.math.Vector2;

public class LaserPath implements Path{

    private Vector2 startPos;
    private Vector2 endPos;

    public LaserPath(Vector2 startPos, Vector2 endPos) {
        this.startPos = startPos;
        this.endPos = endPos;
    }

    @Override
    public Vector2 getPos(double t) {
        return t == 0 ? startPos : endPos;
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
