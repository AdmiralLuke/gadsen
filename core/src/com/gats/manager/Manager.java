package com.gats.manager;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;


public class Manager {

    private static int systemReservedProcessorCount = 2;
    private static final Manager singleton = new Manager();
    private boolean pendingShutdown = false;

    private final Thread executionManager;

    private ThreadPoolExecutor threadPoolExecutor;

    private final ArrayList<Game> games = new ArrayList<>();
    private final ArrayList<Game> scheduledGames = new ArrayList<>();

    private final ArrayList<Game> activeGames = new ArrayList<>();

    private final ArrayList<Game> pausedGames = new ArrayList<>();

    private final ArrayList<Game> completedGames = new ArrayList<>();

    private final Object schedulingLock = new Object();


    public static Manager getManager() {
        return singleton;
    }

    public Run startRun(RunConfiguration runConfiguration) {
        return Run.getRun(this, runConfiguration);
    }

    private void executionManager() {
        while (!Thread.interrupted()) {
            try {
                synchronized (executionManager) {
                    executionManager.wait(5000);
                }
            } catch (InterruptedException e) {
                System.out.println("ExecutionManager shutting down");
                break;
            }
            int threadLimit = Runtime.getRuntime().availableProcessors() - systemReservedProcessorCount;
            synchronized (schedulingLock) {
                int runningThreads = activeGames.size() * Game.REQUIRED_THREAD_COUNT;
                if (runningThreads > threadLimit) {
                    while (runningThreads > threadLimit) {
                        if (activeGames.size() == 0) {
                            System.err.println("Warning: System-reserved Processor Count exceeds physical limit. Simulation offline!");
                            break;
                        }
                        Game game = activeGames.remove(activeGames.size() - 1);
                        game.pause();
                        pausedGames.add(game);
                        runningThreads -= 2;
                    }
                } else while (runningThreads + 2 <= threadLimit) {
                    if (pausedGames.size() > 0) {
                        Game game = pausedGames.remove(pausedGames.size() - 1);
                        game.resume();
                        activeGames.add(game);
                    } else if (scheduledGames.size() > 0) {
                        Game game = scheduledGames.remove(scheduledGames.size() - 1);
                        game.start();
                        activeGames.add(game);
                    } else {
                        break;
                    }
                    runningThreads += 2;
                }
            }
        }
    }

    protected void schedule(Game game) {
        synchronized (schedulingLock) {
            if (pendingShutdown) return;
            game.addCompletionListener(this::notifyExecutionManager);
            games.add(game);
            game.schedule();
            scheduledGames.add(game);
        }
        synchronized (executionManager) {
            executionManager.notify();
        }
    }


    private void notifyExecutionManager(Game game) {
        synchronized (schedulingLock) {
            activeGames.remove(game);
            completedGames.add(game);
        }
        executionManager.notify();
    }

    public void stop(Run run) {
        for (Game game : run.getGames()) {
            stop(game);
        }
    }

    protected void stop(Game game) {
        synchronized (schedulingLock) {
            synchronized (game.schedulingLock) {
                switch (game.getStatus()) {
                    case SCHEDULED:
                        scheduledGames.remove(game);
                        break;
                    case ACTIVE:
                        activeGames.remove(game);
                        break;
                    case PAUSED:
                        pausedGames.remove(game);
                        break;
                }
                completedGames.add(game);
                game.abort();
            }
        }
    }

    public static class NamedPlayerClass {
        private String name;
        private Class<? extends Player> classRef;
        private String fileName;

        @Override
        public String toString() {
            return name;
        }

        public NamedPlayerClass(Class<? extends Player> classRef, String fileName) {
            try {

                Player playerInstance = classRef.getConstructor(new Class[]{}).newInstance();

                name = playerInstance.getName();

                this.fileName = fileName;

            } catch (IllegalAccessException e) {
                throw new RuntimeException("Insufficient Privileges for instantiating bots", e);
            } catch (InvocationTargetException | NoSuchMethodException | InstantiationException e) {
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

    public static NamedPlayerClass[] getPossiblePlayers() {
        List<NamedPlayerClass> players = new ArrayList<NamedPlayerClass>();
        players.add(new NamedPlayerClass(HumanPlayer.class, "HumanPlayer"));
        players.add(new NamedPlayerClass(IdleBot.class, "IdleBot"));
        players.add(new NamedPlayerClass(TestBot.class, "TestBot"));
        File botDir = new File("bots");
        if (botDir.exists()) {
            try {
                URL url = new File(".").toURI().toURL();
                URL[] urls = new URL[]{url};
                ClassLoader loader = new URLClassLoader(urls);
                for (File botFile : Objects.requireNonNull(botDir.listFiles(pathname -> pathname.getName().endsWith(".class")))) {
                    try {
                        Class<?> nextClass = loader.loadClass("bots." + botFile.getName().replace(".class", ""));
                        if (Bot.class.isAssignableFrom(nextClass))
                            players.add(new NamedPlayerClass((Class<? extends Player>) nextClass, botFile.getName().replace(".class", "")));

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

    public static ArrayList<Class<? extends Player>> getPlayers(String[] names, boolean noGUI) {
        NamedPlayerClass[] allPlayers = getPossiblePlayers();
        ArrayList<Class<? extends Player>> selectedPlayers = new ArrayList<>();
        for (String cur : names) {
            boolean playerFound = false;
            for (NamedPlayerClass candidate : allPlayers) {
                if (candidate.fileName.equals(cur)) {
                    if (noGUI && candidate.classRef.equals(HumanPlayer.class))
                        throw new RuntimeException("Human Players cannot be used with option --nogui");
                    selectedPlayers.add(candidate.classRef);
                    playerFound = true;
                    break;
                }
            }
            if (!playerFound) throw new RuntimeException(String.format("Couldn't find bots.%s.class", cur));
        }
        return selectedPlayers;
    }

    private Manager() {
        executionManager = new Thread(this::executionManager);
        executionManager.start();
    }

    public void dispose() {
        //Shutdown all running threads
        pendingShutdown = true;
        executionManager.interrupt();
        synchronized (games) {
            for (Game cur :
                    games) {
                cur.dispose();
            }
        }
    }

    public static int getSystemReservedProcessorCount() {
        return systemReservedProcessorCount;
    }

    public static void setSystemReservedProcessorCount(int systemReservedProcessorCount) {
        Manager.systemReservedProcessorCount = systemReservedProcessorCount;
        getManager().executionManager.notify();
    }

    @Override
    public String toString() {
        return "Manager{" +
                "pendingShutdown=" + pendingShutdown +
                ", executionManager=" + executionManager +
                ", threadPoolExecutor=" + threadPoolExecutor +
                ", games=" + games +
                ", scheduledGames=" + scheduledGames +
                ", activeGames=" + activeGames +
                ", pausedGames=" + pausedGames +
                ", completedGames=" + completedGames +
                '}';
    }
}
