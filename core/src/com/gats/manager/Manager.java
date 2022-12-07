package com.gats.manager;

import com.gats.manager.command.Command;
import com.gats.manager.command.EndTurnCommand;
import com.gats.simulation.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Queue;
import java.util.concurrent.*;

public class Manager {
    public class RunConfiguration {

        public int gameMode = 0;

        public boolean gui = false;

        public AnimationLogProcessor animationLogProcessor = (log) -> {};

        public String mapName;
    }

    private static final int AI_EXECUTION_TIMEOUT = 500;
    private static final int AI_INIT_TIMEOUT = 1000;

    private static final int HUMAN_EXECUTION_TIMEOUT = 30000;
    private static final int HUMAN_INIT_TIMEOUT = 30000;

    private AnimationLogProcessor animationLogProcessor;
    private boolean gui = false;
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
        gui = config.gui;
        animationLogProcessor = config.animationLogProcessor;

        //ToDo: Load Players from configuration

        //Initialize players
        int i = 0;
        for (Player player : players
        ) {

            switch (player.getType()) {
                case Human:
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
                GameCharacterController controller = simulation.getController();
                int currentPlayerIndex = controller.getGameCharacter().getTeam();
                int currentCharacterIndex = controller.getGameCharacter().getTeamPos();

                Player currentPlayer = players[currentPlayerIndex];

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

                    if (gui && currentPlayer.getType() == Player.PlayerType.Human) {
                        animationLogProcessor.animate(simulation.clearReturnActionLog());
                    }
                }
                ActionLog finalLog = simulation.endTurn();
                if (gui) animationLogProcessor.animate(finalLog);
            }
        });
        thread.start();
    }

    protected void queueCommand(Command cmd) {
        commandQueue.add(cmd);
    }
}
