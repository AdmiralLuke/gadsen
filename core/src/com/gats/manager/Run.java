package com.gats.manager;

import com.gats.simulation.GameState;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

public abstract class Run {

    interface CompletionHandler{
        void onComplete(Run run);
    }
    private boolean completed = false;
    private ArrayList<CompletionHandler> completionListeners = new ArrayList<>();

    protected final Manager manager;

    protected GameState.GameMode gameMode;
    private final ArrayList<Game> games = new ArrayList<>();
    private ArrayList<Class<? extends Player>> players;

    public Run(Manager manager, RunConfiguration runConfig) {
        this.players = new ArrayList<>(runConfig.players);
        gameMode = runConfig.gameMode;
        this.manager = manager;
    }

    public static Run getRun(Manager manager, RunConfiguration runConfig){
        switch (runConfig.gameMode){
            case Normal:
            case Christmas:
                //ToDo implement differences
            case Campaign:
            case Exam_Admission:
                return new SingleGameRun(manager, runConfig);
            case Tournament_Phase_1:
                return new ParallelMultiGameRun(manager, runConfig);
            case Tournament_Phase_2:
            default:
                throw new NotImplementedException();
        }
    }

    public ArrayList<Game> getGames() {
        return games;
    }

    protected void addGame(Game game){
        games.add(game);
        manager.schedule(game);
    }


    public boolean isCompleted() {
        return completed;
    }

    public ArrayList<Class<? extends Player>> getPlayers(){
        return players;
    }

    protected void complete(){
        completed = true;
        for (CompletionHandler completionListener : completionListeners) {
            completionListener.onComplete(this);
        }
    }

    public abstract Float[] getScores();

    public void addCompletionListener(CompletionHandler handler){
        completionListeners.add(handler);
        if (completed) handler.onComplete(this);
    }
}
