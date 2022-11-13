package com.eltonb.concurrency.ex14;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.security.SecureRandom;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

/**
 *
 * @author elton.ballhysa
 */
public class FuturesTutorial {
    public static void main(String[] args) {
        FuturesTutorial t = new FuturesTutorial();
        t.runTests();
    }
    
    private SecureRandom secureRandom;
    
    public FuturesTutorial() {
        secureRandom = new SecureRandom();
    }
    
    private void runTests() {
        //testCallable();
        //testAsync();
        //testAsyncWithCancellation();
        //executeAsynch();
        //combineAsynch();
        //combineAsynchWithConsumer();
        //combineAsynchWithRunner();
        //combineFutures();
        parallelFutures();
    }
    
    public void testCallable() {
        try {
            System.out.println("started testCallable");
            Callable<String> callable1 = () -> { 
                System.out.println("...started  callable 1 (hello)");
                Thread.sleep(5000); 
                System.out.println("...finished callable 1 (hello)");
                return "Hello"; 
            };
            Callable<String> callable2 = () -> { 
                System.out.println("...started  callable 2 (world)");
                Thread.sleep(500); 
                System.out.println("...finished callable 2 (world)");
                return " World"; 
            };
            ExecutorService executor = Executors.newFixedThreadPool(2);
            Future<String> future1 = executor.submit(callable1);
            Future<String> future2 = executor.submit(callable2);
            executor.shutdown();
            
            System.out.println("callables submitted");

            String hello = future1.get(10, TimeUnit.SECONDS);
            String world = future2.get(2, TimeUnit.SECONDS);
            System.out.println(hello + world);
            System.out.println("finished testCallable");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    public void testAsync() {
        //future.get() will block until the computation ends, or times out
        try {
            Future<String> asynchFuture = calculateAsync();
            System.out.println("asyncFuture returned " + asynchFuture.get(2, TimeUnit.SECONDS));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public Future<String> calculateAsync() {
        // starts the runnable in a new thread
        // returns the future immediately
        CompletableFuture<String> completableFuture = new CompletableFuture<>();
        Executors.newCachedThreadPool().submit(() -> {
            int timeout = secureRandom.nextInt(5000);
            System.out.println("will wait " + timeout + " seconds");
            sleepSilently(timeout);
            completableFuture.complete("Hello");
            System.out.println("asynch future completes");
        });
 
        return completableFuture;
    }
    
    public void testAsyncWithCancellation() {
        //future.get() will block until the computation ends, or times out
        try {
            Future<String> asynchFuture = calculateAsyncWithCancellation();
            System.out.println("calculateAsyncWithCancellation returned " + asynchFuture.get(2, TimeUnit.SECONDS));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Future<String> calculateAsyncWithCancellation() {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();
        Executors.newCachedThreadPool().submit(() -> {
            int timeout = 500;
            System.out.println("will wait " + timeout + " seconds");
            sleepSilently(timeout);
            completableFuture.cancel(false);
            System.out.println("asynch future completes");
        });
 
        return completableFuture;
    }
    
    public void executeAsynch() {
        try {
            CompletableFuture<String> futureSupplier = CompletableFuture.supplyAsync(() -> "Hello");
            System.out.println("futureSupplier.get = " + futureSupplier.get());

            CompletableFuture<Void> futureRunner = CompletableFuture.runAsync(() -> System.out.println("hello from asynch runnable"));
            System.out.println("futureRunner.get = " + futureRunner.get()); // not needed for futureRunner, nothing to get
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void combineAsynch() {
        try {
            CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> "Hello");
            CompletableFuture<String> helloWorld = hello.thenApply(s -> s + " World");
            System.out.println("helloWorld.get = " + helloWorld.get());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void combineAsynchWithConsumer() {
        try {
            CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> "Hello");
            CompletableFuture<Void> futureConsumer = hello.thenAccept(s -> System.out.println("Computation returned: " + s));
            futureConsumer.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void combineAsynchWithRunner() {
        try {
            CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> "Hello");
            CompletableFuture<Void> futureRunner = hello.thenRun(() -> System.out.println("Computation finished"));
            futureRunner.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void combineFutures() {
        try {
            CompletableFuture<String> combinedFuture =  
                CompletableFuture
                    .supplyAsync(() -> "Hello")
                    .thenCompose(s -> CompletableFuture.supplyAsync(() -> s + " World"))
                    .thenCompose(s -> CompletableFuture.supplyAsync(() -> s + " From Completable Futures"));
            System.out.println("combinedFuture.get = " + combinedFuture.get());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void parallelFutures() {
        try {
            CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> display("Brave"));
            CompletableFuture<Void> future2 = CompletableFuture.runAsync(() -> display("New"));
            CompletableFuture<Void> future3 = CompletableFuture.runAsync(() -> display("World"));
            CompletableFuture<Object> combinedFuture = CompletableFuture.anyOf(future1, future2, future3);
            //CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(future1, future2, future3);
            combinedFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void display(String message) {
        int timeout = secureRandom.nextInt(5000);
        sleepSilently(timeout);
        System.out.printf("%s (timeout %d)\n", message, timeout);
    }
    
    public static void sleepSilently(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }
}
