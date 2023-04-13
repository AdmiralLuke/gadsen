package com.gats.manager;

import com.gats.manager.command.Command;
import com.gats.manager.command.EndTurnCommand;
import com.gats.simulation.GameCharacterController;
import com.gats.simulation.GameState;
import com.gats.simulation.Simulation;
import com.gats.simulation.action.ActionLog;
import com.gats.ui.hud.UiMessenger;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Game {

    public interface CompletionHandler{
        void onComplete(Game game);
    }

    private boolean completed = false;

    private ArrayList<CompletionHandler> completionListeners = new ArrayList<>();
    private static final int AI_EXECUTION_TIMEOUT = 500;
    private static final int AI_INIT_TIMEOUT = 1000;

    private static final int HUMAN_EXECUTION_TIMEOUT = 30000;
    private static final int HUMAN_INIT_TIMEOUT = 30000;

    private final InputProcessor inputGenerator;

    private final AnimationLogProcessor animationLogProcessor;

    private boolean gui = false;
    private Simulation simulation;
    private GameState state;
    private Player[] players;

    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private List<HumanPlayer> humanList = new ArrayList<>();

    private BlockingQueue<Command> commandQueue = new ArrayBlockingQueue<>(128);
    private Thread simulationThread;
    private UiMessenger uiMessenger;
    private boolean pendingShutdown = false;
    private GameConfig config;


    protected Game(GameConfig config) {
        this.config = config;
        gui = config.gui;
        animationLogProcessor = config.animationLogProcessor;
        inputGenerator = config.inputProcessor;
        uiMessenger = config.uiMessenger;
    }

    private void create(){

        simulation = new Simulation(config.gameMode, config.mapName, config.teamCount, config.teamSize);
        state = simulation.getState();

        players = new Player[config.teamCount];

        for (int i = 0; i < config.teamCount; i++) {
            final Player curPlayer;
            try {
                players[i] = (Player) config.players.get(i).getDeclaredConstructors()[0].newInstance();
                curPlayer = players[i];
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            switch (curPlayer.getType()) {
                case Human:
                    if (!gui) throw new RuntimeException("HumanPlayers can't be used without GUI to capture inputs");
                    humanList.add((HumanPlayer) curPlayer);
                    ((HumanPlayer)(curPlayer)).setUiMessenger(uiMessenger);
                    break;
                case AI:

                    Future<?> future = executor.submit(new Runnable() {
                        @Override
                        public void run() {
                            Thread.currentThread().setName("Init_Thread_Player_" + curPlayer.getName());
                            curPlayer.init(state);
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

                        System.out.println("bot" + i + "(" + curPlayer.getName() + ") initialization surpassed timeout");
                    }
                    break;
            }
        }
        config = null;
    }

    public void start() {
        create();
        //Init the Log Processor
        animationLogProcessor.init(state);
        //Run the Game
        simulationThread = new Thread(this::run);
        simulationThread.setName("Manager_Simulation_Thread");
        simulationThread.start();
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
        while (!pendingShutdown && state.isActive()) {
            GameCharacterController gcController = simulation.getController();
            int currentPlayerIndex = gcController.getGameCharacter().getTeam();
            int currentCharacterIndex = gcController.getGameCharacter().getTeamPos();

            Player currentPlayer = players[currentPlayerIndex];
            Controller controller = new Controller(this, gcController);

            Thread futureExecutor;
            Future<?> future;
            switch (currentPlayer.getType()) {
                case Human:
                    future = executor.submit(() -> currentPlayer.executeTurn(state, controller));
                    futureExecutor = new Thread(() -> {
                        inputGenerator.activateTurn((HumanPlayer) currentPlayer);
                        try {
                            Thread.currentThread().setName("Run_Thread_Player_Human");
                            future.get(HUMAN_EXECUTION_TIMEOUT, TimeUnit.MILLISECONDS);
                        } catch (InterruptedException e) {
                            future.cancel(true);//Executor was interrupted: Interrupt Player
                            System.out.println("bot was interrupted");
                        } catch (ExecutionException e) {
                            System.out.println("human player failed with exception: " + e.getCause());
                            e.printStackTrace();
                        } catch (TimeoutException e) {
                            future.cancel(true);

                            System.out.println("player" + currentPlayerIndex + "(" + currentPlayer.getName() + ") computation surpassed timeout");
                        }
                        inputGenerator.endTurn();
                        //Add Empty command to break command Execution
                        try {
                            commandQueue.put(new EndTurnCommand());
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    break;
                case AI:
                    future = executor.submit(() -> {
                        Thread.currentThread().setName("Run_Thread_Player_" + currentPlayer.getName());
                        currentPlayer.executeTurn(state, controller);
                    });
                    futureExecutor = new Thread(() -> {
                        Thread.currentThread().setName("Future_Executor_Player_" + currentPlayer.getName());
                        try {
                            future.get(AI_EXECUTION_TIMEOUT, TimeUnit.MILLISECONDS);
                        } catch (InterruptedException e) {
                            future.cancel(true);//Executor was interrupted: Interrupt Bot
                            System.out.println("bot was interrupted");
                        } catch (ExecutionException e) {
                            System.out.println("bot failed with exception: " + e.getCause());
                            e.printStackTrace();
                        } catch (TimeoutException e) {
                            future.cancel(true);

                            System.out.println("player" + currentPlayerIndex + "(" + currentPlayer.getName() + ") computation surpassed timeout");
                        }
                        //Add Empty command to break command Execution
                        try {
                            commandQueue.put(new EndTurnCommand());
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    break;
                default:
                    throw new IllegalStateException("Player of type: " + currentPlayer.getType() + " can not be executed by the Manager");
            }

            futureExecutor.start();
            if (gui && currentPlayer.getType() == Player.PlayerType.Human) {
                //Contains Action produced by entering new turn
                animationLogProcessor.animate(simulation.clearAndReturnActionLog());
            }
            try {
                while (true) {
                    Command nextCmd = commandQueue.take();
                    if (nextCmd.isEndTurn()) break;
                    //Contains action produced by the commands execution
                    ActionLog log = nextCmd.run();

                    if (gui && currentPlayer.getType() == Player.PlayerType.Human) {
                        animationLogProcessor.animate(log);
                        //animationLogProcessor.awaitNotification(); ToDo: discuss synchronisation for human players
                    }
                }
            } catch (InterruptedException e) {
                System.err.println("Interrupted while processing cmds");
                if (pendingShutdown) {
                    futureExecutor.interrupt();
                    break;
                }
                throw new RuntimeException(e);
            }
            controller.deactivate();

            //Contains actions produced by ending the turn (after last command is executed)
            ActionLog finalLog = simulation.endTurn();
            if (gui) {
                animationLogProcessor.animate(finalLog);
                animationLogProcessor.awaitNotification();
                if (pendingShutdown) {
                    executor.shutdown();
                    futureExecutor.interrupt();
                    break;
                }
            }
            try {
                futureExecutor.join(); //Wait for the executor to shutdown to prevent spamming the executor service
            } catch (InterruptedException e) {
                System.out.printf("Interrupted while shutting down future executor\n");
                if (pendingShutdown) {
                    futureExecutor.interrupt();
                    break;
                }
                throw new RuntimeException(e);
            }
        }
        completed = true;
        for (CompletionHandler completionListener : completionListeners) {
            completionListener.onComplete(this);
        }
//        System.out.println("Shutdown complete");
    }

    public void addCompletionListener(CompletionHandler handler){
        completionListeners.add(handler);
        if (completed) handler.onComplete(this);
    }

    public void dispose() {
        //Shutdown all running threads
        pendingShutdown = true;
        if (simulationThread != null) {
            System.out.println("Interrupting simulation thread");
            simulationThread.interrupt();
            executor.shutdown();
        }
    }

    public List<HumanPlayer> getHumanList() {
        return humanList;
    }

    protected void queueCommand(Command cmd) {
        commandQueue.add(cmd);
    }
}
