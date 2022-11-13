/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eltonb.concurrency.ex13;

import java.util.Date;
import java.util.concurrent.*;

/**
 *
 * @author elton.ballhysa
 */
public class Tester {
        
    public static void main(String[] args) {
        try {
            Tester t = new Tester();
            //t.testRunnables();
            t.testCallables();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void testRunnables() throws Exception { 
        System.out.println("current thread starts    : " + Thread.currentThread().getName());
        ExecutorService executor = Executors.newCachedThreadPool();
        Runnable run1 = () -> {
            sleepSilently(1000); System.out.println("run1: " + Thread.currentThread().getName());};
        Runnable run2 = () -> {
            sleepSilently(1500); System.out.println("run2: " + Thread.currentThread().getName());};
        executor.execute(run1);
        executor.execute(run2);
        executor.shutdown();
        System.out.println("current thread terminates: " + Thread.currentThread().getName());
    }
    
    private void sleepSilently(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ex) {
            // turn it off
        }
        
    }

    private void testCallables() throws Exception {
        System.out.println("current thread starts    : " + Thread.currentThread().getName());
        ExecutorService executor = Executors.newCachedThreadPool();

        Callable<String> callFirst = () -> {
            sleepSilently(2_000);
            System.out.println("call_1: " + Thread.currentThread().getName());
            return "first";
        };
        Callable<String> callSecond = () -> {
            sleepSilently(5_500);
            System.out.println("call_2: " + Thread.currentThread().getName());
            return "second";
        };

        System.out.println("time now: " + new Date());

        Future<String> future1 = executor.submit(callFirst);
        Future<String> future2 = executor.submit(callSecond);
        executor.shutdown();

        String res1 = future1.get(3, TimeUnit.SECONDS);
        System.out.println("res1=" + res1);

        String res2 = future2.get(3, TimeUnit.SECONDS);
        System.out.println("res2=" + res2);

        System.out.println("time now: " + new Date());
        System.out.println("current thread terminates: " + Thread.currentThread().getName());
    }
    
}
