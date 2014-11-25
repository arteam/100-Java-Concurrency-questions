package com.github.arteam.benchmark;

import com.github.arteam.data.Point;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

/**
 * Date: 11/21/14
 * Time: 11:47 PM
 *
 * @author Artem Prigoda
 */
public class FalseSharingBenchmark {

    @State(Scope.Benchmark)
    public static class BenchmarkState {
        private Point point = new Point(42, 0);
    }

    @Benchmark
    @Group
    @GroupThreads(1)
    public long read(BenchmarkState state) {
        return state.point.getX();
    }

    @Benchmark
    @Group
    @GroupThreads(1)
    public void write(BenchmarkState state) {
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
