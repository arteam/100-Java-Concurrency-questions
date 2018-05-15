package com.github.arteam.threadpool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RunnableFuture;

public class PoorManThreadExecutor {

    private List<Thread> threads;
    private BlockingQueue<Runnable> tasks = new LinkedBlockingQueue<>();
    private boolean isShutdown = false;

    public PoorManThreadExecutor(int amountThreads) {
        threads = new ArrayList<>(amountThreads);
        for (int i = 0; i < amountThreads; i++) {
            Thread thread = new Thread(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        tasks.poll().run();
                    } catch (Exception ignore) {
                    }
                }
            });
            thread.start();
            threads.add(thread);
        }
    }

    public <T> Future<T> submit(Callable<T> task) {
        if (isShutdown) {
            throw new IllegalStateException("Pool has been shut down");
        }
        RunnableFuture<T> futureTask = new FutureTask<>(task);
        execute(futureTask);
        return futureTask;
    }

    public Future<Void> submit(Runnable task) {
        if (isShutdown) {
            throw new IllegalStateException("Pool has been shut down");
        }
        RunnableFuture<Void> futureTask = new FutureTask<>(task, null);
        execute(futureTask);
        return futureTask;
    }

    public void execute(Runnable command) {
        tasks.add(command);
    }

    public void shutdown() {
        isShutdown = true;
    }

    public List<Runnable> shutdownNow() {
        isShutdown = true;
        for (Thread thread : threads) {
            thread.interrupt();
        }
        return new ArrayList<>(tasks);
    }

    public boolean isTerminated() {
        return threads.stream().allMatch(t -> t.getState() == Thread.State.TERMINATED);
    }
}
