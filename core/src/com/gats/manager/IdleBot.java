package com.gats.manager;

import com.gats.simulation.GameState;

import java.util.Random;

public class IdleBot extends Bot{
    @Override
    public String getName() {
        return "IdleBot";
    }

    @Override
    public int getMatrikel() {
        return 133769;
    }

    @Override
    public String getStudentName() {
        return "Santa";
    }

    private static Random random;

    @Override
    protected void init(GameState state) {
        long seed = 420L;
        random = new Random(seed);
    }

    @Override
    protected void executeTurn(GameState state, Controller controller) {
        int isNegative = random.nextInt() % 2;
        int newPosX = random.nextInt() % 4;
        newPosX = isNegative != 1 ? newPosX : -newPosX;
        if (state.getTile((int)(controller.getGameCharacter().getPlayerPos().x / 16) + newPosX, (int)(controller.getGameCharacter().getPlayerPos().y / 16) - 1) != null) {
            controller.move(newPosX);
        }
    }


}
