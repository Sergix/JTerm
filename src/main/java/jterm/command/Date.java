package main.java.jterm.command;

import java.time.LocalDate;
import java.util.ArrayList;

public class Date {
	
    
	public Date(ArrayList<String> options)
	{
		
		for (String option: options)
		{
			if (option.equals("-h"))
			{
				System.out.println("Command syntax:\n\techo [-h] input\n\nPrints the specified input to the console."); // Options
				return;
			  
			}
		 
		}
		
		LocalDate today = LocalDate.now();
		System.out.println("The Current Date is " + today + " (yyyy-mm-dd)");
		
	}
	
}
