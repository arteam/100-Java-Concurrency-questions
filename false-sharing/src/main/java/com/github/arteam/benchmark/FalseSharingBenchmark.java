package com.github.arteam.benchmark;

import com.github.arteam.data.ContentedPoint;
import com.github.arteam.data.Point;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

public class FalseSharingBenchmark {

    @State(Scope.Group)
    public static class FalseSharingState {
        private Point point = new Point(42, 0);
    }

    @State(Scope.Group)
    public static class ContentedState {
        private ContentedPoint point = new ContentedPoint(42, 0);
    }

    @Benchmark
    @Group("falseSharing")
    @GroupThreads(1)
    public long read(FalseSharingState state) {
        return state.point.getX();
    }

    @Benchmark
    @Group("falseSharing")
    @GroupThreads(1)
    public void write(FalseSharingState state) {
        state.point.setY(state.point.getY() + 1);
    }

    @Benchmark
    @Group("contended")
    @GroupThreads(1)
    public long read(ContentedState state) {
        return state.point.getX();
    }

    @Benchmark
    @Group("contended")
    @GroupThreads(1)
    public void write(ContentedState state) {
        state.point.setY(state.point.getY() + 1);
    }

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                .include(FalseSharingBenchmark.class.getSimpleName())
                .forks(1)
                .warmupIterations(10)
                .measurementIterations(10)
                .threads(2)
                .timeUnit(TimeUnit.MICROSECONDS)
                .build();
        new Runner(options).run();
    }
}
