/*
* JTerm - a cross-platform terminal
* Copyright (C) 2017 Sergix, NCSGeek
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

// package = folder :P
package main.java.jterm;

import java.util.Scanner;
import java.io.*;
import java.util.ArrayList;
import java.util.Hashtable;

public class JTerm
{
	
	  // Global version variable
	  static String version = "0.4.1";
	  
	  // Global directory variable (use "cd" command to change)
	  // Default value "./" is equal to the default directory set when the program starts
	  static String currentDirectory = "./";
	  
	  static String commandChars = "";
	  
	  static Hashtable<String, Exec> commands = new Hashtable<String, Exec>();
	  
	  static BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
	  
	  /*
	  * main() void
	  * 
	  * Function called when the program loads. Sets
	  * up basic input streams and runs the command
	  * loop.
	  * 
	  * String[] args - arguments passed from the 
	  * 				console
	  */
	  public static void main(String[] args)
	  {  
		  
		  // Assign a default value of false to the quit variable
		  boolean quit = false;
		 
		  // Print licensing information
		  System.out.println(
			  "JTerm Copyright (C) 2017 Sergix, NCSGeek, chromechris\n" +
		 	  "This program comes with ABSOLUTELY NO WARRANTY.\n" +
		 	  "This is free software, and you are welcome to redistribute it\n" +
		 	  "under certain conditions.\n"
		  );
		 
		  // Infinite loop for getting input
		  do
		  {
		      // Set return value of the input function to "quit"
			  quit = JTerm.Standby();
			  
		  // As long as we are not quitting...
		  } while (!quit);
		  
		  // Close all open window instances
		  Window.CloseAll();

	  }
	  
	  /*
	  * Standby() boolean
	  * 
	  * Awaits user command and then calls Parse() with the
	  * input.
	  *
	  * BufferedReader user_unput - Input stream loaded from the
	  * 							main() function
	  */
	  public static boolean Standby()
	  {

		  // Print the current directory as the prompt (e.g. "./")
		  System.out.print(JTerm.currentDirectory + " ");
		  String command = "";
		  
		  // Attempt to read a line from the input
		  try
		  {
			  command = userInput.readLine();
			  
			  // If the command is a blank line, loop to next
			  if (command.equals(""))
			  {
				  return false;
				  
			  }
			  
		  }
		  catch (IOException ioe)
		  {
			  System.out.println(ioe);
			  
			  // Quit because of error
			  return true;
			  
		  }
		  
		  // Reset the value of the input prompt char reader
		  commandChars = "";
		  
		  // Get each substring of the command entered
		  Scanner tokenizer = new Scanner(command);
		  
		  // options String array will be passed to command functions
		  ArrayList<String> options = new ArrayList<String>();
		  
		  // Get command arguments
		  while (tokenizer.hasNext())
		  {
			  String next = tokenizer.next();
			  options.add(next);
			  
		  }
		  
		  // Close the string stream
		  tokenizer.close();
		  
		  // Parse the command and quit if necessary
		  if (Parse(options))
			  return true;
		  
		  // Keep looping; we don't want to quit
		  return false;
		  
	  }
	  
	  /*
	  * Parse() boolean
	  * 
	  * Checks input and passes command options to the function
	  * that runs the requested command.
	  *
	  * ArrayList<String> options - command options
	  */
	  public static boolean Parse(ArrayList<String> options)
	  {
		  
		  // Get the first string in the options array, which is the command
		  String command = options.get(0).toLowerCase();
		  
		  // Get rid of the command for when we pass the rest of the command options
		  options.remove(0);
		  
		  // Switch through command names
		  switch (command)
		  {
		  	case "help":
		  		// Prints "JTerm v1.0" for example
		  		System.out.println("JTerm v" + version);
			  	break;
			  
		  	case "quit":
		  		// Quit the program
		  		return true;
			  
		  	case "write":
		  		Files.WriteFile(options);
		  		break;
			  
		  	case "dir":
		  		Dir.PrintDir(options);
		  		break;
			  
		  	case "chdir":
		  	case "cd":
		  		Dir.ChangeDir(options);
		  		break;
		  		
		  	case "pwd":
		  		Dir.PrintWorkingDir(options);
		  		break;
		  		
		  	case "echo":
		  		Echo.EchoInput(options);
		  		break;
		  		
		  	case "delete":
		  	case "del":
		  		Files.Delete(options);
		  		break;
		  		
		  	case "md":
		  		Dir.NewDir(options);
		  		break;
		  		
		  	case "read":
		  		Files.ReadFile(options);
		  		break;
		  		
		  	/*case "connect":
		  		Client.Connect(options);
		  		break;
		  		
		  	case "server":
		  		Server.Start(options);
		  		break;*/
		  		
		  	case "window":
		  		new Window(options);
		  		break;
			  
		  	case "exec":
		  		Exec.Run(options);
		  		break;
	
		  	case "ps":
		  		Ps.View(options);
				break;
		  		
		  	case "ping":
		  		Ping.PingHost(options);		  		
				break;
				  
			case "set":
				Set.NewVar(options);
				break;

			case "pause":
				Pause.EnterPause(options);
				break;

			// Commands to skip in batch files
			case "bcdedit":
			case "chkdsk":
			case "chkntfs":
			case "cls":
			case "cmd":
			case "color":
			case "convert":
			case "diskpart":
			case "driverquery":
			case "format":
			case "fsutil":
			case "gpresult":
			case "mode":
			case "sc":
			case "shutdown":
			case "start":
			case "tasklist":
			case "taskkill":
			case "ver":
			case "vol":
			case "wmic":
				break;
				  
		  	default:
		  		// Create a new array that contains the command and check if it is an executable
		  		ArrayList<String> execFile = new ArrayList<String>();
		  		execFile.add(command);
		  		if ( Exec.Run(execFile) )
		  			System.out.println("Unknown Command \"" + command + "\"");
		  		
		  		// All else fails
		  		break;
			  
		  }

		  // Keep looping
		  return false;
		  
	  }

}