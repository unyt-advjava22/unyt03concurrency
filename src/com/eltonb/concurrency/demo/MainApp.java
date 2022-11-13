package com.eltonb.concurrency.demo;

import java.text.ParseException;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class MainApp {

    private static final String dateValue = "2022-11-14 19:30:00";
    private static final int MAX_TRIES = 1000;

    public static void main(String[] args) {
        MainApp app = new MainApp();
        boolean isOk = app.testOne();
        System.out.println("Single run returned " + (isOk ? "success" : "failure"));
        System.out.println();
        System.out.println();

        System.out.println("Doing " + MAX_TRIES + " sequential runs:");
        int okCount = 0, nokCount = 0;
        for(int i=0; i<MAX_TRIES; i++) {
            isOk = app.testOne();
            if (isOk)
                okCount++;
            else
                nokCount++;
        };
        System.out.printf(" okCount=%3d\nnokCount=%3d\n", okCount, nokCount);
        System.out.println();
        System.out.println();

        System.out.println("Doing " + MAX_TRIES + " concurrent runs:");
        AtomicInteger aokCount = new AtomicInteger(0);
        AtomicInteger anokCount = new AtomicInteger(0);
        for(int i=0; i<MAX_TRIES; i++) {
            Thread t = new Thread(() -> {
                boolean ok = app.testOne();
                if (ok)
                    aokCount.incrementAndGet();
                else
                    anokCount.incrementAndGet();
                //System.out.printf("thread %s finished \n", Thread.currentThread().getName());
            });  //app::testOne
            t.start();
        }

        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            //
        }

        System.out.printf(" okCount=%3s\nnokCount=%3s\n", aokCount, anokCount);
        System.out.println();
        System.out.println();
        System.out.println("main thread terminates: " + Thread.currentThread().getName());
    }

    private boolean testOne() {
        try {
            Date date = CommonUtils.toDate(dateValue);
            boolean ok =
                    date.getYear()      ==122 &&
                    date.getMonth()     == 10 &&
                    date.getDate()      == 14 &&
                    date.getHours()     == 19 &&
                    date.getMinutes()   == 30 &&
                    date.getSeconds()   ==  0 ;
            return ok;
        } catch (Exception e) {
            System.out.printf("Parsing %s failed: %s\n", dateValue, e.getMessage());
            return false;
        }
    }
}
