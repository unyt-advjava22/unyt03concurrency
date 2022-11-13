/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eltonb.concurrency.ex09;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;

/**
 *
 * @author elton.ballhysa
 */
public class GoButtonActionListener implements ActionListener {
    
    private JLabel label;
    private String text;
    
    public GoButtonActionListener(JLabel label, String text) {
        this.label = label;
        this.text = text;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
       int n;
       try {
          n = Integer.parseInt(text);
       } 
       catch(NumberFormatException ex) {
          label.setText("Enter an integer.");
          return;
       } 
       label.setText("Calculating...");
       BackgroundCalculator task = new BackgroundCalculator(n, label);         
       task.execute(); // execute the task
    } 
    
}
