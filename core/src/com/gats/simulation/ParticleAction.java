package com.gats.simulation;

import com.badlogic.gdx.math.Vector2;


public class ParticleAction extends Action {

    private Vector2 pos;
    private ParticleType type;

    public ParticleAction(Vector2 pos, ParticleType type) {
        super(0);
        this.pos = pos;
        this.type = type;
    }

    public ParticleType getType() {
        return type;
    }

    public Vector2 getPos() {
        return pos;
    }

    @Override
    public float getDelay() {
        return super.getDelay();
    }
}
