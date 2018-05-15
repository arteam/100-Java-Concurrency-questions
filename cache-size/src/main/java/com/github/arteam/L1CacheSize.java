package com.github.arteam;

import java.io.*;

public class L1CacheSize {

    public static void main(String[] args) throws Exception {
        new L1CacheSize().getL1CacheSize();
    }

    public void getL1CacheSize() {
        // Walk through data and instructions caches
        for (int i = 0; i <= 1; i++) {
            String cacheInfoBasePath = "/sys/devices/system/cpu/cpu0/cache/index" + i;
            if (isDataL1Cache(cacheInfoBasePath)) {
                System.out.printf("L1 cache size: %s bytes", getCacheSize(cacheInfoBasePath));
                return;
            }
        }

        throw new IllegalStateException("Unable find L1 cache info");
    }

    private boolean isDataL1Cache(String cacheInfoBasePath) {
        File type = new File(cacheInfoBasePath + "/type");
        File level = new File(cacheInfoBasePath + "/level");
        try (BufferedReader typeReader = new BufferedReader(new FileReader(type));
             BufferedReader levelReader = new BufferedReader(new FileReader(level))) {
            return typeReader.readLine().equalsIgnoreCase("Data") &&
                    levelReader.readLine().equals("1");
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private String getCacheSize(String cacheInfoBasePath) {
        File size = new File(cacheInfoBasePath + "/size");
        try (BufferedReader reader = new BufferedReader(new FileReader(size))) {
            return reader.readLine();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
