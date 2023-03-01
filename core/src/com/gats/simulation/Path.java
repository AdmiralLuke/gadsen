package com.gats.simulation;

import com.badlogic.gdx.math.Vector2;

public interface Path {
    Vector2 getPos(double t);

    Vector2 getDir(double t);

    //ToDo: discuss removal
    float getEndTime();
}
