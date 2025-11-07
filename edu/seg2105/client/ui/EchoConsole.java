package edu.seg2105.client.ui;

import edu.seg2105.client.common.ChatIF;
import edu.seg2105.edu.server.backend.EchoServer;

import java.util.Scanner;



public class EchoConsole implements ChatIF {
	
	final public static int DEFAULT_PORT = 5555;
	
	EchoServer server;
	
	 Scanner fromConsole; 

	public EchoConsole(int port) {
		// TODO Auto-generated constructor stub
	
	    try 
	    {
	      server= new EchoServer(port, this);
	      
	      
	    } 
	    catch(Exception exception) 
	    {
	      System.out.println("Error: Can't setup connection!"
	                + " Terminating server.");
	      System.exit(1);
	    }
	    
	    try 
	    {
	      server.listen(); //Start listening for connections
	    } 
	    catch (Exception ex) 
	    {
	      System.out.println("ERROR - Could not listen for clients!");
	    }
	    
	    // Create scanner object to read from console
	    fromConsole = new Scanner(System.in); 
		
	}

	  public void accept() 
	  {
	    try
	    {

	      String message;

	      while (true) 
	      {
	        message = fromConsole.nextLine();
	        display(message);
	        server.handleConsole(message);
	      }
	    } 
	    catch (Exception ex) 
	    {
	      System.out.println
	        ("Unexpected error while reading from console!");
	    }
	  }

	  /**
	   * This method overrides the method in the ChatIF interface.  It
	   * displays a message onto the screen.
	   *
	   * @param message The string to be displayed.
	   */
	  public void display(String message) 
	  
	  {
		  
		if (message.startsWith("#")) {
			
			System.out.println(message);
			
		} else {
			
			System.out.println(server.serverPrefix + message);
		}
	  }


	  public static void main(String[] args) 
	  {
	    int port = 0; //Port to listen on

	    try
	    {
	      port = Integer.parseInt(args[0]); //Get port from command line
	    }
	    catch(Throwable t)
	    {
	      port = DEFAULT_PORT; //Set port to 5555
	    }
	    
	    EchoConsole echo = new EchoConsole(port);
	    echo.accept();  //Wait for console data
	    

	  }
	  
		

}
