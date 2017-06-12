package main.java.jterm;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server implements Runnable {

	private Socket socket;
	public static boolean run = true;
	public static int port = 0;
	private static String line;
	
	Server(Socket newSocket) {
		socket = newSocket;
		
	}
	
	public void run() {
		
		while (run) {
			try {
				InputStream input = socket.getInputStream();
				BufferedReader bufferedSocketInput 	= new BufferedReader(new InputStreamReader(input));
				
				line = bufferedSocketInput.readLine();
				if (line.isEmpty()) {
					break;
					
				}
				System.out.println("\n" + line);
				
				bufferedSocketInput.close();
				
			}
			catch (IOException ioe)
			{
				System.out.println(ioe);
				break;
				
			}
			
		}
		
		return;
		
	}
	
	public static void Start(ArrayList<String> options) {
		
		String portInput = "80";
		
		for (String option: options)
		{
			if(option.equals("-h"))
			{
				System.out.println("Command syntax:\n\tserver [-h] port\n\nStarts a TCP server socket that accepts ");
				return;
				
			}
			else
			{
				portInput = option;
				
			}
		}
		
		int i = 0;

        while( i < portInput.length())  
        {
            port *= 10;
            port += portInput.charAt(i++) - '0';
            
        }
		
		try {
			ServerSocket server = new ServerSocket(port);
			
			new Thread(new Runnable() {
				
				public void run() {
					
					while (true)
					{
						System.out.print("> ");
						BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in), 1);
						try {
							String input = consoleInput.readLine();
							switch (input) {
								case "help":
									System.out.println("Server currently opened on port " + port);
									break;
								
								case "quit":
									run = false;
									return;
							
							}
							
						} catch (IOException ioe) {
							System.out.println("Input Stream closed.");
							break;
							
						}
						
					}
					
				}
				
			}).start();
			
			while (run) {
				Socket socket = server.accept();
				Server newRequest = new Server(socket);
				new Thread(newRequest).start();
				
			}
			
			server.close();
			
		}
		catch (IOException ioe)
		{
			System.out.println("ERROR: Server closed");
			
		}
		
	}
	
}
