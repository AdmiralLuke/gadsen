package com.gats.manager;


import com.gats.simulation.GameState;
import com.gats.simulation.campaign.CampaignResources;

import java.util.*;

public class ParallelMultiGameRun extends Run {


    int completed=0;

    int gameCount =0;

    private final float[] scores;
    private final Map<Game, Integer[]> playerIndices = new HashMap<>();

    protected ParallelMultiGameRun(Manager manager, RunConfiguration runConfig) {
        super(manager, runConfig);
        if (runConfig.gameMode == GameState.GameMode.Exam_Admission){
            if (runConfig.players.size() != 1) {
                System.err.println("Exam Admission only accepts exactly 1 player");
                scores = new float[1];
                complete();
                return;
            }
            //ToDo: replace with enemies
            runConfig.players.add(IdleBot.class);
            runConfig.players.add(IdleBot.class);
            runConfig.players.add(IdleBot.class);
            runConfig.teamCount = runConfig.players.size();
            runConfig.teamSize = 3;
            runConfig.mapName = "MangoMap";
        }
        ArrayList<Integer> indices = new ArrayList<>();
        for (int i = 0; i < runConfig.players.size(); i++) {
            indices.add(i);
        }
        scores = new float[runConfig.players.size()];
        List<List<Integer>> listOfMatchUps = subsetK(indices, runConfig.teamCount);
        List<List<Integer>> permListOfMatchUps = new ArrayList<>();
        for (List<Integer> matchUp : listOfMatchUps) {
            permListOfMatchUps.addAll(permutations(matchUp));
        }
        List<Game> games = new ArrayList<>();
        for (List<Integer> matchUp : permListOfMatchUps
        ) {
            GameConfig cur = new GameConfig(runConfig);
            List<Class<? extends Player>> players = new ArrayList<>();
            for (Integer index : matchUp) {
                players.add(runConfig.players.get(index));
            }
            cur.players = players;
            Game curGame = new Game(cur);
            curGame.addCompletionListener(this::onGameCompletion);
            playerIndices.put(curGame, matchUp.toArray(new Integer[0]));
            gameCount++;
            games.add(curGame);
        }


        for (Game game : games) {
            addGame(game);
        }

    }

    public void onGameCompletion(Game game) {
        Integer[] matchup = playerIndices.get(game);
        int i = 0;
        synchronized (scores){
            for (float score:game.getState().getScores()){
                scores[matchup[i++]] += score;
            }
            completed++;
        }
        if (completed == gameCount){
            for (int j =0; j<scores.length; j++){
                scores[j] /= gameCount;
            }
            if (gameMode == GameState.GameMode.Exam_Admission) scores[0] = scores[0]<333?0:1;
            complete();
        }
    }

    @Override
    public float[] getScores() {
        return scores;
    }


    protected static <T> List<List<T>> subsetK(List<T> list, int subSetSize) {
        ArrayList<List<T>> results = new ArrayList<>();

        int listSize = list.size();
        if (subSetSize > listSize) return results;
        if (subSetSize <= 0) return results;
        if (subSetSize == listSize) {
            results.add(list);
            return results;
        }

        T head = list.remove(0);

        List<List<T>> results1 = subsetK(new ArrayList<>(list), subSetSize);
        results.addAll(results1);
        if (subSetSize == 1) {
            List<T> elemList = new ArrayList<>();
            elemList.add(head);
            results.add(elemList);
        } else {
            List<List<T>> results2 = subsetK(list, subSetSize - 1);
            for (List<T> cur :
                    results2) {
                cur.add(head);
                results.add(cur);
            }
        }
        return results;
    }

    protected static <T> List<List<T>> permutations(List<T> list) {
        int size = 1;
        ArrayList<List<T>> results = new ArrayList<>();
        ListIterator<T> iterator = list.listIterator();
        int i = 1;
        while (iterator.hasNext()) {
            size *= i;
            T next = iterator.next();
            ArrayList<List<T>> newResults = new ArrayList<>(size);
            if (size == 1) {
                ArrayList<T> perm = new ArrayList<>();
                perm.add(next);
                newResults.add(perm);
            } else {
                for (List<T> result : results) {
                    for (int j = 0; j <= result.size(); j++) {
                        newResults.add(insertInCopy(result, next, j));
                    }
                }
            }
            results = newResults;
            i++;
        }
        return results;
    }

    protected static <T> ArrayList<T> insertInCopy(List<T> list, T element, int index) {
        ArrayList<T> copy = new ArrayList<>(list.size()+1);
        ListIterator<T> iter = list.listIterator();
        int i = 0;
        while (iter.hasNext() && i <index){
            copy.add(iter.next());
            i++;
        }
        copy.add(element);
        while (iter.hasNext()){
            copy.add(iter.next());
        }
        return copy;
    }

    @Override
    public String toString() {
        return "ParallelMultiGameRun{" +
                "super=" + super.toString() +
                ", completed=" + completed +
                ", gameCount=" + gameCount +
                ", scores=" + Arrays.toString(scores) +
                ", playerIndices=" + playerIndices +
                '}';
    }
}
