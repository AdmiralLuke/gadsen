package com.gats.manager;

public class SingleGameRun extends Run{

    private float[] scores;


    public SingleGameRun(Manager manager, RunConfiguration runConfig) {
        super(manager, runConfig);
        Game game = new Game(new GameConfig(runConfig));
        game.addCompletionListener(this::onGameCompletion);
        addGame(game);
    }

    public void onGameCompletion(Game game){
        if (isCompleted()) throw new RuntimeException("In a single game run only one game may complete");
        scores = game.getState().getScores();
        complete();
    }


    @Override
    public float[] getScores() {
        return scores;
    }


}
