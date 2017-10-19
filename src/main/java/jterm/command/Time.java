package jterm.command;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Time {
	
    
	public Time(ArrayList<String> options)
	{
		
		for (String option: options)
		{
			if (option.equals("-h"))
			{
				System.out.println("Command syntax:\n\techo [-h] input\n\nPrints the specified input to the console."); // Options
				return;
			  
			}
		 
		}
		
		DateFormat df = new SimpleDateFormat("HH:mm:ss , z");
	    Date dateobj = new Date();
	    System.out.println("The Current Time is: " + df.format(dateobj));
		
	}
	
}