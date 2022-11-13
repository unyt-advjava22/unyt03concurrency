package com.eltonb.concurrency.ex09;

// Fig. 23.25: FibonacciNumbers.java
// Using SwingWorker to perform a long calculation with 
// results displayed in a GUI.
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.util.concurrent.ExecutionException;

public class FibonacciNumbers extends JFrame 
{
   // components for calculating the Fibonacci of a user-entered number
   private final JPanel workerJPanel =  
      new JPanel(new GridLayout(2, 2, 5, 5));
   private final JTextField numberJTextField = new JTextField();
   private final JButton goJButton = new JButton("Go");
   private final JLabel fibonacciJLabel = new JLabel();

   // components and variables for getting the next Fibonacci number
   private final JPanel eventThreadJPanel =  
      new JPanel(new GridLayout(2, 2, 5, 5));
   private long n1 = 0; // initialize with first Fibonacci number
   private long n2 = 1; // initialize with second Fibonacci number
   private int count = 1; // current Fibonacci number to display
   private final JLabel nJLabel = new JLabel("Fibonacci of 1: ");
   private final JLabel nFibonacciJLabel = 
      new JLabel(String.valueOf(n2));
   private final JButton nextNumberJButton = new JButton("Next Number");

   // constructor
   public FibonacciNumbers()
   {
      super("Fibonacci Numbers");
      setLayout(new GridLayout(2, 1, 10, 10));
      
      // add GUI components to the SwingWorker panel
      workerJPanel.setBorder(new TitledBorder(
         new LineBorder(Color.BLACK), "With SwingWorker"));
      workerJPanel.add(new JLabel("Get Fibonacci of:"));
      workerJPanel.add(numberJTextField);
      
      goJButton.addActionListener(this::computeFibonacciViaAsynch); // end call to addActionListener
      workerJPanel.add(goJButton);
      workerJPanel.add(fibonacciJLabel);
      
      // add GUI components to the event-dispatching thread panel
      eventThreadJPanel.setBorder(new TitledBorder(
         new LineBorder(Color.BLACK), "Without SwingWorker"));
      eventThreadJPanel.add(nJLabel);
      eventThreadJPanel.add(nFibonacciJLabel); 
      nextNumberJButton.addActionListener(this::nextFib);
      eventThreadJPanel.add(nextNumberJButton);

      add(workerJPanel);
      add(eventThreadJPanel);
      setSize(275, 200);
      setVisible(true);
   } // end constructor

   // recursive method fibonacci; calculates nth Fibonacci number
   public long fibonacci(long number)
   {
      if (number == 0 || number == 1)
         return number;
      else
         return fibonacci(number - 1) + fibonacci(number - 2);
   }

   private void computeFibonacciSynchronously(ActionEvent event) {
      int n;
      try {
         n = Integer.parseInt(numberJTextField.getText());
      } catch(NumberFormatException ex) {
         fibonacciJLabel.setText("Enter an integer.");
         return;
      }
      fibonacciJLabel.setText("Calculating...");
      long result = fibonacci(n);
      fibonacciJLabel.setText(String.valueOf(result));
   }

   private void computeFibonacciViaAsynch(ActionEvent event) {
      int n;
      try {
         n = Integer.parseInt(numberJTextField.getText());
      } catch(NumberFormatException ex) {
         fibonacciJLabel.setText("Enter an integer.");
         return;
      }
      fibonacciJLabel.setText("Calculating...");
      BackgroundCalculator task = new BackgroundCalculator(n, fibonacciJLabel);
      task.execute(); // execute the task
   }

   private void nextFib(ActionEvent event) {
      // calculate the Fibonacci number after n2
      long temp = n1 + n2;
      n1 = n2;
      n2 = temp;
      ++count;

      // display the next Fibonacci number
      nJLabel.setText("Fibonacci of " + count + ": ");
      nFibonacciJLabel.setText(String.valueOf(n2));
   }

   // main method begins program execution
   public static void main(String[] args)
   {
      FibonacciNumbers application = new FibonacciNumbers();
      application.setDefaultCloseOperation(EXIT_ON_CLOSE);
   } 
} // end class FibonacciNumbers

/*************************************************************************
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
  