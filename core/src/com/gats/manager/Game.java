package com.gats.manager;

import com.gats.manager.command.Command;
import com.gats.manager.command.EndTurnCommand;
import com.gats.simulation.GameCharacterController;
import com.gats.simulation.GameState;
import com.gats.simulation.Simulation;
import com.gats.simulation.action.ActionLog;
import com.gats.simulation.campaign.CampaignResources;
import com.gats.ui.hud.UiMessenger;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class Game {


    protected static final int REQUIRED_THREAD_COUNT = 2;


    enum Status {
        INITIALIZED,
        CREATED,

        SCHEDULED,
        ACTIVE,
        PAUSED,
        COMPLETED,
        ABORTED
    }

    protected final Object schedulingLock = new Object();

    private Status status = Status.INITIALIZED;

    private final ArrayList<CompletionHandler<Game>> completionListeners = new ArrayList<>();
    private static final int AI_EXECUTION_TIMEOUT = 500;
    private static final int AI_EXECUTION_GRACE_PERIODE = 100;
    private static final int AI_INIT_TIMEOUT = 1000;

    private static final int HUMAN_EXECUTION_TIMEOUT = 30000;
    private static final int HUMAN_EXECUTION_GRACE_PERIODE = 5000;
    private static final int HUMAN_INIT_TIMEOUT = 30000;

    private final InputProcessor inputGenerator;

    private final AnimationLogProcessor animationLogProcessor;

    private final boolean gui;

    private final GameResults gameResults;
    private Simulation simulation;
    private GameState state;
    private Player[] players;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final List<HumanPlayer> humanList = new ArrayList<>();

    private final BlockingQueue<Command> commandQueue = new ArrayBlockingQueue<>(128);
    private Thread simulationThread;
    private final UiMessenger uiMessenger;
    private boolean pendingShutdown = false;
    private GameConfig config;

    private boolean saveReplay;


    protected Game(GameConfig config) {
        this.config = config;
        if (config.gameMode == GameState.GameMode.Campaign) {
            if (config.players.size() != 1) {
                System.err.println("Campaign only accepts exactly 1 player");
                setStatus(Status.ABORTED);
            }
            config.players.addAll(CampaignResources.getEnemies(config.mapName));
            config.teamCount = config.players.size();
            config.teamSize = CampaignResources.getCharacterCount(config.mapName);
        }
        gui = config.gui;
        saveReplay = config.replay;
        animationLogProcessor = config.animationLogProcessor;
        inputGenerator = config.inputProcessor;
        uiMessenger = config.uiMessenger;
        gameResults = new GameResults(config);
        gameResults.setStatus(status);
    }

    private void create() {

        simulation = new Simulation(config.gameMode, config.mapName, config.teamCount, config.teamSize);
        state = simulation.getState();
        gameResults.setInitialState(state);

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
                    break;
                case AI:

                    Future<?> future = executor.submit(() -> {
                        Thread.currentThread().setName("Init_Thread_Player_" + curPlayer.getName());
                        curPlayer.init(state);
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
        setStatus(Status.CREATED);
    }

    public void start() {
        if (status == Status.ABORTED) return;
        setStatus(Status.ACTIVE);
        create();
        //Init the Log Processor
        if (gui) animationLogProcessor.init(state);
        //Run the Game
        simulationThread = new Thread(this::run);
        simulationThread.setName("Manager_Simulation_Thread");
        simulationThread.setUncaughtExceptionHandler(this::crashHandler);
        simulationThread.start();
    }

    private void crashHandler(Thread thread, Throwable throwable) {
        System.err.println("Error in game: " + this);
        System.err.println("Error in thread: " + thread);
        throwable.printStackTrace();
        System.err.println("Game crashed during execution\nIf you see this message, please forward all console logs to wettbewerb@acagamics.de");
        Manager.getManager().stop(this);
    }

    private void setStatus(Status newStatus) {
        status = newStatus;
        gameResults.setStatus(status);
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
            synchronized (schedulingLock) {
                if (status == Status.PAUSED)
                    try {
                        schedulingLock.wait();

                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
            }

            GameCharacterController gcController = simulation.getController();
            int currentPlayerIndex = gcController.getGameCharacter().getTeam();
            int currentCharacterIndex = gcController.getGameCharacter().getTeamPos();

            Player currentPlayer = players[currentPlayerIndex];
            Controller controller = new Controller(this, gcController);

            Thread futureExecutor;
            Future<?> future;
            GameState stateCopy = state.copy();
            switch (currentPlayer.getType()) {
                case Human:
                    future = executor.submit(() -> {
                        Thread.currentThread().setName("Run_Thread_Player_Human");
                        simulation.setTurnTimer(new Timer(1000 * HUMAN_EXECUTION_TIMEOUT));
                        currentPlayer.executeTurn(stateCopy, controller);
                    });
                    futureExecutor = new Thread(() -> {
                        inputGenerator.activateTurn((HumanPlayer) currentPlayer);
                        try {
                            Thread.currentThread().setName("Future_Executor_Player_Human");
                            future.get(HUMAN_EXECUTION_TIMEOUT + HUMAN_EXECUTION_GRACE_PERIODE, TimeUnit.MILLISECONDS);
                        } catch (InterruptedException e) {
                            future.cancel(true);//Executor was interrupted: Interrupt Player
                            System.out.println("bot was interrupted");
                            e.printStackTrace(System.err);
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
                            commandQueue.put(new EndTurnCommand(gcController));
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    break;
                case AI:
                    future = executor.submit(() -> {
                        Thread.currentThread().setName("Run_Thread_Player_" + currentPlayer.getName());
                        simulation.setTurnTimer(new Timer(1000 * AI_EXECUTION_TIMEOUT));
                        currentPlayer.executeTurn(stateCopy, controller);
                    });
                    futureExecutor = new Thread(() -> {
                        Thread.currentThread().setName("Future_Executor_Player_" + currentPlayer.getName());
                        try {
                            future.get(AI_EXECUTION_TIMEOUT + AI_EXECUTION_GRACE_PERIODE, TimeUnit.MILLISECONDS);
                        } catch (InterruptedException e) {
                            future.cancel(true);//Executor was interrupted: Interrupt Bot
                            System.out.println("bot was interrupted");
                            e.printStackTrace(System.err);
                        } catch (ExecutionException e) {
                            System.out.println("bot failed with exception: " + e.getCause());
                            e.printStackTrace();
                        } catch (TimeoutException e) {
                            future.cancel(true);

                            System.out.println("player" + currentPlayerIndex + "(" + currentPlayer.getName() + ") computation surpassed timeout");
                        }
                        //Add Empty command to break command Execution
                        try {
                            commandQueue.put(new EndTurnCommand(gcController));
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    break;
                default:
                    throw new IllegalStateException("Player of type: " + currentPlayer.getType() + " can not be executed by the Manager");
            }

            futureExecutor.start();
            ActionLog log = simulation.clearAndReturnActionLog();
            gameResults.addActionLog(log);
            if (gui && currentPlayer.getType() == Player.PlayerType.Human) {
                //Contains Action produced by entering new turn
                animationLogProcessor.animate(log);
            }
            try {
                while (true) {
                    if (!simulation.isActingCharacterAlive()) {
                        if (currentPlayer.getType() == Player.PlayerType.Human) inputGenerator.endTurn();
                        break;
                    }
                    Command nextCmd = commandQueue.take();
                    if (nextCmd.isEndTurn()) break;
                    //Contains action produced by the commands execution
                    log = nextCmd.run();
                    if (log == null) continue;
                    gameResults.addActionLog(log);
                    if (gui) {
                        animationLogProcessor.animate(log);
                        //animationLogProcessor.awaitNotification(); ToDo: discuss synchronisation for human players
                    }
                }
            } catch (InterruptedException e) {
                System.err.println("Interrupted while processing cmds");
                e.printStackTrace(System.err);
                if (pendingShutdown) {
                    futureExecutor.interrupt();
                    break;
                }
                throw new RuntimeException(e);
            }
            controller.deactivate();

            //Contains actions produced by ending the turn (after last command is executed)
            ActionLog finalLog = simulation.endTurn();
            gameResults.addActionLog(finalLog);
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
                System.out.print("Interrupted while shutting down future executor\n");
                e.printStackTrace(System.err);
                if (pendingShutdown) {
                    futureExecutor.interrupt();
                    break;
                }
                throw new RuntimeException(e);
            }
        }
        setStatus(Status.COMPLETED);
        for (CompletionHandler<Game> completionListener : completionListeners) {
            completionListener.onComplete(this);
        }
//        System.out.println("Shutdown complete");
    }

    public void addCompletionListener(CompletionHandler<Game> handler) {
        completionListeners.add(handler);
        if (status == Status.COMPLETED) handler.onComplete(this);
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

    public Status getStatus() {
        return status;
    }

    protected void schedule() {
        synchronized (schedulingLock) {
            if (status == Status.CREATED)
                setStatus(Status.SCHEDULED);
        }
    }

    protected void pause() {
        synchronized (schedulingLock) {
            if (status == Status.ACTIVE)
                setStatus(Status.PAUSED);
        }
    }

    protected void resume() {
        synchronized (schedulingLock) {
            if (status != Status.PAUSED) return;
            setStatus(Status.ACTIVE);
            schedulingLock.notify();
        }
    }

    protected void abort() {
        synchronized (schedulingLock) {
            setStatus(Status.ABORTED);
            dispose();
        }
    }

    public boolean shouldSaveReplay() {
        return saveReplay;
    }

    public GameResults getGameResults() {
        return gameResults;
    }

    @Override
    public String toString() {
        return "Game{" +
                "status=" + status +
                ", completionListeners=" + completionListeners +
                ", inputGenerator=" + inputGenerator +
                ", animationLogProcessor=" + animationLogProcessor +
                ", gui=" + gui +
                ", gameResults=" + gameResults +
                ", simulation=" + simulation +
                ", state=" + state +
                ", players=" + Arrays.toString(players) +
                ", executor=" + executor +
                ", humanList=" + humanList +
                ", commandQueue=" + commandQueue +
                ", simulationThread=" + simulationThread +
                ", uiMessenger=" + uiMessenger +
                ", pendingShutdown=" + pendingShutdown +
                ", config=" + config +
                '}';
    }
}
