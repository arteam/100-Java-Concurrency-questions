package com.github.arteam;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class L2CacheSize {

    public static void main(String[] args) throws Exception {
        new L2CacheSize().getL2CacheSize();
    }

    public void getL2CacheSize() {
        int level = 2;
        File size = new File("/sys/devices/system/cpu/cpu0/cache/index" + level + "/size");
        try (BufferedReader reader = new BufferedReader(new FileReader(size))) {
            System.out.printf("L2 cache size: %s bytes", reader.readLine());
        } catch (IOException e) {
            throw new IllegalStateException("Unable find L2 cache info");
        }
    }
}
