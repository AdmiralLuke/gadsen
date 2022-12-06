package com.gats.manager;

import com.gats.simulation.GameCharacterController;
import com.gats.simulation.GameState;
import com.gats.simulation.Simulation;

import java.util.concurrent.*;

public class Manager {
    public class RunConfiguration {

        public int gameMode = 0;

        public String mapName;
    }

    private static final int AI_EXECUTION_TIMEOUT = 500;
    private static final int AI_INIT_TIMEOUT = 1000;

    private Simulation simulation;
    private GameState state;
    private Player[] players;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    /**
     * Initializes simulation and players before
     * starting the asynchronous execution
     * @param config The execution-parameters of the simulation
     */
    public Manager(RunConfiguration config) {
        // ToDo: add Team-Stuff
        simulation = new Simulation(config.gameMode, config.mapName, 0, 0);
        state = simulation.getState();

        //ToDo: Load Players from configuration

        //Initialize players
        int i = 0;
        for (Player player : players
        ) {

            switch (player.getType()) {
                case Human:
                    //ToDo
                    break;
                case AI:

                    Future<?> future = executor.submit(new Runnable() {
                        @Override
                        public void run() {
                            player.init(state);
                        }
                    });

                    try {
                        future.get(AI_INIT_TIMEOUT, TimeUnit.MILLISECONDS);
                    } catch (InterruptedException e) {
                        System.out.println("bot was interrupted");
                    } catch (ExecutionException e) {
                        System.out.println("bot failed initialization with exception: " + e.getCause());
                    } catch (TimeoutException e) {
                        future.cancel(true);

                        System.out.println("bot" + i + "(" + player.getName()
                                + ") initialization surpassed timeout");
                    }
                    break;
            }
            i++;
        }
        //Run the Game
        new Thread(this::run).start();
    }

    /**
     * @return The state of the underlying simulation
     */
    public GameState getState() {
        return state;
    }

    /**
     * Controls Player Execution
     */
    private void run() {
        while (true) { // ToDo: state.isActive()
            int currentPlayerIndex = 0;//ToDo: Get Player whose turn it is
            int currentCharacterIndex = 0;//ToDo: Get Character whose turn it is
            Player currentPlayer = players[currentPlayerIndex];
            GameCharacterController controller = null; //ToDo get Controller from Simulation
            switch (currentPlayer.getType()) {
                case Human:
                    //ToDo Run Human player
                    //ToDo Pause thread until human player finishes
                    //ToDo: Animate Action Logs in real time

                    break;
                case AI:

                    Future<?> future = executor.submit(new Runnable() {
                        @Override
                        public void run() {
                            currentPlayer.executeTurn(state, controller);
                        }
                    });

                    try {
                        future.get(AI_EXECUTION_TIMEOUT, TimeUnit.MILLISECONDS);
                    } catch (InterruptedException e) {    //     <-- possible error cases
                        System.out.println("bot was interrupted");
                    } catch (ExecutionException e) {
                        System.out.println("bot failed with exception: " + e.getCause());
                    } catch (TimeoutException e) {
                        future.cancel(true);

                        System.out.println("bot" + currentPlayerIndex + "(" + currentPlayer.getName()
                                + ") computation surpassed timeout");
                    }
                    //ToDo: Animate Action Logs if required
                    break;
            }
            //ToDo: End Turn
            controller.invalidate();
        }
    }
}
