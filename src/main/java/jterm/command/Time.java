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

import jterm.JTerm;
import jterm.io.output.TextColor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Time {
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss , z");

    @Command(name = "time")
    public static void printTime(List<String> options) {
        JTerm.out.printf(TextColor.INFO, "The Current Time is: %s%n", DATE_FORMAT.format(new Date()));
    }
}