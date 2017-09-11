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

package main.java.jterm;

import java.util.Hashtable;
import java.util.Enumeration;
import java.util.ArrayList;

public class Set
{

	// Global variable hashtable
	public static Hashtable<String, String> vars = new Hashtable<String, String>();

	/*
	* Set() void
	* 
	* Constructor for calling Process() function.
	*/
	public Set() { }
	
	/*
	* Process() void
	* 
	* Process the input.
	* 
	* String options - command options
	*/
	public static void Process(String options)
	{

		ArrayList<String> optionsArray = JTerm.GetAsArray(options);
		NewVar(optionsArray);

	}
	
	/*
	* NewVar() void
	* 
	* Creates a new variable that is stored in
	* the global vars hashtable.
	* 
	* ArrayList<String> options - command options
	*/
	public static void NewVar(ArrayList<String> options)
	{

		// Print the value of all current variables
		if (options.size() == 1)
		{
			PrintVars();
			return;

		}
		
		// Get the variable name
		String key = options.get(1);

		// The name can't include spaces
		if ( !options.get(2).equals("=") )
			return;
		
		// If the type is a window, create a new one
		if (options.get(3).equals("window"))
		{
			// Remove the 'set' word from the options list
			options.remove(0);
			
			// Pass the rest of the options to create a new Window
			Window newWindow = new Window(options);

			// Put the window ID into the vars hashtable
			// associated with its key
			vars.put(key, Integer.toString(newWindow.GetId()));

			// Add the window to the global list
			Window.windows.add(newWindow);

			return;
			
		}
		
		// Put the variable contents into the global hashtable
		vars.put(key, Exec.GetRest(options, 3));

	}

	/*
	* PrintVars() void
	* 
	* Prints the values of all variables
	* currently stored.
	*/
	public static void PrintVars()
	{

		String element = "";

		// For each key...
		for (Enumeration<String> e = vars.keys(); e.hasMoreElements();)
			// ...print in the format of "key=value"
			System.out.println((element = e.nextElement()) + "=" + vars.get(element));
		
	}

}