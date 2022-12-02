package com.gats.manager;

import com.gats.simulation.GameCharacterController;
import com.gats.simulation.GameState;

public class IdleBot extends Bot{
    @Override
    public String getStudentName() {
        return "Cornelius Zenker";
    }

    @Override
    public int getMatrikel() {
        return 42;
    }

    @Override
    protected String getName() {
        return "Idle";
    }

    @Override
    protected void init(GameState state) {

    }

    @Override
    protected void executeTurn(GameState state, GameCharacterController characterController) {

    }
}
