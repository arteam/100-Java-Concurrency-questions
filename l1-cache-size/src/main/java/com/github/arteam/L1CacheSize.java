package com.github.arteam;

import java.io.*;

/**
 * Date: 11/26/14
 * Time: 1:03 AM
 *
 * @author Artem Prigoda
 */
public class L1CacheSize {

    public static void main(String[] args) throws Exception {
        new L1CacheSize().getL1CacheLineSize();
    }

    public void getL1CacheLineSize() {
        boolean found = false;
        for (int i = 0; i <= 1; i++) {
            String cacheInfoBasePath = "/sys/devices/system/cpu/cpu0/cache/index" + i;
            if (isDataL1Cache(cacheInfoBasePath)) {
                System.out.printf("L0 cache line size: %d bytes", getLineSize(cacheInfoBasePath));
                found = true;
                break;
            }
        }

        if (!found) {
            throw new IllegalStateException("Unable find L1 cache info");
        }
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

    private int getLineSize(String cacheInfoBasePath) {
        File lineSize = new File(cacheInfoBasePath + "/coherency_line_size");
        try (BufferedReader reader = new BufferedReader(new FileReader(lineSize))) {
            return Integer.parseInt(reader.readLine());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
