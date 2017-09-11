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

import java.util.ArrayList;

public class Help
{
	
	/*
	* Help() void
	* 
	* Constructor for calling Process() function.
	*/
	public Help() { }
	
	/*
	* Process() void
	* 
	* Process the input.
	* 
	* String options - command options
	*/
	public static void Process(String options)
	{

		PrintHelp(JTerm.GetAsArray(options));

	}

	/*
	* PrintHelp() void
	* 
	* Prints help information about JTerm.
	* 
	* ArrayList<String> options - command options
	*/
	public static void PrintHelp(ArrayList<String> options)
	{
		
		System.out.println("JTerm v" + JTerm.version);
		
	}
	
}
