package main.java.com.jterm; // package = folder :P

import java.util.Scanner;
import java.io.*;
import main.java.com.jterm.Write;
import java.util.ArrayList;

public class JTerm { // Main method, call when going back to standby
	
	  static String version = "Prerelease1.0"; // Global version variable
	
	  public static void main(String[] args) {
		  
		 boolean quit = false; // Assign a default value of false to the quit variable
		 BufferedReader user_input = new BufferedReader(new InputStreamReader(System.in), 1); // Setup input: String input = user_input.next();
		 
		 do { // Infinite loop for getting input
			 
			 quit = JTerm.Standby(user_input); // Set return value of the input function to "quit"
			 
		 } while (!quit); // As long as we are not quitting...
		 
	}
	  
	  
	  public static boolean Standby(BufferedReader user_input) { // Standby mode, awaiting user input.
		  	  
		  System.out.println("jterm> ");
		  String command = "";
		  
		  try {
			  command = user_input.readLine();
		  }
		  catch (IOException ioe)
		  {
			  System.out.println(ioe);
		  }
		  
		  Scanner tokenizer = new Scanner(command); // Get each substring of the command entered
		  String input = tokenizer.next(); // Get the next substring
		  
		  ArrayList<String> options = new ArrayList<String>();
		  
		  
		  // Get command arguments
		  
		  while(tokenizer.hasNext()) {
			  String next = tokenizer.next();
			  options.add(next);
		  }
		  
		  // Switch through command names
		  switch (input) {
		  
		  case "help":
			  System.out.println("JTerm v" + version); // Prints "JTerm v1.0" for example
			  break;
			  
		  case "quit":
			  tokenizer.close();
			  return true; // Quit the program
			  
		  case "write":
			  Write.WriteFile(options); // Get the last option, which is the filename, and send it the option list
			  break;
			  
		  case "dir":
			  Dir.PrintDir(options);
			  break;
			  
		  default:
			  System.out.println("Unknown Command."); // Fall back when unknown command is entered
			  break;
			  
		  }
		  
		  tokenizer.close();
		  
		  return false; // Keep looping; we don't want to quit
		  
	  }
	  
	  
}