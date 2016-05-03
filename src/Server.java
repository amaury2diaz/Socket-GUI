import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/*
File name: 	Server.java
Author: 	Amaury Diaz Diaz, 040-738-985
Course: 	CST8221 – JAP, Lab Section: 302
Assignment: 2.2
Date: 		December 10th 2015
Professor:  Svillen Ranev
Purpose: 	Responsible for launching the server that is going to interact with
			the client application. It does run a multithreaded server.
*/

/**
 * This class is responsible for launching the server that 
 * is going to interact with the client application. It does 
 * run a multithreaded server.
 * 
 * @author Amaury Diaz Diaz
 * @version 1.0
 * @see java.io.IOException
 * @see java.net.ServerSocket
 * @see java.net.Socket
 * @see java.util.concurrent.ExecutorService
 * @see java.util.concurrent.Executors
 * @since 1.8.0_20
 *
 */
public class Server {
	
	/**
	 * To launch the Server application.
	 * 
	 * @param args String arguments
	 */
	public static void main(String[] args) {
		/*Default port number used by the server*/
		int portNumber = 65000;
		/*Server Socket used by the server*/
		ServerSocket server;
		/*Socket used by the client to connect to the server*/
		Socket connection;
		/*Instance of SocketServerRunnable use to accept many clients
		 * by using multithreading*/
		ServerSocketRunnable ssRunnable;
		/*Check if any argument was entered from command prompt*/
		if (args.length > 0) {
			/*Check that the command entered is an Integer*/
			try {
				/*Set the port number to the port entered in command prompt*/
				portNumber = Integer.valueOf(args[0]);
				System.out.println("Using port: " + portNumber);
			} catch (NumberFormatException e) {
				System.out.println(args[0] + " Is an invalid port.");
				System.out.println("Using default port: " + portNumber);
			}
		} else {
			System.out.println("Using default port: " + portNumber);
		}
		try {
			/*Initialize the server socket*/
			server = new ServerSocket(portNumber);
			/*Accept connections from clients to the server*/
			while(true){
				connection=server.accept();
				System.out.println("Connecting to a client "+connection);
				ssRunnable=new ServerSocketRunnable(connection);
				/*Create a Thread pool*/
				ExecutorService threadExecutor = Executors.newCachedThreadPool();
				/*Execute the thread*/
				threadExecutor.execute(ssRunnable);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
