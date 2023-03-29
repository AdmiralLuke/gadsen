package com.gats.simulation.action;

import com.gats.simulation.Path;

public class CharacterMoveAction extends CharacterAction {

    Path path;

    public CharacterMoveAction(float delay, int team, int character, Path path) {
        super(delay, team, character);
        this.path = path;
    }


}
