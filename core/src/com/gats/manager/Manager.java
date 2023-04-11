package com.gats.manager;

import com.gats.simulation.GameState;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;


public class Manager {

    private static final Manager singleton = new Manager();
    private boolean pendingShutdown = false;

    private ArrayList<Game> games = new ArrayList<>();

    public static Manager getManager() {
        return singleton;
    }

    public ArrayList<Game> schedule(RunConfiguration runConfiguration) {
        ArrayList<Game> games = new ArrayList<>();

        switch (runConfiguration.gameMode){
            case Normal:
            case Christmas:
                //ToDo implement differences
            case Campaign:
            case Exam_Admission:
                GameConfig config = new GameConfig(runConfiguration);
                games.add(new Game(config));
                break;
            case Tournament_Phase_1:

                break;
            case Tournament_Phase_2:
            default:
                throw new NotImplementedException();
        }
        this.games.addAll(games);
        games.forEach(Game::start);
        return games;
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
    }

    public void dispose() {
        //Shutdown all running threads
        pendingShutdown = true;
        for (Game cur :
                games) {
            cur.dispose();
        }
    }

}
