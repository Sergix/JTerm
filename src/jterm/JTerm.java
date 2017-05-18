package jterm; // package = folder :P

import java.util.Scanner;

public class JTerm { // Main method, call when going back to standby
	
	  static String version = "Prerelease1.0"; // Global version variable
	
	  public static void main(String[] args) {
		  
		 boolean quit = false; // Assign a default value of false to the quit variable
		 Scanner user_input = new Scanner(System.in); // Setup input: String input = user_input.next();
		 
		 do { // Infinite loop for getting input
			 
			 quit = JTerm.Standby(user_input); // Set return value of the input function to "quit"
			 
		 } while (!quit); // As long as we are not quitting...
		 
		 user_input.close();
		 
	}
	  
	  
	  public static boolean Standby(Scanner user_input) { // Standby mode, awaiting user input.
		  	  
		  System.out.println("jterm> ");
		  String input = user_input.next(); // Get a line of text
		  
		  switch (input) {
		  
		  case "help":
			  System.out.println("JTerm v" + version); // Prints "JTerm v1.0" for example
			  break;
			  
		  case "quit":
			  return true; // Quit the program
			  
		  case "write":
			  Write.WriteFile();
			  break;
			  
		  default:
			  System.out.println("Unknown Command."); // Fall back when unknown command is entered
			  break;
			  
		  }
		  
		  return false; // Keep looping; we don't want to quit
		  
	  }
	  
	  
}