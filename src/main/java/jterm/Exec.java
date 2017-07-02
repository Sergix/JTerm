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
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Scanner;

public class Exec
{
	
	private static Hashtable<String, String> vars = new Hashtable<>();
	
	// Var name, window object
	private static Hashtable<String, Window> windows = new Hashtable<>();
	
	public static void Run(ArrayList<String> options)
	{	
		
		String file = "";
	
		for (String option: options) {
			if (option.equals("-h"))
			{
				System.out.println("Command syntax:\n\texec [-h] file\n\nExecutes a batch script.");
				
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
				System.out.println("ERROR: File \"" + file + "\" either does not exist or is not a file.");
				return;
				
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
					Exec.Parse(directive);
					
				} while ((directive = reader.readLine()) != null);

				reader.close();
				
			}
			catch (IOException ioe)
			{
				System.out.println(ioe);
				
			}
			
<<<<<<< HEAD
		}
		catch (FileNotFoundException ioe)
		{
=======
			
		} catch (FileNotFoundException ioe) {
>>>>>>> master
			System.out.println(ioe);
			
		}
		
	}
	
	public static void Parse(String directive)
	{
		String command = "";
		Scanner tokenizer = new Scanner(directive);
		ArrayList<String> options = new ArrayList<String>();
		while (tokenizer.hasNext())
			options.add(tokenizer.next());
		
		if (options.size() != 0)
			command = options.get(0).toLowerCase();
		
		else
		{
			tokenizer.close();
			return;
			
		}
			
		switch (command)
		{
		case "set":
			if (options.size() == 1)
			{
				String element = "";
				for (Enumeration<String> e = vars.keys(); e.hasMoreElements();)
				       System.out.println((element = e.nextElement()) + "=" + vars.get(element));
				       
				break;
				
			}
			
			String key = options.get(1), value = "";
			if ( !options.get(2).equals("=") )
			{
				tokenizer.close();
				return;
				
			}
			
			if (options.get(3).equals("window"))
			{
				for (int i = 0; i < 4; i++)
					options.remove(0);
				
				Window newWindow = new Window(options);
				windows.put(key, newWindow);
				break;
				
			}
			
			for (int i = 3; i < options.size(); i++)
			{
				value += options.get(i) + " ";
				
			}

			vars.put(key, value);
			break;
			
		case "pause":
			if (options.size() == 1)
				System.out.println("Press any key to continue...");
			else
			{
				String message = "";
				for (int i = 1; i < options.size(); i++)
				{
					if (i != options.size() - 1)
						message = options.get(i) + " ";
					else
						message = options.get(i);
				}
				
				System.out.print(message);
			}
			
			try {
				System.in.read();
				
			} catch (IOException ioe) {
				System.out.println(ioe);
				
			}
			
			tokenizer.close();
			return;
			
		// These commands are system-level/OS-dependent and are not possible to integrate into JTerm.
		// Just skip over them.
		case "bcdedit":
		case "chkdsk":
		case "chkntfs":
		case "cls":
		case "cmd":
		case "color":
		case "convert":
		case "diskpart":
		case "driverquery":
		case "format":
		case "fsutil":
		case "gpresult":
		case "mode":
		case "sc":
		case "shutdown":
		case "start":
		case "tasklist":
		case "taskkill":
		case "ver":
		case "vol":
		case "wmic":
			break;
			
		default:
			for (int i = 0; i < vars.size(); i++)
			{
				if ( vars.containsKey(options.get(0)) )
				{
					tokenizer.close();
					return;
					
				}
				if ( windows.containsKey(options.get(0)) )
				{
					switch(options.get(1))
					{
					case "visible":
						windows.get(options.get(0)).ToggleVisible();
						break;
						
					case "title":
						String newTitle = "";
						for (int j = 2; j < options.size(); j++)
							newTitle = options.get(j) + " ";

						windows.get(options.get(0)).GetFrame().setTitle(newTitle);
						break;
						
					default:
						break;
						
					}
					
					tokenizer.close();
					return;
					
				}
				
			}

			JTerm.Parse(options);
			break;
		
		}
		
		tokenizer.close();
		
	}
	
}
