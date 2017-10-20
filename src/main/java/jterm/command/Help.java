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

import java.util.ArrayList;
import main.java.jterm.JTerm;

public class Help {

    /*
	* Help() void
	* 
	* Prints help information.
     */
    public Help(ArrayList<String> options) {

        for (String option: options)
		{
			if (option.equals("-h"))
			{
				System.out.println("Command syntax:\n\thelp [-h]\n\nPrints help information.");
				return;

            }
            
        }

        System.out.println("JTerm v" + JTerm.version);
        System.out.println("Available commands:");
        System.out.println("  client");
        System.out.println("  dir");
        System.out.println("  echo");
        System.out.println("  exec");
        System.out.println("  exit");
        System.out.println("  files");
        System.out.println("  pause");
        System.out.println("  ping");
        System.out.println("  ps");
        System.out.println("  server");
        System.out.println("  set");
        System.out.println("  window");

    }

}
