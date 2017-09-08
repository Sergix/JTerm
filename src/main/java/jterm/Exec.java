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

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Exec
{
	
	/*
	* Run() boolean
	* 
	* Runs the executable file.
	* 
	* ArrayList<String> options - command options
	*/
	public static boolean Run(ArrayList<String> options)
	{	
		
		String file = "";
	
		for (String option: options) {
			if (option.equals("-h"))
			{
				System.out.println("Command syntax:\n\texec [-h] file\n\nExecutes a batch script.");
				return false;
				
			}
			else
				file = option;
			
		}
		
		File script = new File(file);
		if (!script.exists() || !script.isFile())
		{
			file = JTerm.currentDirectory + file;
			script = new File(file);
			if (!script.exists() || !script.isFile())
			{
				System.out.println("ERROR: File \"" + file + "\" either does not exist or is not an executable file.");
				return true;
				
			}
			
		}
		
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(script));
			try
			{
				String directive = reader.readLine();
				if (directive != null)
				do
				{
					// Store the command as an ArrayList
					Scanner tokenizer = new Scanner(directive);
					ArrayList<String> commandOptions = new ArrayList<String>();
					while (tokenizer.hasNext())
						options.add(tokenizer.next());

					tokenizer.close();

					// Where the magic happens!
					JTerm.Parse(commandOptions);
					
				} while ((directive = reader.readLine()) != null);

				reader.close();
				
			}
			catch (IOException ioe)
			{
				System.out.println(ioe);
				
			}
			
		}
		catch (FileNotFoundException ioe)
		{
			System.out.println(ioe);
			
		}
		
		return false;
		
	}

	// public static void Parse(String directive)
	// {
		
	// 	switch (command)
	// 	{	
	// 	case "pause":

	// 		return;
			
	// 	default:
	// 		// for (;;)
	// 		// if ( vars.containsKey(options.get(0)) )
	// 		// {
	// 		// 	int value;
	// 		// 	//
	// 		// 	// TODO
	// 		// 	// Create arithmetic operations that passes the value 
	// 		// 	// to whatever it is needed for
	// 		// 	//
	// 		// 	if ( !options.get(1).equals("=") || !vars.containsKey(options.get(2)) || !vars.containsKey(options.get(4)) )
	// 		// 		break;
				
	// 		// 	switch(options.get(3))
	// 		// 	{
	// 		// 	case "+":
	// 		// 		value = Integer.parseInt( vars.get(options.get(2)) ) + Integer.parseInt( vars.get(options.get(4)) );
	// 		// 		break;
					
	// 		// 	case "-":
	// 		// 		value = Integer.parseInt( vars.get(options.get(2)) ) - Integer.parseInt( vars.get(options.get(4)) );
	// 		// 		break;
					
	// 		// 	case "*":
	// 		// 		value = Integer.parseInt( vars.get(options.get(2)) ) * Integer.parseInt( vars.get(options.get(4)) );
	// 		// 		break;
					
	// 		// 	case "/":
	// 		// 		value = Integer.parseInt( vars.get(options.get(2)) ) / Integer.parseInt( vars.get(options.get(4)) );
	// 		// 		break;
					
	// 		// 	default:
	// 		// 		return;
					
	// 		// 	}
				
	// 		// 	//vars.replace(options.get(0), String.valueOf(value));

	// 		// 	return;
				
	// 		// }
	// 		// else if ( windows.containsKey(options.get(0)) )
	// 		// {
	// 		// 	switch(options.get(1))
	// 		// 	{
	// 		// 	case "visible":
	// 		// 		windows.get(options.get(0)).ToggleVisible();
	// 		// 		break;
					
	// 		// 	case "title":
	// 		// 		windows.get(options.get(0)).GetFrame().setTitle(GetRest(options, 2));
	// 		// 		break;
					
	// 		// 	default:
	// 		// 		break;
					
	// 		// 	}
				
	// 		// }
	// 		// else

	// 		return;
			
	// 	}
		
	// }
	
	public static String GetRest(ArrayList<String> options, int index)
	{
		
		String output = "";
		for (int i = index; i < options.size(); i++)
		{
			if (i != options.size() - 1)
				output += options.get(i) + " ";
			
			else
				output += options.get(i);
			
		}
		
		return output;
		
	}
	
}