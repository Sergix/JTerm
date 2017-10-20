/*
* JTerm - a cross-platform terminal
* Copyright (code) 2017 Sergix, NCSGeek
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
				System.out.println("Command syntax:\n\tdate [-h]\n\nPrints the system date."); // Options
				return;
			  
			}
		 
		}
		
		Calendar cal = Calendar.getInstance(); // Display the date
        System.out.println("The current date is: " + sdf.format(cal.getTime()));
		
	}
	
}
