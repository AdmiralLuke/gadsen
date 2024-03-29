package com.gats.manager;

import jdk.internal.reflect.Reflection;
import sun.security.util.SecurityConstants;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

public class Manager {

    public static final RuntimePermission CHECK_MANAGER_ACCESS_PERMISSION =
            new RuntimePermission("accessManagerInstance");
    private static final String RESULT_DIR_NAME = "results";
    private static final File RESULT_DIR = new File(RESULT_DIR_NAME);
    private static int systemReservedProcessorCount = 2;
    private static final Manager singleton = new Manager();
    private boolean pendingShutdown = false;
    private int writtenFiles = 0;

    private final Thread executionManager;

    private ThreadPoolExecutor threadPoolExecutor;

    private final ArrayList<Executable> games = new ArrayList<>();
    private final ArrayList<Executable> scheduledGames = new ArrayList<>();

    private final ArrayList<Executable> activeGames = new ArrayList<>();

    private final ArrayList<Executable> pausedGames = new ArrayList<>();

    private final ArrayList<Executable> completedGames = new ArrayList<>();

    private final BlockingQueue<GameResults> pendingSaves = new LinkedBlockingQueue<>();

    private final Object schedulingLock = new Object();

    private static long seed = 345342624;

    private static int availableCores = 0;


    @SuppressWarnings("removal")
    public static Manager getManager() {
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            sm.checkPermission(CHECK_MANAGER_ACCESS_PERMISSION);
        }
        return singleton;
    }

    public Run startRun(RunConfiguration runConfiguration) {
        return Run.getRun(this, runConfiguration);
    }

    private void executionManager() {
        Thread.currentThread().setName("Execution_Manager");
        while (!Thread.interrupted()) {
            try {
                synchronized (executionManager) {
                    executionManager.wait(5000);
                }
            } catch (InterruptedException e) {
                System.out.println("ExecutionManager shutting down");
                break;
            }
            int threadLimit = Math.max(Runtime.getRuntime().availableProcessors() - systemReservedProcessorCount, Executable.REQUIRED_THREAD_COUNT);
            if (threadLimit != availableCores) {
                availableCores = threadLimit;
                System.out.printf("Resource load changed to %d cores, adapting...\n", threadLimit);
            }
            synchronized (schedulingLock) {
                int runningThreads = activeGames.size() * Executable.REQUIRED_THREAD_COUNT;
                if (runningThreads > threadLimit) {
                    while (runningThreads > threadLimit) {
                        if (activeGames.size() == 0) {
                            System.err.println("Warning: System-reserved Processor Count exceeds physical limit. Simulation offline!");
                            break;
                        }
                        Executable game = activeGames.remove(activeGames.size() - 1);
                        game.pause();
                        pausedGames.add(game);
                        runningThreads -= 2;
                    }
                } else
                    while (runningThreads + 2 <= threadLimit) {
                        if (pausedGames.size() > 0) {
                            Executable game = pausedGames.remove(pausedGames.size() - 1);
                            game.resume();
                            activeGames.add(game);
                        } else if (scheduledGames.size() > 0) {
                            Executable game = scheduledGames.remove(scheduledGames.size() - 1);
                            try {
                                game.start();
                                activeGames.add(game);
                            } catch (Exception e) {
                                e.printStackTrace();
                                System.err.println("Game crashed on start(); Aborting...\n" + game);
                                game.abort();
                            }
                        } else {
                            break;
                        }
                        runningThreads += 2;
                    }
            }
            while (!pendingSaves.isEmpty()) {
                GameResults results = pendingSaves.poll();
                if (RESULT_DIR.exists() || RESULT_DIR.mkdirs()) {
                    try (FileOutputStream fs = new FileOutputStream(String.format("%s/%s_%d_%d.replay", RESULT_DIR, results.getConfig().gameMode, System.currentTimeMillis(), writtenFiles++))) {
                        new ObjectOutputStream(fs).writeObject(results);
                    } catch (IOException e) {
                        System.err.printf("Unable to save replay at %s/%s_%d_%d.replay %n", RESULT_DIR, results.getConfig().gameMode, System.currentTimeMillis(), writtenFiles);
                    }
                } else System.err.printf("Unable to create results directory at %s %n", RESULT_DIR);
            }
        }
    }

    protected void schedule(Executable game) {
        synchronized (schedulingLock) {
            if (pendingShutdown) return;
            if (game.getStatus() == Executable.Status.ABORTED) return;
            game.addCompletionListener(this::notifyExecutionManager);
            games.add(game);
            game.schedule();
            scheduledGames.add(game);
        }
        synchronized (executionManager) {
            executionManager.notify();
        }
    }


    private void notifyExecutionManager(Executable game) {
        synchronized (schedulingLock) {
            if (!activeGames.remove(game) && !pausedGames.remove(game))
                System.err.printf("Warning unsuccessfully attempted to complete Game %s\nInstance: %s", game, this);
            if (game.shouldSaveReplay()) pendingSaves.add(game.getGameResults());
            completedGames.add(game);
            game.dispose();
        }
        synchronized (executionManager) {
            executionManager.notify();
        }
    }

    public void stop(Run run) {
        run.dispose();
    }

    protected void stop(Executable game) {
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
                    case COMPLETED:
                        return;
                }
                if (game.shouldSaveReplay()) pendingSaves.add(game.getGameResults());
                completedGames.add(game);
                game.abort();
            }
        }
    }

    public static class NamedPlayerClass {
        private final String name;
        private final Class<? extends Player> classRef;
        private final String fileName;

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
        List<NamedPlayerClass> players = new ArrayList<>();
        players.add(new NamedPlayerClass(HumanPlayer.class, "HumanPlayer"));
        players.add(new NamedPlayerClass(IdleBot.class, "IdleBot"));
        players.add(new NamedPlayerClass(TestBot.class, "TestBot"));
        File botDir = new File("bots");
        seed = 345342624;
        System.out.println(new File("").getAbsolutePath());
        if (botDir.exists()) {
            System.out.println("Attempting to load Bots from " + botDir.getAbsolutePath());
            try {
                URL url = new File(".").toURI().toURL();
                URL[] urls = new URL[]{url};
                ClassLoader loader = new URLClassLoader(urls);

                for (File botFile : Objects.requireNonNull(botDir.listFiles(pathname -> pathname.getName().endsWith(".class")))) {
                    try {
                        if (containsIllegalTerms(botFile)) {
                            System.err.printf("File %s contains illegal terms. -> Exclude from Loading%n", botFile);
                            continue;
                        }
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
        } else {
            System.err.println("Warning: No Bot-Dir found at " + botDir.getAbsolutePath());
        }
        NamedPlayerClass[] array = new NamedPlayerClass[players.size()];
        players.toArray(array);
        return array;
    }

    private static boolean containsIllegalTerms(File botFile) {
        if (botFile == null) return false;
        if (!botFile.exists()) return false;
        if (!botFile.isFile()) return false;
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(Files.newInputStream(botFile.toPath())))) {
            String line;
            while ((line = br.readLine()) != null) {
                seed = seed + br.hashCode();
                resultStringBuilder.append(line).append("\n");
            }
        } catch (IOException e) {
            return false;
        }
        String fileContent = resultStringBuilder.toString();

        if (fileContent.contains("java/lang/Thread")) return true;
        if (fileContent.contains("java/util/concurrent/")) return true;

        return false;
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

    @SuppressWarnings({"removal"})
    private Manager() {
        java.security.Policy.setPolicy(new BotSecurityPolicy());
        System.setProperty("java.security.manager", "allow");
        try {
            System.err.println("Please Ignore the following Warning---------------------");
            System.setSecurityManager(new SecurityManager());
            System.err.println("--------------------------------------------------------");
        } catch (UnsupportedOperationException e) {
            System.err.println("--------------------------------------------------------");
            throw e;
        }
        executionManager = new Thread(this::executionManager);
        executionManager.start();
    }

    public void dispose() {
        //Shutdown all running threads
        pendingShutdown = true;
        executionManager.interrupt();
        synchronized (games) {
            for (Executable cur :
                    games) {
                cur.dispose();
            }
        }
    }

    public static long getSeed() {
        return seed;
    }

    public static int getSystemReservedProcessorCount() {
        return systemReservedProcessorCount;
    }

    public static void setSystemReservedProcessorCount(int systemReservedProcessorCount) {
        Manager.systemReservedProcessorCount = systemReservedProcessorCount;
        synchronized (getManager().executionManager) {
            getManager().executionManager.notify();
        }
    }

    @Override
    public String toString() {
        synchronized (schedulingLock) {
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
}
