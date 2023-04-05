package com.gats.animation;

import com.badlogic.gdx.math.Vector2;
import com.gats.simulation.GameCharacter;
import com.gats.simulation.Path;

public class CharacterPath implements Path {

    private final Path path;

    private static final Vector2 offset = GameCharacter.getSize().scl(0.5f);

    public CharacterPath(Path path) {

        this.path = path;
    }

    @Override
    public Vector2 getPos(float t) {
        return path.getPos(t).add(offset);
    }

    @Override
    public Vector2 getDir(float t) {
        return path.getDir(t);
    }

    @Override
    public float getEndTime() {
        return path.getEndTime();
    }
}
