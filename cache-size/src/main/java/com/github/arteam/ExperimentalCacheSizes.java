package com.github.arteam;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class ExperimentalCacheSizes {

    int[] data;

    @Param({"8", "16", "32", "64", "128"})
    int length;

    ThreadLocalRandom tlr = ThreadLocalRandom.current();

    @Setup(Level.Iteration)
    public void setup() {
        int size = length * 1024 / 4;
        data = new int[size];
        for (int i = 0; i < size; i++) {
            data[i] = tlr.nextInt();
        }
    }

    @Benchmark
    public void bench(Blackhole blackhole) {
        shuffle(data);
        //Arrays.sort(data);
        selectionSort(data);
        blackhole.consume(data);
    }

    private void shuffle(int[] data) {
        // Shuffle array
        for (int i = data.length; i >= 1; i--) {
            swap(data, i - 1, tlr.nextInt(i));
        }
    }

    private void insertionSort(int[] data) {
        for (int i = 0; i < data.length - 1; i++) {
            if (data[i] > data[i + 1]) {
                swap(data, i, i + 1);
                for (int j = i; j >= 1; j--) {
                    if (data[j - 1] > data[j]) {
                        swap(data, j - 1, j);
                    } else {
                        break;
                    }
                }
            }
        }
    }

    private void selectionSort(int[] data) {
        for (int i = 0; i < data.length; i++) {
            int minIdx = i;
            for (int j = i; j < data.length; j++) {
                if (data[j] < data[minIdx]) {
                    minIdx = j;
                }
            }
            swap(data, i, minIdx);
        }
    }

    private void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    public static void main(String[] args) throws Exception {
        Options opt = new OptionsBuilder()
                .include(ExperimentalCacheSizes.class.getSimpleName())
                .warmupIterations(5)
                .measurementIterations(5)
                .forks(1)
                .build();

        new Runner(opt).run();
        /*int[] data = {11, 5, 3, 7, 1};
        new ExperimentalCacheSizes().selectionSort(data);
        System.out.println(Arrays.toString(data));*/
    }
}
