/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eltonb.concurrency.ex13;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author elton.ballhysa
 */
public class Callables {
    public static void main(String[] args) {
        Callables t = new Callables();
        try {
            t.runTest();
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            ex.printStackTrace();
        } 
    }

    private void runTest() throws InterruptedException, ExecutionException, TimeoutException {
        ExecutorService executor = Executors.newCachedThreadPool();
        Future<String> future = executor.submit(new MysteryCall());
        executor.shutdown();
        String result = future.get(5, TimeUnit.SECONDS);
        System.out.println("result is " + result + " ... end of " + Thread.currentThread().getName());
        
    }
    
}
