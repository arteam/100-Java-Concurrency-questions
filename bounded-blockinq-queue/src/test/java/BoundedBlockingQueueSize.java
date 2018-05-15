import com.github.arteam.bbqueue.BoundedBlockingQueue;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class BoundedBlockingQueueSize {

    final BlockingQueue<Long> queue = new BoundedBlockingQueue<Long>(10);
    final ThreadLocalRandom random = ThreadLocalRandom.current();

    private void initQueue() {
        for (int i = 0; i < 10; i++) {
            queue.add(random.nextLong());
        }
    }

    @Test
    public void test10Adds() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        for (int i = 0; i < 10; i++) {
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    assertTrue(queue.add(random.nextLong()));
                }
            });
        }
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        assertEquals(queue.size(), 10);
    }

    @Test
    public void test11Adds() throws InterruptedException {
        final AtomicInteger successes = new AtomicInteger(0);
        final AtomicInteger fails = new AtomicInteger(0);
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        for (int i = 0; i < 11; i++) {
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        queue.add(random.nextLong());
                        successes.incrementAndGet();
                    } catch (IllegalStateException e) {
                        fails.incrementAndGet();
                    }
                }
            });
        }
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);


        assertEquals(queue.size(), 10);
        assertEquals(successes.get(), 10);
        assertEquals(fails.get(), 1);
    }

    @Test
    public void test11Offers() throws InterruptedException {
        final AtomicInteger successes = new AtomicInteger(0);
        final AtomicInteger fails = new AtomicInteger(0);
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        for (int i = 0; i < 11; i++) {
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    boolean offered = queue.offer(random.nextLong());
                    if (offered) {
                        successes.incrementAndGet();
                    } else {
                        fails.incrementAndGet();
                    }
                }
            });
        }
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        assertEquals(queue.size(), 10);
        assertEquals(successes.get(), 10);
        assertEquals(fails.get(), 1);
    }


    @Test
    public void testPut() throws InterruptedException {
        initQueue();

        ExecutorService putExecutor = Executors.newFixedThreadPool(2);
        for (int i = 0; i < 10; i++) {
            putExecutor.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        queue.put(random.nextLong());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        ExecutorService removeExecutor = Executors.newFixedThreadPool(2);
        for (int i = 0; i < 10; i++) {
            removeExecutor.submit(new Runnable() {
                @Override
                public void run() {
                    queue.remove();
                }
            });
        }

        putExecutor.shutdown();
        removeExecutor.shutdown();

        putExecutor.awaitTermination(5, TimeUnit.MINUTES);
        removeExecutor.awaitTermination(5, TimeUnit.MINUTES);

        assertEquals(queue.size(), 10);
    }

    @Test
    public void testOfferWithTimeout() throws InterruptedException {
        initQueue();

        boolean offered = queue.offer(random.nextLong(), 10, TimeUnit.MILLISECONDS);
        Assert.assertFalse(offered);
    }


    @Test
    public void test11Removes() throws InterruptedException {
        initQueue();

        final AtomicInteger successes = new AtomicInteger(0);
        final AtomicInteger fails = new AtomicInteger(0);
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        for (int i = 0; i < 11; i++) {
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        queue.remove();
                        successes.incrementAndGet();
                    } catch (IllegalStateException e) {
                        fails.incrementAndGet();
                    }
                }
            });
        }
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        assertEquals(queue.size(), 0);
        assertEquals(successes.get(), 10);
        assertEquals(fails.get(), 1);
    }

    @Test
    public void testPoll() throws InterruptedException {
        initQueue();

        final AtomicInteger successes = new AtomicInteger(0);
        final AtomicInteger fails = new AtomicInteger(0);
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        for (int i = 0; i < 11; i++) {
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    Long result = queue.poll();
                    if (result != null) {
                        successes.incrementAndGet();
                    } else {
                        fails.incrementAndGet();
                    }
                }
            });
        }
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        assertEquals(queue.size(), 0);
        assertEquals(successes.get(), 10);
        assertEquals(fails.get(), 1);
    }

    @Test
    public void testPollWithTimeout() throws Exception {
        assertNull(queue.poll(10, TimeUnit.MILLISECONDS));
    }

    @Test
    public void testTake() throws InterruptedException {
        ExecutorService takeExecutor = Executors.newFixedThreadPool(2);
        for (int i = 0; i < 10; i++) {
            takeExecutor.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        queue.take();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        ExecutorService putExecutor = Executors.newFixedThreadPool(2);
        for (int i = 0; i < 10; i++) {
            putExecutor.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        queue.put(random.nextLong());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        takeExecutor.shutdown();
        putExecutor.shutdown();

        takeExecutor.awaitTermination(5, TimeUnit.MINUTES);
        putExecutor.awaitTermination(5, TimeUnit.MINUTES);

        assertEquals(queue.size(), 0);
    }

}
