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
import java.util.concurrent.atomic.AtomicInteger;

import com.gats.manager.Executable.Status;

public class Game extends Executable {


    protected static final ThreadGroup PLAYER_THREAD_GROUP = new ThreadGroup("players");


    protected final Object schedulingLock = new Object();
    private static final int AI_EXECUTION_TIMEOUT = 500;
    private static final int AI_EXECUTION_GRACE_PERIODE = 100;
    private static final int AI_INIT_TIMEOUT = 1000;
    private static final int AI_CONTROLLER_USES = 200;

    private static final int HUMAN_EXECUTION_TIMEOUT = 30000;
    private static final int HUMAN_EXECUTION_GRACE_PERIODE = 5000;
    private static final int HUMAN_INIT_TIMEOUT = 30000;
    private static final int HUMAN_CONTROLLER_USES = 100000;

    private static final boolean isDebug;

    static {
        isDebug = java.lang.management.ManagementFactory.getRuntimeMXBean().getInputArguments().toString().contains("-agentlib:jdwp");
        if (isDebug) System.err.println("Warning: Debugger engaged; Disabling Bot-Timeout!");

    }

    private GameResults gameResults;
    private Simulation simulation;
    private GameState state;
    private Player[] players;

    private float[] scores;

    private static final AtomicInteger gameNumber = new AtomicInteger(0);

    private BotThread executor;
    private final List<HumanPlayer> humanList = new ArrayList<>();

    private final BlockingQueue<Command> commandQueue = new ArrayBlockingQueue<>(256);
    private Thread simulationThread;


    protected Game(GameConfig config) {
        super(config);
        if (config.gameMode == GameState.GameMode.Campaign) {
            if (config.players.size() != 1) {
                System.err.println("Campaign only accepts exactly 1 player");
                setStatus(Status.ABORTED);
            }
            config.players.addAll(CampaignResources.getEnemies(config.mapName));
            config.teamCount = config.players.size();
            config.teamSize = CampaignResources.getCharacterCount(config.mapName);
        }
        gameResults = new GameResults(config);
        gameResults.setStatus(getStatus());
    }

    private void create() {

        simulation = new Simulation(config.gameMode, config.mapName, config.teamCount, config.teamSize);
        state = simulation.getState();
        if (saveReplay)
            gameResults.setInitialState(state);

        players = new Player[config.teamCount];

        for (int i = 0; i < config.teamCount; i++) {
            final Player curPlayer;
            executor.waitForCompletion();
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
                    Future<?> future = executor.execute(() -> {
                        Thread.currentThread().setName("Init_Thread_Player_" + curPlayer.getName());
                        ((Bot) curPlayer).setRnd(Manager.getSeed());
                        curPlayer.init(state);
                    });
                    try {
                        if (isDebug) future.get();
                        else
                            future.get(AI_INIT_TIMEOUT, TimeUnit.MILLISECONDS);
                    } catch (InterruptedException e) {
                        System.out.println("bot was interrupted");
                    } catch (ExecutionException e) {
                        System.out.println("bot failed initialization with exception: " + e.getCause());
                    } catch (TimeoutException e) {
                        future.cancel(true);
                        executor.forceStop();

                        System.out.println("bot" + i + "(" + curPlayer.getName() + ") initialization surpassed timeout");
                    }
                    break;
            }
        }
        gameResults.setPlayerNames(getPlayerNames());
        gameResults.setSkins(getSkins(players));
        config = null;
    }

    public void start() {
        synchronized (schedulingLock) {
            if (getStatus() == Status.ABORTED) return;
            setStatus(Status.ACTIVE);
            executor = new BotThread();
            create();
            //Init the Log Processor
            if (gui) animationLogProcessor.init(state.copy(), getPlayerNames(), getSkins(players));
            //Run the Game
            simulationThread = new Thread(this::run);
            simulationThread.setName("Game_Simulation_Thread");
            simulationThread.setUncaughtExceptionHandler(this::crashHandler);
            simulationThread.start();
        }
    }

    private String[][] getSkins(Player[] players) {
        String[][] skins = new String[players.length][simulation.getState().getCharactersPerTeam()];
        for (int i = 0; i < players.length; i++)
            for (int j = 0; j < simulation.getState().getCharactersPerTeam(); j++)
                skins[i][j] = players[i].getSkin(j);
        return skins;
    }

    @Override
    protected void setStatus(Status newStatus) {
        super.setStatus(newStatus);
        if (gameResults!= null) gameResults.setStatus(newStatus);
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
        Thread.currentThread().setName("Game_Thread_" + gameNumber.getAndIncrement());
        while (!pendingShutdown && state.isActive()) {
            synchronized (schedulingLock) {
                if (getStatus() == Status.PAUSED)
                    try {
                        schedulingLock.wait();

                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
            }

            ActionLog firstLog = simulation.clearAndReturnActionLog();
            if (saveReplay)
                gameResults.addActionLog(firstLog);
            if (gui) {
                animationLogProcessor.animate(firstLog);
            }

            GameCharacterController gcController = simulation.getController();
            int currentPlayerIndex = gcController.getGameCharacter().getTeam();
            int currentCharacterIndex = gcController.getGameCharacter().getTeamPos();

            Player currentPlayer = players[currentPlayerIndex];
            GameState stateCopy = state.copy();
            Controller controller = new Controller(this, gcController, stateCopy, currentPlayer.getType() == Player.PlayerType.Human ? HUMAN_CONTROLLER_USES : AI_CONTROLLER_USES);

            executor.waitForCompletion();
            Thread futureExecutor;
            Future<?> future;
            switch (currentPlayer.getType()) {
                case Human:
                    future = executor.execute(() -> {
                        Thread.currentThread().setName("Run_Thread_Player_Human");
                        simulation.setTurnTimer(new Timer(1000 * HUMAN_EXECUTION_TIMEOUT));
                        currentPlayer.executeTurn(stateCopy, controller);
                    });
                    futureExecutor = new Thread(() -> {
                        inputGenerator.activateTurn((HumanPlayer) currentPlayer);
                        try {
                            Thread.currentThread().setName("Future_Executor_Player_Human");
                            if (isDebug) future.get();
                            else
                                future.get(HUMAN_EXECUTION_TIMEOUT + HUMAN_EXECUTION_GRACE_PERIODE, TimeUnit.MILLISECONDS);
                        } catch (InterruptedException e) {
                            future.cancel(true);//Executor was interrupted: Interrupt Player
                            System.out.println("bot was interrupted");
                            e.printStackTrace(System.err);
                        } catch (ExecutionException e) {
                            System.err.println("human player failed with exception: " + e.getCause());
                            e.printStackTrace();
                        } catch (TimeoutException e) {
                            future.cancel(true);
                            executor.forceStop();
                            System.err.println("player" + currentPlayerIndex + "(" + currentPlayer.getName() + ") computation surpassed timeout");
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
                    future = executor.execute(() -> {
                        Thread.currentThread().setName("Run_Thread_Player_" + currentPlayer.getName());
                        simulation.setTurnTimer(new Timer(1000 * AI_EXECUTION_TIMEOUT));
                        currentPlayer.executeTurn(stateCopy, controller);
                    });
                    futureExecutor = new Thread(() -> {
                        Thread.currentThread().setName("Future_Executor_Player_" + currentPlayer.getName());
                        try {
                            if (isDebug) future.get();
                            else
                                future.get(AI_EXECUTION_TIMEOUT + AI_EXECUTION_GRACE_PERIODE, TimeUnit.MILLISECONDS);
                        } catch (InterruptedException e) {
                            future.cancel(true);//Executor was interrupted: Interrupt Bot
                            System.out.println("bot was interrupted");
                            e.printStackTrace(System.err);
                        } catch (ExecutionException e) {
                            System.out.println("bot failed with exception: " + e.getCause());
                            e.printStackTrace();
                            System.err.println("The failed player has been penalized!");
                            simulation.penalizeCurrentPlayer();
                        } catch (TimeoutException e) {
                            future.cancel(true);
                            executor.forceStop();

                            System.out.println("player" + currentPlayerIndex + "(" + currentPlayer.getName() + ") computation surpassed timeout");
                            System.err.println("The failed player has been penalized!");
                            simulation.penalizeCurrentPlayer();
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
            if (saveReplay)
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
                    if (saveReplay)
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
            if (saveReplay)
                gameResults.addActionLog(finalLog);
            if (gui) {
                animationLogProcessor.animate(finalLog);
                animationLogProcessor.awaitNotification();
            }
            if (pendingShutdown) {
                executor.shutdown();
                futureExecutor.interrupt();
                break;
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
        scores = state.getScores();
        setStatus(Status.COMPLETED);
        for (CompletionHandler<Executable> completionListener : completionListeners) {
            completionListener.onComplete(this);
        }
    }

@Override
    public void dispose() {
        //Shutdown all running threads
        super.dispose();
        if (simulationThread != null) {
            simulationThread.interrupt();
        }
        if (executor!= null)
            executor.shutdown();
        if (state!=null) scores = state.getScores();
        simulation = null;
        state = null;
        executor = null;
        simulationThread = null;
        gameResults = null;
    }

    public List<HumanPlayer> getHumanList() {
        return humanList;
    }

    protected void queueCommand(Command cmd) {
        commandQueue.add(cmd);
    }

    protected String[] getPlayerNames() {
        String[] names = new String[players.length];
        int i = 0;
        for (Player p : players) {
            names[i] = p.getName();
            i++;
        }
        return names;

    }

    public float[] getScores() {
        return scores;
    }

    public boolean shouldSaveReplay() {
        return super.saveReplay;
    }

    public GameResults getGameResults() {
        return gameResults;
    }

    @Override
    public String toString() {
        return "Game{" +
                "status=" + getStatus() +
                ", completionListeners=" + super.completionListeners +
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
