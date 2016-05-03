import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
/*
File name: 	Client.java
Author: 	Amaury Diaz Diaz, 040-738-985
Course: 	CST8221 – JAP, Lab Section: 302
Assignment: 2.2
Date: 		December 10th 2015
Professor:  Svillen Ranev
Purpose: 	Runs the Client GUI 
*/

/**
 * Runs the client GUI
 * 
 * @author Amaury Diaz Diaz
 * @version 1.0
 * @see java.awt.Dimension
 * @see java.awt.EventQueue
 * @see javax.swing.JFrame
 * @since 1.8.0_20
 *
 */

public class Client {
	/**
	 * Runs the main method that executes the client GUI
	 * @param args
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				JFrame frame = new ClientView();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setMinimumSize(new Dimension(600, 550));
				frame.setTitle("Amaury Diaz Diaz's Client");
		        frame.setVisible( true );
			}
		});
	}
}
