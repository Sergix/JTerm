package main.java.com.jterm;

import java.util.ArrayList;

public class Echo {
	
	/*
	* EchoInput() void
	* 
	* Echoes the input recieved to the console.
	* 
	* ArrayList<String> options - command options
	*
	* -h
	*     Prints help information.
	*/
	public static void EchoInput(ArrayList<String> options) {
		
		String output = "";
		
		for (String option: options) {
			if (option.equals("-h"))
			{
				System.out.println("Command syntax:\n\techo [-h] input\n\nPrints the specified input to the console.");
				return;
			  
			}
			else
			{
				output += option + " ";
			  
			}
		 
		}
		
		System.out.println(output);
		
	}
	
}
