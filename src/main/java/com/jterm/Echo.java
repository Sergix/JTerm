package main.java.com.jterm;

import java.util.ArrayList;

public class Echo {
	
	
	public static void EchoInput(ArrayList<String> options) {
		
		String output = "";
		
		for (String option: options) {
			
			  if (option.equals("-h"))
			  {
				  System.out.println("");
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
