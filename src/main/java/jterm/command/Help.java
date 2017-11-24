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

import jterm.JTerm;
import jterm.io.output.TextColor;

import java.util.List;
import java.util.TreeSet;

public class Help {
    @Command(name = "help")
    public static void printHelp(List<String> options) {
        JTerm.out.println(TextColor.INFO, "JTerm v" + JTerm.VERSION + "\n"
                + "Available Commands\n"
                + "(type [command] -h to view specific help information)");
        new TreeSet<>(JTerm.getCommands()).forEach(command -> JTerm.out.printf(TextColor.INFO, "\t%s%n", command));
    }
}
