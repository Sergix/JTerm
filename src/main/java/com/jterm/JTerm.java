// package = folder :P
package main.java.com.jterm;

import java.util.Scanner;
import java.io.*;
import main.java.com.jterm.Write;
import java.util.ArrayList;

public class JTerm {
	
	  // Global version variable
	  static String version = "0.1.0";
	  
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
	  public static void main(String[] args) {
		  
		 // Assign a default value of false to the quit variable
		 boolean quit = false;
		 
		 BufferedReader user_input = new BufferedReader(new InputStreamReader(System.in), 1); // Setup input: String input = user_input.next();
		 
		 // Infinite loop for getting input
		 do {
			 // Set return value of the input function to "quit"
			 quit = JTerm.Standby(user_input);
			 
		 } while (!quit); 
		 // As long as we are not quitting...
		 
	  }
	  
	  /*
	  * Standby() boolean
	  * 
	  * Awaits user command and then runs the command
	  * specified.
	  *
	  * BufferedReader user_unput - Input stream loaded from the
	  * 							main() function
	  */
	  public static boolean Standby(BufferedReader user_input) {
		  	  
		  System.out.println("jterm> ");
		  String command = "";
		  
		  try {
			  command = user_input.readLine();
			  
		  }
		  catch (IOException ioe)
		  {
			  System.out.println(ioe);
			  
		  }
		  
		  // Get each substring of the command entered
		  Scanner tokenizer = new Scanner(command);
		  
		  // Get the next substring
		  String input = tokenizer.next();
		  
		  // options String array will be passed to command functions
		  ArrayList<String> options = new ArrayList<String>();
		  
		  // Get command arguments
		  while (tokenizer.hasNext()) {
			  String next = tokenizer.next();
			  options.add(next);
			  
		  }
		  
		  // Switch through command names
		  switch (input) {
		  	case "help":
			  // Prints "JTerm v1.0" for example
			  System.out.println("JTerm v" + version);
			  break;
			  
		  	case "quit":
		  	  // Quit the program
			  tokenizer.close();
			  return true;
			  
		  	case "write":
			  // Get the last option, which is the filename, and send it the option list
			  Write.WriteFile(options);
			  break;
			  
		  	case "dir":
			  Dir.PrintDir(options);
			  break;
			  
		  	default:
			  // Fall back when unknown command is entered
			  System.out.println("Unknown Command.");
			  break;
			  
		  }
		  
		  tokenizer.close();
		  
		  // Keep looping; we don't want to quit
		  return false;
		  
	  }
	  
	  
}