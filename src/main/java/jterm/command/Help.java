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

import java.util.List;

import static jterm.JTerm.logln;

public class Help implements Command {
    @Override
    public void execute(List<String> options) {
        logln("JTerm v" + JTerm.VERSION + "\n"
                + "Available commands:\n"
                + "  client\n"
                + "  dir\n"
                + "  echo\n"
                + "  exec\n"
                + "  exit\n"
                + "  files\n"
                + "  pause\n"
                + "  ping\n"
                + "  ps\n"
                + "  server\n"
                + "  set\n"
                + "  window\n", true);
    }
}
