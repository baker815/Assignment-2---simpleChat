// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package edu.seg2105.client.backend;

import ocsf.client.*;

import java.io.*;

import edu.seg2105.client.common.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 
  public String loginID;

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String host, int port, ChatIF clientUI, String loginID) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    this.loginID = loginID;
    openConnection();
  }

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
    
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message)
  {
    try
    {
    	if(message.startsWith("#")) {
    		
    		handleCommand(message);
    		
    	} else {
 
    		sendToServer(message);
    	}
    }
    catch(IOException e)
    {
      clientUI.display
        ("Could not send message to server.  Terminating client.");
      quit();
    }
  }
  
  
  public void handleCommand(String message) {
	   
    if (message.equals("#quit")) {
    	clientUI.display("Shutting down");
    	quit();
    } else if (message.equals("#logoff")) {
    	clientUI.display("logging off");
        try {closeConnection(); } catch (IOException e) {}
    } else if (message.equals("#login")) {
        if (!isConnected()) {
            try {openConnection(); } catch (IOException e) {}
            clientUI.display("logging in.");
        } else {
            clientUI.display("Already logged in.");
        }
    } else if (message.startsWith("#sethost")) {
        if (!isConnected()) {
    		String content = "";
    		int beg = message.indexOf('<');
    		int end =  message.indexOf('>');
    		content = message.substring(beg + 1, end);
    		clientUI.display("Setting host to " + content);
    		setHost(content);
        } else  {
        	clientUI.display("Unable to set host, logged in");
        }
    } else if (message.startsWith("#setport")) {
        if (!isConnected()) {
    		String content = "";
    		int beg = message.indexOf('<');
    		int end =  message.indexOf('>');
    		content = message.substring(beg + 1, end);
    		clientUI.display("Setting port to " + content);
    		setPort(Integer.parseInt(content));
        } else  {
        	clientUI.display("Unable to set port, logged in");
        }
    } else if (message.equals("#gethost")) {
        clientUI.display(getHost());
    } else if (message.equals("#getport")) {
        clientUI.display(String.valueOf(getPort()));
    }
    
  }

  
	/**
	 * Handles a message sent from the server to this client. This MUST be
	 * implemented by subclasses, who should respond to messages.
	 * 
	 * @param msg
	 *            the message sent.
	 */

  
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {
    	
    }
    System.exit(0);
  }
  
	/**
	 * Implements Hook method called each time an exception is thrown by the client's
	 * thread that is waiting for messages from the server. The method may be
	 * overridden by subclasses.
	 * 
	 * @param exception
	 *            the exception raised.
	 */
  	@Override 
	protected void connectionException(Exception exception) {
  		clientUI.display("The server has shut down");
  		quit();
	}
  	
	/**
	 * Hook method called after the connection has been closed. The default
	 * implementation does nothing. The method may be overriden by subclasses to
	 * perform special processing such as cleaning up and terminating, or
	 * attempting to reconnect.
	 */
  	
  	@Override
	protected void connectionClosed() {
  		clientUI.display("Connection closed");
	}
  	
  	
	/**
	 * Hook method called after a connection has been established. The default
	 * implementation does nothing. It may be overridden by subclasses to do
	 * anything they wish.
	 */
  	@Override
	protected void connectionEstablished() {
  		
  	    String message = "#login <" + loginID + ">";
  	    try {
			sendToServer(message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  		
  		
	}

  	

  
}


//End of ChatClient class
