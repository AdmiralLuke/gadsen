package com.gats.simulation.campaign;

import com.gats.manager.Bot;
import com.gats.manager.Controller;
import com.gats.simulation.GameState;

import java.util.Random;

public class CampaignBot extends Bot {

    @Override
    public String getStudentName() {
        return "Cornelius Zenker";
    }

    @Override
    public int getMatrikel() {
        return -1; //Heh, you thought
    }

    @Override
    public String getName() {
        return "Training Bot";
    }

    protected int turnCount = -1;

    @Override
    protected void init(GameState state) {

    }

    @Override
    protected void executeTurn(GameState state, Controller controller) {
        turnCount++;
    }

    @Override
    public String getSkin(int characterIndex) {
        return "coolCatSkin";
    }
}
