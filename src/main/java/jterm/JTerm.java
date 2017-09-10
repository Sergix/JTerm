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

// package = folder :P
package main.java.jterm;

import java.util.Scanner;
import java.io.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class JTerm
{
	
	// Global version variable
	static String version = "0.4.1";
	
	// Global directory variable (use "cd" command to change)
	// Default value "./" is equal to the default directory set when the program starts
	static String currentDirectory = "./";
	
	// User input variable used among all parts of the application
	static BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
	
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
	public static void main(String[] args)
	{  
		
		// Assign a default value of false to the quit variable
		boolean quit = false;
		
		// Print licensing information
		System.out.println(
			"JTerm Copyright (C) 2017 Sergix, NCSGeek, chromechris\n" +
			"This program comes with ABSOLUTELY NO WARRANTY.\n" +
			"This is free software, and you are welcome to redistribute it\n" +
			"under certain conditions.\n"
		);
		
		// Infinite loop for getting input
		do
		{
			// Set return value of the input function to "quit"
			quit = JTerm.Standby();
			
		// As long as we are not quitting...
		} while (!quit);
		
		// Close all open window instances
		Window.CloseAll();

	}
	
	/*
	* Standby() boolean
	* 
	* Awaits user command and then calls Parse() with the
	* input.
	*
	* BufferedReader user_unput - Input stream loaded from the
	* 							main() function
	*/
	public static boolean Standby()
	{

		// Print the current directory as the prompt (e.g. "./")
		System.out.print(JTerm.currentDirectory + " ");
		String command = "";
		
		// Attempt to read a line from the input
		try
		{
			command = userInput.readLine();
			
			// If the command is a blank line, loop to next
			if (command.equals(""))
			{
				return false;
				
			}
			
		}
		catch (IOException ioe)
		{
			System.out.println(ioe);
			
			// Quit because of error
			return true;
			
		}
		
		// Parse the command and quit if necessary
		if (Parse(command))
			return true;
		
		// Keep looping; we don't want to quit
		return false;
		
	}
	
	/*
	* Parse() boolean
	* 
	* Checks input and passes command options to the function
	* that runs the requested command.
	*
	* ArrayList<String> options - command options
	*/
	public static boolean Parse(String options)
	{

		ArrayList<String> optionsArray = GetAsArray(options);

		// Get the first string in the options array, which is the command,
		// and capitalize the first letter of the command
		String original = optionsArray.get(0).toLowerCase(), command = original;
		String classChar = command.substring(0, 1);
		classChar = classChar.toUpperCase();
		command = command.substring(1);
		command = "main.java.jterm." + classChar + command;

		// When we pass the options, we don't want the command name included
		optionsArray.remove(0);

		try
		{
			// Get the class of the command
			Class c = Class.forName(command);
			Object o = c.newInstance();
			
			// Invoke the "Process" method of the class to run
			Method m = o.getClass().getMethod("Process", String.class);
			m.invoke(options.getClass(), new Object[] {GetAsString(optionsArray)});
			
		}

		// Exceptions
		catch (ClassNotFoundException cnfe)
		{
			ArrayList<String> execFile = new ArrayList<String>();
			execFile.add(original);
			if ( Exec.Run(execFile) )
				System.out.println("Unknown Command \"" + original + "\"");
				
		}
		catch (InstantiationException ie)
		{
			System.out.println(ie);

		}
		catch (IllegalAccessException iae)
		{
			System.out.println(iae);

		}
		catch (NoSuchMethodException nsme)
		{
			System.out.println(nsme);

		}
		catch (InvocationTargetException ite)
		{
			System.out.println(ite);

		}

		return false;

		// 	// Commands to skip in batch files
		// 	case "bcdedit":
		// 	case "chkdsk":
		// 	case "chkntfs":
		// 	case "cls":
		// 	case "cmd":
		// 	case "color":
		// 	case "convert":
		// 	case "diskpart":
		// 	case "driverquery":
		// 	case "format":
		// 	case "fsutil":
		// 	case "gpresult":
		// 	case "mode":
		// 	case "sc":
		// 	case "shutdown":
		// 	case "start":
		// 	case "tasklist":
		// 	case "taskkill":
		// 	case "ver":
		// 	case "vol":
		// 	case "wmic":
		// 		break;
		
	}

	public static ArrayList<String> GetAsArray(String options)
	{
		
		// Get each substring of the command entered
		Scanner tokenizer = new Scanner(options);
		
		// options String array will be passed to command functions
		ArrayList<String> array = new ArrayList<String>();
		
		// Get command arguments
		while (tokenizer.hasNext())
		{
			String next = tokenizer.next();
			array.add(next);
			
		}
		
		// Close the string stream
		tokenizer.close();

		return array;

	}

	public static String GetAsString(ArrayList<String> options)
	{
		
		// Get each substring of the command entered
		String string = "";

		// Get command arguments
		for (String option: options)
			string += option + " ";

		string.trim();

		return string;

	}

}