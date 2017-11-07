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
package jterm.command;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static jterm.JTerm.logln;

public class Time {
    public Time(ArrayList<String> options) {
        if (options.contains("-h")) {
            logln("Command syntax:\n\techo [-h] input\n\nPrints the specified input to the console.", false); // Options
            return;
        }
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss , z");
        logln("The Current Time is: " + dateFormat.format(date), true);
    }
}