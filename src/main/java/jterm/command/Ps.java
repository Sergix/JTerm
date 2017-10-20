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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.commons.lang3.SystemUtils;

/*
* Original code credit to @chromechris
* 
* (edits for release done by @Sergix)
*/
public class Ps 
{

	/*
	* Ps() void
	* 
	* Prints a list of process running on
	* the system.
	*
	* ArrayList<String> options - command options
	*
	* -h
	* 	Prints help information
	*/
	public Ps(ArrayList<String> options)
	{

		for (String option: options)
		{
			if (option.equals("-h"))
			{
				System.out.println("Command syntax:\n\tps [-h]\n\nDisplays all current processes running on the host system.");
				return;
				
			}
			
		}

		if (SystemUtils.IS_OS_LINUX)
		{
			try
			{
				String line;
				Process p = Runtime.getRuntime().exec("ps -e");
				BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
				while ((line = input.readLine()) != null)
				{
					// Parse data here.
					System.out.println(line);
					
				}
				input.close();
				
			}
			catch (Exception err)
			{
				err.printStackTrace();
				
			}

		}
		else if (SystemUtils.IS_OS_WINDOWS)
		{
			try
			{
				String line;
				Process p = Runtime.getRuntime().exec(System.getenv("windir") + "\\system32\\" + "tasklist.exe");
				BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
				while ((line = input.readLine()) != null)
				{
					// Parse data here.
					System.out.println(line);
					
				}
				
				input.close();
				
			}
			catch (Exception err)
			{
				err.printStackTrace();
				
			}
			
		}

	}

}