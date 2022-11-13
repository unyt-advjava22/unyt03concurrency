package com.eltonb.concurrency.ex13;

// FibonacciDemo.java
// Fibonacci calculations performed synchronously and asynchronously
import java.sql.Time;
import java.time.Duration;
import java.text.NumberFormat;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import static java.util.stream.Collectors.*;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Stream;

class TimeData {
    public Instant start;
    public Instant end;

    // return total time in seconds
    public double timeInSeconds() {
        return Duration.between(start, end).toMillis() / 1000.0;
    }
    
    TimeData(Instant start, Instant end) {
        this.start = start;
        this.end = end;
    }
} 

public class FibonacciDemo {

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        // perform synchronous fibonacci(45) and fibonacci(44) calculations
        System.out.println("Synchronous Long Running Calculations");
        TimeData synchRes1 = startFibonacci(43);
        TimeData synchRes2 = startFibonacci(44);
        TimeData synchRes3 = startFibonacci(45);
        double synchTime = calculateTime(synchRes1, synchRes2, synchRes3);
        System.out.printf("  Total calculation time = %.3f seconds%n", synchTime);

        // perform asynchronous fibonacci(45) and fibonacci(44) calculations
        System.out.printf("%nAsynchronous Long Running Calculations%n");
        CompletableFuture<TimeData> futureRes1 = CompletableFuture.supplyAsync(() -> startFibonacci(43));
        CompletableFuture<TimeData> futureRes2 = CompletableFuture.supplyAsync(() -> startFibonacci(44));
        CompletableFuture<TimeData> futureRes3 = CompletableFuture.supplyAsync(() -> startFibonacci(45));

        // wait for results from the asynchronous operations
        try {
            TimeData asynchRes1 = futureRes1.get(2, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            TimeData asynchRes2 = futureRes2.get(2, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            TimeData asynchRes3 = futureRes3.get(2, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*
        double asynchTime = calculateTime(asynchRes1, asynchRes2, asynchRes3);
        System.out.printf("  Total calculation time = %.3f seconds%n", asynchTime);

        // display time difference as a percentage
        String percentage = NumberFormat.getPercentInstance().format(synchTime / asynchTime);
        System.out.printf("%nSynchronous calculations took %s more time than the asynchronous ones%n", percentage);
        */
    }

    private static TimeData startFibonacci(int n) {
        System.out.printf("  Calculating fibonacci(%d)%n", n);
        Instant start = Instant.now(); 
        long fibonacciValue = fibonacci(n);
        Instant end = Instant.now();
        TimeData timeData = new TimeData(start, end);
        displayResult(n, fibonacciValue, timeData);
        return timeData;
    } 

    // recursive method fibonacci; calculates nth Fibonacci number
    private static long fibonacci(long n) {
        if (n == 0 || n == 1)
            return n;
        else
            return fibonacci(n - 1) + fibonacci(n - 2);
    } 

    // display fibonacci calculation result and total calculation time
    private static void displayResult(int n, long value, TimeData timeData) {
        System.out.printf("  fibonacci(%d) = %d%n", n, value);
        System.out.printf("  Calculation time for fibonacci(%d) = %.3f seconds%n", n, timeData.timeInSeconds()); 
    }

    // display fibonacci calculation result and total calculation time
    private static double calculateTime(TimeData ... results) {
        List<Instant> starts = Stream.of(results).map(res -> res.start).collect(toList());
        List<Instant> ends = Stream.of(results).map(res -> res.end).collect(toList());
        TimeData allThreads = new TimeData(Collections.min(starts), Collections.max(ends));        
        return allThreads.timeInSeconds();
    }
} // end class FibonacciDemo

/**************************************************************************
 * (C) Copyright 1992-2015 by Deitel & Associates, Inc. and               *
 * Pearson Education, Inc. All Rights Reserved.                           *
 *                                                                        *
 * DISCLAIMER: The authors and publisher of this book have used their     *
 * best efforts in preparing the book. These efforts include the          *
 * development, research, and testing of the theories and programs        *
 * to determine their effectiveness. The authors and publisher make       *
 * no warranty of any kind, expressed or implied, with regard to these    *
 * programs or to the documentation contained in these books. The authors *
 * and publisher shall not be liable in any event for incidental or       *
 * consequential damages in connection with, or arising out of, the       *
 * furnishing, performance, or use of these programs.                     *
 *************************************************************************/

