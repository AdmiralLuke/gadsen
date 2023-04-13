package com.gats.manager;


import com.gats.simulation.GameState;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Collection;

@RunWith(Parameterized.class)
public class TestMultiGameRun {


    private final RunConfiguration runConfig;
    private Run run;
    private Manager manager;

    static class TestExample{
        private RunConfiguration config;

        public TestExample(RunConfiguration config) {
            this.config = config;
        }

    }

    public TestMultiGameRun(TestExample testSet) {
        this.runConfig = testSet.config;
        manager = Manager.getManager();
        run = manager.startRun(testSet.config);
    }


    @Parameterized.Parameters
    public static Collection<TestExample> data() {
        Collection<TestExample> samples = new ArrayList<>();
        RunConfiguration config;

        config = new RunConfiguration();
        config.gameMode = GameState.GameMode.Tournament_Phase_1;
        config.mapName = "map1";
        config.teamCount = 2;
        config.players = new ArrayList<>();
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        samples.add(new TestExample(config));

        config = new RunConfiguration();
        config.gameMode = GameState.GameMode.Tournament_Phase_1;
        config.mapName = "map1";
        config.teamCount = 2;
        config.players = new ArrayList<>();
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        samples.add(new TestExample(config));

        config = new RunConfiguration();
        config.gameMode = GameState.GameMode.Tournament_Phase_1;
        config.mapName = "map1";
        config.teamCount = 2;
        config.players = new ArrayList<>();
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        samples.add(new TestExample(config));

        config = new RunConfiguration();
        config.gameMode = GameState.GameMode.Tournament_Phase_1;
        config.mapName = "map1";
        config.teamCount = 2;
        config.players = new ArrayList<>();
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        samples.add(new TestExample(config));

        config = new RunConfiguration();
        config.gameMode = GameState.GameMode.Tournament_Phase_1;
        config.mapName = "map1";
        config.teamCount = 3;
        config.players = new ArrayList<>();
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        samples.add(new TestExample(config));

        config = new RunConfiguration();
        config.gameMode = GameState.GameMode.Tournament_Phase_1;
        config.mapName = "map1";
        config.teamCount = 3;
        config.players = new ArrayList<>();
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        samples.add(new TestExample(config));

        config = new RunConfiguration();
        config.gameMode = GameState.GameMode.Tournament_Phase_1;
        config.mapName = "map1";
        config.teamCount = 3;
        config.players = new ArrayList<>();
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        samples.add(new TestExample(config));

        config = new RunConfiguration();
        config.gameMode = GameState.GameMode.Tournament_Phase_1;
        config.mapName = "map1";
        config.teamCount = 3;
        config.players = new ArrayList<>();
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        samples.add(new TestExample(config));

        config = new RunConfiguration();
        config.gameMode = GameState.GameMode.Tournament_Phase_1;
        config.mapName = "map1";
        config.teamCount = 4;
        config.players = new ArrayList<>();
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        samples.add(new TestExample(config));

        config = new RunConfiguration();
        config.gameMode = GameState.GameMode.Tournament_Phase_1;
        config.mapName = "map1";
        config.teamCount = 4;
        config.players = new ArrayList<>();
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        samples.add(new TestExample(config));


        config = new RunConfiguration();
        config.gameMode = GameState.GameMode.Tournament_Phase_1;
        config.mapName = "map1";
        config.teamCount = 4;
        config.players = new ArrayList<>();
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        samples.add(new TestExample(config));


        config = new RunConfiguration();
        config.gameMode = GameState.GameMode.Tournament_Phase_1;
        config.mapName = "map1";
        config.teamCount = 4;
        config.players = new ArrayList<>();
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        samples.add(new TestExample(config));


        config = new RunConfiguration();
        config.gameMode = GameState.GameMode.Tournament_Phase_1;
        config.mapName = "map1";
        config.teamCount = 4;
        config.players = new ArrayList<>();
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        samples.add(new TestExample(config));


        config = new RunConfiguration();
        config.gameMode = GameState.GameMode.Tournament_Phase_1;
        config.mapName = "map1";
        config.teamCount = 4;
        config.players = new ArrayList<>();
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        samples.add(new TestExample(config));


        config = new RunConfiguration();
        config.gameMode = GameState.GameMode.Tournament_Phase_1;
        config.mapName = "map1";
        config.teamCount = 4;
        config.players = new ArrayList<>();
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        config.players.add(IdleBot.class);
        samples.add(new TestExample(config));

        return samples;
    }

    @Test
    public void testStats(){
        int expectedCount = binCoeff(run.getPlayers().size(), runConfig.teamCount);
        expectedCount *= factorial(runConfig.teamCount);
        Assert.assertEquals("Run contains the wrong manager instance", manager, run.manager);
        Assert.assertEquals("Number of games doesn't equal to the calculated theoretical amount", expectedCount, run.getGames().size());
        Assert.assertEquals("Player aren't equal to the list specified in config", run.getPlayers(), runConfig.players);
        Assert.assertEquals("Run implementation", run.getClass(), ParallelMultiGameRun.class);
        Assert.assertEquals("Wrong Game mode", run.gameMode, runConfig.gameMode);
    }

    private int binCoeff(int n, int k){
        return (factorial(n)/factorial(k)) / factorial(n-k);
    }

    private int factorial(int n){
        int res = 1;
        for (int i = 1; i <= n; i++) {
            res*= i;
        }
        return res;
    }

}
