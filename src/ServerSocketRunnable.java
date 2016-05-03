import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
/*
File name: 	ServerSocketRunnable.java
Author: 	Amaury Diaz Diaz, 040-738-985
Course: 	CST8221 – JAP, Lab Section: 302
Assignment: 2.2
Date: 		December 10th 2015
Professor:  Svillen Ranev
Purpose: 	Manages a connection from a client to the server. 
*/

/**
 * Manages a connection from a client to the server.
 * 
 * @author Amaury Diaz Diaz
 * @version 1.0
 * @see java.io.IOException
 * @see java.io.ObjectInputStream
 * @see java.io.ObjectOutputStream
 * @see java.net.Socket
 * @see java.net.SocketException
 * @see java.text.SimpleDateFormat
 * @see java.util.Date
 * @since 1.8.0_20
 *
 */
public class ServerSocketRunnable implements Runnable {
	/**
	 * Current time.
	 */
	private SimpleDateFormat time;
	/**
	 * Current date.
	 */
	private SimpleDateFormat date;
	/**
	 * Socket used by the client to connect to the server
	 */
	private Socket connection;
	/**
	 * output stream
	 */
	private ObjectOutputStream output; 
	/**
	 * input stream
	 */
	private ObjectInputStream input;
	/**
	 * Time the current Thread sleeps
	 */
	private final int SLEEP_TIME=100;
	/**
	 * Initial Constructor of ServerSocketRunnable
	 * 
	 * @param connection - Client socket address
	 */
	public ServerSocketRunnable(Socket connection){
		this.connection=connection;
		/*date with the specified format (E.g 10 March 2015)*/
		date=new SimpleDateFormat("dd MMMMM yyyy");
		/*time with the specified format(E.g 12:23:54 PM)*/
		time=new SimpleDateFormat("HH:mm:ss aaa");
	}
	/**
	 * Overridden method from the Runnable Interface
	 */
	@Override
	public void run() {
		/*
		 * Initialize the streams and perform the server connection
		 */
		try {
			
			getStreams();
			processConnection();
		}
		/*If the user closes the client without ending the connection first*/
		catch(SocketException e){
			System.out.println("Next time close the connection!!!!!!!");
		}
		/*In case anything went wrong with the connection*/
		catch (IOException | ClassNotFoundException | InterruptedException e) {
			System.out.println("OH OH Something went really wrong");
		}
		/*Close the streams and connection when finished*/
		finally{
			closeConnection();
		}
		
	}
	/**
	 * Initialize the streams to be used
	 * @throws IOException If something goes wrong with the streams
	 */
	private void getStreams() throws IOException{
		output=new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		
		input=new ObjectInputStream(connection.getInputStream());
	}
	/**
	 * Process the connection and manages the streams according to the commands received from
	 * the client.
	 * @throws IOException				Something went wrong during the connection
	 * @throws ClassNotFoundException 	The stream cannot be casted
	 * @throws InterruptedException		Something went wrong during the connection
	 * @throws SocketException			The client was closed without ending the connection first		
	 */
	private void processConnection() throws IOException, ClassNotFoundException, InterruptedException,SocketException{
		/*Text entered by the client*/
		String textEntered;
		/*Text Passed to the client*/
		String textDisplayed;
		/*Command passed by the client*/
		String command="default";
		/*Delimiter used to get the command input by the client */
		int delimiter;
		/*Get commands from the client until the connection is ended by it*/
		do{
			/*Get the text entered by the client from the input stream*/
			textEntered = (String) input.readObject();
			/*Get the second (-) to delimiter the command and the optional text*/
			delimiter=textEntered.indexOf("-", 1);
			
			/*If there is no delimiter the command is the whole input from client*/
			if(delimiter==-1){
				command=textEntered;
			}
			/*Otherwise the command is everything entered before the second dash*/
			else{
				command=textEntered.substring(0,delimiter);
			}
			switch(command){
			/*In this case set the textDisplayed the corresponding text and continue*/
			case "-end":
				textDisplayed="Connection closed.";
				break;
			/*Set the textDisplayed to the text after the delimiter*/
			case "-echo":
				textDisplayed="ECHO: "+textEntered.substring((delimiter+1));
				break;
			/*Clear the screen*/
			case "-cls":
				output.writeObject("cls");
				output.flush();
				continue;
			/*Get the current time*/
			case "-time":
				textDisplayed="TIME: "+time.format(new Date());
				break;
			/*Get the current date*/
			case "-date":
				textDisplayed="DATE: "+date.format(new Date());
				break;
			/*Show all the available commands*/
			case "-help":
				textDisplayed="Available Services:\nend\necho\ntime\ndate\nhelp\ncls\n";
				break;
			/*The command is invalid*/
			default:
				textDisplayed="ERROR: Unrecognized command";
				break;
			}
			/*Write into the output stream the formated textDisplayed*/
			output.writeObject("SERVER>"+textDisplayed);
			output.flush();
			/*Set the thread to sleep for 100 miliseconds*/
			Thread.sleep(SLEEP_TIME);
		}while(!command.equals("-end"));
		
	}
	/**
	 * Close the connection and streams
	 */
	private void closeConnection(){
		try{
			System.out.println( "Server Socket: Closing client connection..." );
			output.close();
			input.close();
			connection.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
}
