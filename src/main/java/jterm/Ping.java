package main.java.jterm;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;

/*
* Original code credit to @chromechris
* 
* (edits for release done by @Sergix)
*/
public class Ping
{

	/*
	* Ping() void
	* 
	* Pings the specified host.
	*
	* ArrayList<String> options - command options

	* -h
	* 	Prints help information
	* host
	* 	Host to ping
	* -p port
	*	Port to ping the host on
	*/
	public Ping() { }
	
	/*
	* Process() void
	* 
	* Process the input.
	* 
	* String options - command options
	*/
	public static void Process (String options)
	{

		ArrayList<String> optionsArray = JTerm.GetAsArray(options);
		PingHost(optionsArray);

	}

	/*
	* PingHost() void
	* 
	* Attempts to connect to the specified host
	* through the port provided.
	* 
	* ArrayList<String> options - command options
	*/
	public static void PingHost(ArrayList<String> options)
	{

  		String host = "google.com", port = "80";
		boolean portNext = false;
		
		for (String option: options)
		{
			if (option.equals("-h"))
			{
				System.out.println("Command syntax:\n\tping [-h] [-p port] host\n\nAttempts to connect to the specified host. Default port is '80'.");
				return;
				
			}
			else if (portNext)
			{
				port = option;
				portNext = false;
				
			}
			else if (option.equals("-p"))
				portNext = true;
			
			else
				host = option;
			
		}
		
	    try (Socket socket = new Socket())
	    {
	    	System.out.println("Pinging " + host + "...");
	        socket.connect(new InetSocketAddress(host, Integer.parseInt(port)), 10000);
	        System.out.println("Ping Successful");
	        
	    }
	    catch (IOException e)
	    {
	    	// Either timeout or unreachable or failed DNS lookup
	    	System.out.println("Ping Failed");
	        
	    }
	    
	}
	
}
