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

package jterm.command;

import javax.swing.text.html.parser.Entity;
import java.util.*;

public class Set {
    // Global variable HashMap
    public static HashMap<String, String> vars = new HashMap<>();

    /*
    * Set() void
    *
    * Creates a new variable or prints all
    * stored variables.
    *
    * ArrayList<String> options - command options
    */
    public Set(ArrayList<String> options) {
        if (options.size() == 0) {
            for (Map.Entry<String, String> entry : vars.entrySet()) {
                System.out.println(entry.getKey() + "=" + entry.getValue());
            }
            return;
        }

        // Get the variable name
        String key = options.get(0);

        // The name can't include spaces
        if (options.toArray().length > 2) {
            if (!options.get(1).equals("=")) {
                return;
            }
        } else {
            return;
        }

        // If the type is a window, create a new one
        if (options.get(2).equals("window")) {
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
        vars.put(key, Exec.getRest(options, 2));
    }
}