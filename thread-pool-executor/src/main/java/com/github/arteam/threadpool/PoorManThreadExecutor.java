package com.github.arteam.threadpool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class PoorManThreadExecutor {

    private final List<Thread> threads;
    private final BlockingQueue<Runnable> tasks = new LinkedBlockingQueue<>();
    private volatile boolean isShutdown = false;

    public PoorManThreadExecutor(int amountThreads) {
        threads = new ArrayList<>(amountThreads);
        for (int i = 0; i < amountThreads; i++) {
            Thread thread = new Thread(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    Runnable task = tasks.poll();
                    if (task == null) {
                        if (isShutdown) {
                            break;
                        }
                        try {
                            task = tasks.take();
                        } catch (InterruptedException e) {
                            break;
                        }
                    }
                    try {
                        task.run();
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

    public void shutdownNow() {
        isShutdown = true;
        for (Thread thread : threads) {
            thread.interrupt();
        }
    }

    public boolean isShutdown() {
        return isShutdown;
    }

    public boolean isTerminated() {
        return threads.stream().allMatch(t -> t.getState() == Thread.State.TERMINATED);
    }

    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        long deadline = System.currentTimeMillis() + unit.toMillis(timeout);
        while (System.currentTimeMillis() < deadline) {
            if (isTerminated()) {
                return true;
            }
            LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(100));
        }
        return isTerminated();
    }
}
