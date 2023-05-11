package bots;

import com.gats.manager.Bot;
import com.gats.manager.BotThreadFactory;
import com.gats.manager.Controller;
import com.gats.simulation.GameState;
import java.util.*;
import java.util.concurrent.*;

public class FilterTestBot extends Bot {
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
        return "Hacker Gadse";
    }

    @Override
    protected void init(GameState state) {
        new Thread();
    }

    @Override
    protected void executeTurn(GameState state, Controller controller) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
    }
}
