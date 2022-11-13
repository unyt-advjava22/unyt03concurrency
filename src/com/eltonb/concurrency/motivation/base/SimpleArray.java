package com.eltonb.concurrency.motivation.base;

public interface SimpleArray {
    void add(int value);

    default void sleepSilently(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
