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

import java.util.List;

public class Ping {
    @Command(name = "ping", minOptions = 1, syntax = "ping [-h] host")
    public static void ping(final List<String> options) {
        final String host = options.get(0);
        try {
            final String cmd;
            if (JTerm.IS_WIN)
                // For Windows
                cmd = "ping -n 1 " + host;
            else
                // For Linux and OSX
                cmd = "ping -c 1 " + host;

            final Process myProcess = Runtime.getRuntime().exec(cmd);
            myProcess.waitFor();

            if (myProcess.exitValue() != 0)
                throw new CommandException("Ping failed");
        } catch (Exception e) {
            throw new CommandException("Ping command failed");
        }
    }
}
