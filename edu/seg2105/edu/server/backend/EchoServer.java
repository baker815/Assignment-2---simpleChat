package edu.seg2105.edu.server.backend;
// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 


import java.io.IOException;

import edu.seg2105.client.common.ChatIF;

import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 */
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  final public String key = "LoginID";
  
  
  ChatIF serverUI; 
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
 * @param echoConsole 
   */
  public EchoServer(int port, ChatIF serverUI) 
  {
    super(port);
    this.serverUI = serverUI;
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient
    (Object msg, ConnectionToClient client)
  {
	String message = msg.toString();
	

	
	if (message.startsWith("#login")) {
		
		serverUI.display(message);
		
		int start, end;
		start = message.indexOf('<');
		end = message.indexOf('>');
		
		String slice = message.substring(start + 1, end);
		
		client.setInfo(key, slice);
		
		
		
	
		
	} else {
		
		System.out.println("Message received: " + msg + " from " + client + " " + client.getInfo(key));
		
		String message2 = (String) msg;
		
		message2 = client.getInfo(key).toString() + ":" + msg;
		
		
		
		this.sendToAllClients((Object) message2);
	}
	
  }
  
  public void handleConsole(String message) throws IOException 
  {
	  
	  	if (!message.startsWith("#")) {
	  		
	  		this.sendToAllClients("SERVER MSG> " + message);
	  	} else if (message.equals("#quit")) {
	  		serverUI.display("Quiting");
	  		System.exit(0);
	  	} else if(message.equals("#stop")) {
	  		serverUI.display("Server stopping");
	  		this.stopListening();
	  	} else if (message.equals("#close")) {
	  		serverUI.display("Server closing");
	  		this.close();
	  	} else if (message.startsWith("#setport")) {
	  		 
	  		if (this.isListening()) {
	  			serverUI.display("Server is not closed");
	  			return;
	  		}
	  		 int start, end;
	  		 start = message.indexOf('<');
	  		 end = message.indexOf('>');
	  		 String port = message.substring(start + 1, end);
	  		 int portNumber = Integer.parseInt(port);
	  		 this.setPort(portNumber);
	  		 serverUI.display("Server port set to " + port);
	  	} else if (message.equals("#start")) {
	  		if (this.isListening()) {
	  			serverUI.display("Server is not closed");
	  			return;
	  		}
	  		this.listen();
	  		serverUI.display("Sever is open");
	  	
	  	} else if (message.equals("#getport")) {
			serverUI.display("Sever port is " + this.getPort());	  		
	  	}
	  
	  
	  
	  
  }
    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }
  
  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */

  
  	@Override
	protected void clientConnected(ConnectionToClient client) {
		System.out.println("Client Connected");
	}
  	
  	@Override
	synchronized protected void clientDisconnected(ConnectionToClient client) {
		// Since we don't track which ID belongs to this client directly,
		// remove by value.
		System.out.println("Client Disconnected");
	}
  	
  	@Override
	synchronized protected void clientException(ConnectionToClient client, Throwable exception) {
		System.out.println("Client Disconnected");
	}
}
//End of EchoServer class
