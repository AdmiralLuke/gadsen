package com.gats.manager;

import bots.MalBot2;
import com.gats.simulation.GameState;
import org.junit.Assert;
import org.junit.Test;

public class TestPolicy {
    private final long COMPLETION_TIMEOUT = 10000;
    final Object lock = new Object();

    @Test
    public void TestMalBot() {
        RunConfiguration config = new RunConfiguration();
        config.gameMode = GameState.GameMode.Normal;
        config.mapName = "MangoMap";
        config.teamCount = 2;
        config.teamSize = 1;
        config.players = Manager.getPlayers(new String[]{"MalBot", "IdleBot"}, true);
        Manager manager = Manager.getManager();
        Run run = manager.startRun(config);
        synchronized (lock) {
            run.addCompletionListener(r -> {
                synchronized (lock) {
                    lock.notify();
                }
            });
            try {
                lock.wait(COMPLETION_TIMEOUT);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        Assert.assertTrue(String.format("The bot should not be able to use any system resources or reflections.\n" +
                "failedExperiments:%s\n" +
                "Var-Dump:%s", MalBot2.failedExperiments, manager), MalBot2.failedExperiments.isEmpty());
    }
}
