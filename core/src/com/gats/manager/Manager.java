package com.gats.manager;

import com.gats.manager.command.Command;
import com.gats.manager.command.EndTurnCommand;
import com.gats.simulation.*;
import com.gats.ui.GameSettings;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import com.gats.simulation.GameCharacterController;
import com.gats.simulation.GameState;
import com.gats.simulation.Simulation;
import jdk.internal.loader.ClassLoaders;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Manager {

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
    private List<HumanPlayer> humanList = new ArrayList<>();

    private BlockingQueue<Command> commandQueue = new ArrayBlockingQueue<>(128);

    /**
     * Initializes simulation and players before
     * starting the asynchronous execution
     *
     * @param config The execution-parameters of the simulation
     */
    public Manager(RunConfiguration config) {
        simulation = new Simulation(config.gameMode, config.mapName, config.teamCount, config.teamSize);
        state = simulation.getState();
        gui = config.gui;
        animationLogProcessor = config.animationLogProcessor;

        players = new Player[config.teamCount];

        for (int i = 0; i<config.teamCount; i++) {
            final Player curPlayer;
            try {
                players[i] = (Player) config.players.get(i).getDeclaredConstructors()[0].newInstance();
                curPlayer = players[i];
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            switch (curPlayer.getType()) {
                case Human:
                    humanList.add((HumanPlayer) curPlayer);
                    break;
                case AI:

                    Future<?> future = executor.submit(new Runnable() {
                        @Override
                        public void run() {
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

                        System.out.println("bot" + i + "(" + curPlayer.getName()
                                + ") initialization surpassed timeout");
                    }
                    break;
            }
        }
    }

    public void start() {
        //Run the Game
        new Thread(this::run).start();
    }

    public static class NamedPlayerClass{
        private String name;
        private Class<? extends Player> classRef;

        @Override
        public String toString() {
            return name;
        }

        public NamedPlayerClass(Class<? extends Player> classRef) {
            try {

                Method method = classRef.getMethod("getName");

                name = (String) method.invoke(null);

            } catch (IllegalAccessException e) {
                throw new RuntimeException("Insufficient Privileges for instantiating bots", e);
            } catch (InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }

            this.classRef = classRef;


        }

        public String getName() {
            return name;
        }

        public Class<? extends Player> getClassRef() {
            return classRef;
        }
    }

    public static NamedPlayerClass[] getPossiblePlayers(){
        List<NamedPlayerClass> players = new ArrayList<NamedPlayerClass>();
        players.add(new NamedPlayerClass(HumanPlayer.class));
        File botDir = new File("bots");
        if (botDir.exists()){
            try {
                URL url = botDir.toURI().toURL();
                URL[] urls = new URL[] {url};
                ClassLoader loader = new URLClassLoader(urls);
                for (File botFile: Objects.requireNonNull(botDir.listFiles(pathname -> pathname.getName().endsWith(".class")))
                     ) {
                    try {
                        players.add(new NamedPlayerClass((Class<? extends Player>) loader.loadClass("bots." + botFile.getName())));
                    } catch (ClassNotFoundException e) {
                        System.err.println("Could not find class for " + botFile.getName());
                    }
                }
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }
        NamedPlayerClass[] array = new NamedPlayerClass[players.size()];
        players.toArray(array);
        return array;
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
                controller.deactivate();
                ActionLog finalLog = simulation.endTurn();
                if (gui) animationLogProcessor.animate(finalLog);
            }
        });
        thread.start();
    }

    public List<HumanPlayer> getHumanList() {
        return humanList;
    }

    protected void queueCommand(Command cmd) {
        commandQueue.add(cmd);
    }

}
