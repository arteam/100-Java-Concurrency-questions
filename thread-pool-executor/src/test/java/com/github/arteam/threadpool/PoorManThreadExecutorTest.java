package com.github.arteam.threadpool;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PoorManThreadExecutorTest {

    private final String text = "His fool's heart was persuaded, and he took his bow from its case.\n" +
            "This bow was made from the horns of a wild ibex which he had killed\n" +
            "as it was bounding from a rock; he had stalked it, and it had fallen\n" +
            "as the arrow struck it to the heart. Its horns were sixteen palms\n" +
            "long, and a worker in horn had made them into a bow, smoothing them\n" +
            "well down, and giving them tips of gold. When Pandarus had strung\n" +
            "his bow he laid it carefully on the ground, and his brave followers\n" +
            "held their shields before him lest the Achaeans should set upon him\n" +
            "before he had shot Menelaus. Then he opened the lid of his quiver\n" +
            "and took out a winged arrow that had yet been shot, fraught with the\n" +
            "pangs of death. He laid the arrow on the string and prayed to Lycian\n" +
            "Apollo, the famous archer, vowing that when he got home to his strong\n" +
            "city of Zelea he would offer a hecatomb of firstling lambs in his\n" +
            "honour. He laid the notch of the arrow on the oxhide bowstring, and\n" +
            "drew both notch and string to his breast till the arrow-head was near\n" +
            "the bow; then when the bow was arched into a half-circle he let fly,\n" +
            "and the bow twanged, and the string sang as the arrow flew gladly\n" +
            "on over the heads of the throng.";
    private PoorManThreadExecutor executor = new PoorManThreadExecutor(4);

    @Test
    public void testRunExecutor() throws Exception {
        int length = 0;
        for (String word : text.split("\\W")) {
            length += executor.submit(word::length).get();
        }
        System.out.println(length);
        executor.shutdownNow();
        assertTrue(executor.isShutdown());
    }

    @Test
    public void testExecuteAndWait() throws Exception {
        for (String word : text.split("\\W")) {
            executor.execute(() -> System.out.println(word.length()));
        }
        executor.shutdown();
        boolean terminated = executor.awaitTermination(1, TimeUnit.SECONDS);
        assertTrue(terminated);
        assertTrue(executor.isTerminated());
    }

    @Test
    public void testSubmitRunnableAndWait() throws Exception {
        executor.submit(() -> System.out.println(text.length())).get(1, TimeUnit.SECONDS);
        executor.shutdownNow();
    }

    @Test
    public void testExceptionsAreIgnored() throws Exception {
        for (String word : text.split("\\W")) {
            executor.submit(() -> {
                if (word.length() % 2 == 0) {
                    throw new IllegalStateException("Even");
                }
                System.out.println(word.length());
            });
        }
        executor.shutdown();
        boolean terminated = executor.awaitTermination(1, TimeUnit.SECONDS);
        assertTrue(terminated);
        assertTrue(executor.isTerminated());
    }

    @Test
    public void testNotShutdownByDefault() throws Exception {
        assertFalse(executor.isShutdown());
        executor.shutdown();
        assertTrue(executor.isShutdown());
    }
}
