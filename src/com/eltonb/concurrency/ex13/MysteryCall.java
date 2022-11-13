/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eltonb.concurrency.ex13;

import java.util.concurrent.Callable;

/**
 *
 * @author elton.ballhysa
 */
public class MysteryCall implements Callable<String> {

    @Override
    public String call() throws Exception {
        Thread.sleep(10000);
        System.out.println("inside mystery, running in thread " + Thread.currentThread().getName());
        return "1";
    }
    
}
