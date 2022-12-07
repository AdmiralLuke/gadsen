package com.gats.manager;

import com.gats.manager.command.Command;
import com.gats.manager.command.EndTurnCommand;
import com.gats.simulation.GameCharacterController;
import com.gats.simulation.GameState;
import com.gats.simulation.Simulation;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Queue;
import java.util.concurrent.*;

public class Manager {
    public class RunConfiguration {

        public int gameMode = 0;

        public String mapName;
    }

    private static final int AI_EXECUTION_TIMEOUT = 500;
    private static final int AI_INIT_TIMEOUT = 1000;

    private static final int HUMAN_EXECUTION_TIMEOUT = 30000;
    private static final int HUMAN_INIT_TIMEOUT = 30000;

    private Simulation simulation;
    private GameState state;
    private Player[] players;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    private BlockingQueue<Command> commandQueue = new ArrayBlockingQueue<>(128);

    /**
     * Initializes simulation and players before
     * starting the asynchronous execution
     *
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
        Thread thread = new Thread(() -> {
            while (true) { // ToDo: state.isActive()
                int currentPlayerIndex = 0;//ToDo: Get Player whose turn it is
                int currentCharacterIndex = 0;//ToDo: Get Character whose turn it is
                Player currentPlayer = players[currentPlayerIndex];
                GameCharacterController controller = null; //ToDo get Controller from Simulation
                Thread futureExecutor;
                Future<?> future;
                switch (currentPlayer.getType()) {
                    case Human:
                        future = executor.submit(() -> currentPlayer.executeTurn(state, controller));
                        futureExecutor = new Thread(() -> {
                            try {
                                future.get(HUMAN_EXECUTION_TIMEOUT, TimeUnit.MILLISECONDS);
                            } catch (InterruptedException e) {    //     <-- possible error cases
                                System.out.println("bot was interrupted");
                            } catch (ExecutionException e) {
                                System.out.println("human player failed with exception: " + e.getCause());
                            } catch (TimeoutException e) {
                                future.cancel(true);

                                System.out.println("player" + currentPlayerIndex + "(" + currentPlayer.getName()
                                        + ") computation surpassed timeout");
                            }
                        });
                        break;
                    case AI:
                        future = executor.submit(() -> currentPlayer.executeTurn(state, controller));
                        futureExecutor = new Thread(() -> {
                            try {
                                future.get(AI_EXECUTION_TIMEOUT, TimeUnit.MILLISECONDS);
                            } catch (InterruptedException e) {    //     <-- possible error cases
                                System.out.println("bot was interrupted");
                            } catch (ExecutionException e) {
                                System.out.println("bot failed with exception: " + e.getCause());
                            } catch (TimeoutException e) {
                                future.cancel(true);

                                System.out.println("player" + currentPlayerIndex + "(" + currentPlayer.getName()
                                        + ") computation surpassed timeout");
                            }
                            //Add Empty command to
                            commandQueue.add(new EndTurnCommand());
                        });
                        break;
                    default:
                        throw new IllegalStateException("Player of type: " + currentPlayer.getType() + " can not be executed by the Manager");
                }

                futureExecutor.start();
                while (futureExecutor.isAlive() && !commandQueue.isEmpty()) {
                    Command nextCmd = commandQueue.poll();
                    if (nextCmd.isEndTurn()) break;
                    nextCmd.run();

                    if (currentPlayer.getType() == Player.PlayerType.AI) {
                        //ToDo: Animate Action Logs in real time
                    }
                }
                //ToDo: Animate Action Logs if required
                //ToDo: End Turn
            }
        });
        thread.start();
    }

    protected void queueCommand(Command cmd) {
        commandQueue.add(cmd);
    }
}
