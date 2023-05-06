package com.gats.manager;

import com.gats.simulation.GameState;
import com.gats.ui.menu.gamemodeLayouts.CampaignLayout;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;

public abstract class Run {


    private boolean completed = false;

    private final Object schedulingLock = new Object();
    private final ArrayList<CompletionHandler<Run>> completionListeners = new ArrayList<>();

    protected final Manager manager;

    protected GameState.GameMode gameMode;
    private final ArrayList<Game> games = new ArrayList<>();
    private final ArrayList<Class<? extends Player>> players;

    public Run(Manager manager, RunConfiguration runConfig) {
        this.players = new ArrayList<>(runConfig.players);
        gameMode = runConfig.gameMode;
        this.manager = manager;
    }

    public static Run getRun(Manager manager, RunConfiguration runConfig) {
        switch (runConfig.gameMode) {
            case Campaign:
            case Normal:
            case Christmas:
            case Exam_Admission:
                return new SingleGameRun(manager, runConfig);
            case Tournament_Phase_1:
                return new ParallelMultiGameRun(manager, runConfig);
            case Tournament_Phase_2:
                return new TournamentRun(manager, runConfig);
            default:
                throw new NotImplementedException();
        }
    }

    public ArrayList<Game> getGames() {
        return games;
    }

    protected void addGame(Game game) {
        games.add(game);
        manager.schedule(game);
    }

    public GameState.GameMode getGameMode() {
        return gameMode;
    }

    public boolean isCompleted() {
        return completed;
    }

    public ArrayList<Class<? extends Player>> getPlayers() {
        return players;
    }

    protected void complete() {
        synchronized (schedulingLock) {
            completed = true;
            for (CompletionHandler<Run> completionListener : completionListeners) {
                completionListener.onComplete(this);
            }
        }
    }

    public abstract float[] getScores();

    public void addCompletionListener(CompletionHandler<Run> handler) {
        synchronized (schedulingLock) {
            completionListeners.add(handler);
            if (completed) new Thread(() -> handler.onComplete(this)).start();
        }
    }

    @Override
    public String toString() {
        return "Run{" +
                "completed=" + completed +
                ", completionListeners=" + completionListeners +
                ", gameMode=" + gameMode +
                ", games=" + games +
                ", players=" + players +
                '}';
    }
}
