package com.gats.manager;

import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class BotThread {

    private static final String namePrefix = "BotThread";
    private static final AtomicInteger threadNumber = new AtomicInteger(1);

    private final ReentrantLock mainLock = new ReentrantLock();

    private final Object lock = new Object();

    private Thread worker;

    private FutureTask<?> target = null;

    public BotThread() {
        worker = new Thread(Game.PLAYER_THREAD_GROUP, this::waitAndExecute);
        worker.start();
    }

    private void waitAndExecute() {
        while (!Thread.interrupted()) {
            synchronized (lock) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    break;
                }
            }
            target.run();
            target = null;
        }
    }

    public Future<?> execute(Runnable runnable){
        if (target != null) return null;
        target = new FutureTask<>(runnable, null);
        synchronized (lock) {
            lock.notify();
        }
        return target;
    }

    public void forceStop(){
        worker.stop();
        target.cancel(true);
        target = null;
        worker = new Thread(Game.PLAYER_THREAD_GROUP, this::waitAndExecute);
        worker.start();
    }

    public void shutdown(){
        target.cancel(true);
        worker.interrupt();
    }

}
