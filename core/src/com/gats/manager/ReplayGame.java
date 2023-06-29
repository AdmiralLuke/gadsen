package com.gats.manager;

import com.gats.simulation.Simulation;
import com.gats.simulation.action.Action;
import com.gats.simulation.action.ActionLog;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * This is more a workaround, ReplayGame and Game should really be implementing a common Interface
 */
public class ReplayGame extends Executable{

    private GameResults replay = null;
    private Thread executionThread;

    protected ReplayGame(GameConfig config) {
        super(config);
        if (!config.gui) {
            System.err.println("Replays require a gui");
            abort();
        }
        loadGameResults(config.mapName);
    }

    private void loadGameResults(String path){
        try (FileInputStream fs = new FileInputStream(path)) {
            this.replay = (GameResults) new ObjectInputStream(fs).readObject();
        } catch (IOException e) {
            System.err.printf("Unable to read replay at %s %n", path);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void start() {
        synchronized (schedulingLock) {
        if (getStatus() == Status.ABORTED) return;
        setStatus(Status.ACTIVE);
        //Init the Log Processor
        animationLogProcessor.init(replay.getInitialState().copy(), getPlayerNames(), getSkins());
        //Run the Game
            executionThread = new Thread(this::run);
            executionThread.setName("Replay_Execution_Thread");
            executionThread.setUncaughtExceptionHandler(this::crashHandler);
            executionThread.start();
    }
    }

    private void run(){
        Iterator<ActionLog> actionLogs = replay.getActionLogs().iterator();
        while (!pendingShutdown && !actionLogs.hasNext()) {
            synchronized (schedulingLock) {
                if (getStatus() == Status.PAUSED)
                    try {
                        schedulingLock.wait();

                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                animationLogProcessor.animate(actionLogs.next());
                animationLogProcessor.awaitNotification();
            }

        }
        setStatus(Status.COMPLETED);
        for (CompletionHandler<Executable> completionListener : completionListeners) {
            completionListener.onComplete(this);
        }
    }

    private String[][] getSkins(){
        return replay.getSkins();
    }

    @Override
    protected String[] getPlayerNames() {
        return replay.getPlayerNames();
    }

    @Override
    public void dispose() {
        super.dispose();
        if (executionThread != null) {
            executionThread.interrupt();
        }
    }

    public boolean shouldSaveReplay() {
        return false;
    }

    @Override
    public GameResults getGameResults() {
        throw new RuntimeException("Replays dont produce GameResults!");
    }
}
