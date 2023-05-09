package bots;

import com.gats.manager.Bot;
import com.gats.manager.Controller;
import com.gats.simulation.GameState;

public class CampaignTestBot extends Bot {
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

    @Override
    protected void init(GameState state) {

    }

    @Override
    protected void executeTurn(GameState state, Controller controller) {
        controller.move(100);
    }
}
