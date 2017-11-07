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

import java.util.ArrayList;

import static jterm.JTerm.logln;

public class Echo {
    /*
    * Echo() void
    *
    * Echo the input to the terminal.
    *
    * ArrayList<String> options - command options
    *
    * -h
    * 	Prints help information
    * input
    * 	Text to output
    */
    // FIXME: echo is not working correctly, for example: > echo $JAVA_HOME
    public Echo(ArrayList<String> options) {
        StringBuilder outputBuilder = new StringBuilder();
        for (String option : options) {
            if (option.equals("-h")) {
                logln("Command syntax:\n\techo [-h] input\n\nPrints the specified input to the console.", false);
                return;
            } else {
                outputBuilder
                        .append(option)
                        .append(" ");
            }
        }
        logln(outputBuilder.substring(0, outputBuilder.length() - 1), true);
    }
}
