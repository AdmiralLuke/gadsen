package com.gats.simulation.action;

import com.gats.simulation.Path;

public class CharacterMoveAction extends CharacterAction {

    Path path;

    public CharacterMoveAction(float delay, int team, int character, Path path) {
        super(delay, team, character);
        this.path = path;
    }

    public Path getPath() {
        return path;
    }

    @Override
    public String toString() {
        return "CharacterMoveAction: moved from " + path.getPos(0) + " to " + path.getPos(path.getDuration()) + " in " + path.getDuration() + "s";
    }
}
