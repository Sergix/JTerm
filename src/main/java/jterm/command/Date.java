package main.java.jterm.command;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;

public class Date {
	
	private static final DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); // DateFormat sdf
    
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
		
		Calendar cal = Calendar.getInstance(); // Display the date
        System.out.println("The current date is: " + sdf.format(cal.getTime()));
		
	}
	
}
