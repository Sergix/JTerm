package main.java.com.jterm;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Client implements Runnable {

	private static BufferedReader input;
	
	public void run() {
		
		while (true) {
			try {
				String output = Client.input.readLine();
				if (output != null)
					System.out.println(output);
				
			} catch (IOException ioe) {
				return;
				
			}
			
		}
		
	}
	
	public static void Connect(ArrayList<String> options) {
		
		String address = "0.0.0.0";
		String portInput = "80";
		boolean next = false;
		
		for (String option: options) {
			if (option.equals("-h"))
			{
				System.out.println("Command syntax:\n\tconnect [-h] [-p port] address\n\nConnect to the specified IP address using TCP/IP. Default address is \"0.0.0.0\". Default port is 80.");
				
			}
			else if (option.equals("-p"))
			{
				next = true;
				
			}
			else if (next)
			{
				portInput = option;
				next = false;
				
			}
			else
			{
				address = option;
				
			}
			
		}
		
		int i = 0;
        int port = 0;

        while( i < portInput.length())  
        {
            port *= 10;
            port += portInput.charAt(i++) - '0';
            
        }

		try {
			System.out.println("Connecting to " + address + ":" + port);
			
			Socket connection 					= new Socket(address, port);
			InputStream input 					= connection.getInputStream();
			OutputStream output 				= connection.getOutputStream();
			BufferedReader bufferedSocketInput 	= new BufferedReader(new InputStreamReader(input));
			BufferedReader bufferedSocketOutput = new BufferedReader(new InputStreamReader(System.in), 1);
			
			Client.input 						= bufferedSocketInput;
			
			Client client 						= new Client();
			Thread readThread 					= new Thread(client);
			readThread.start();
			
			System.out.println("Connected to server. Enter a blank line to quit. Reading for input...");
			
			while (true) {
				String line = bufferedSocketOutput.readLine();
				if (line.equals(""))
					break;
				output.write(line.getBytes());
				
			}
			
			connection.close();
			
		}
		catch (IOException ioe)
		{
			System.out.println("ERROR: " + ioe);
			
		}
		
	}
	
}
