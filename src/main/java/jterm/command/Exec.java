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

package main.java.jterm.command;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

import com.sun.jna.platform.win32.WinBase;
import main.java.jterm.JTerm;

public class Exec
{
	
	/*
	* Exec() void
	* 
	* Constructor for calling Run() function.
	*/
	public Exec(ArrayList<String> options)
	{

		// Default to Run()
		Run(options);
	}
	

	public static boolean Run(ArrayList<String> options) {
		try {
			for (String s : options) {
				if (s.endsWith("jar")) { // runs jar files, takes no options
					Process p = Runtime.getRuntime().exec("java -jar " + s);
					Scanner in = new Scanner(p.getInputStream());
					String nextLine;
					while (p.isAlive() && (nextLine = in.nextLine()) != null) // print program output
						System.out.println(nextLine);
				} else { // pass program name to system, it will open it (for executables)
					Process p = Runtime.getRuntime().exec(s);
					Scanner in = new Scanner(p.getInputStream());
					String nextLine;
					while (p.isAlive() && (nextLine = in.nextLine()) != null) // print program output
						System.out.println(nextLine);
				}
			}
			return true; // ran programs successfully
		} catch (IOException e) {
			e.printStackTrace();
			return false; // error occurred
		}
	}
	
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