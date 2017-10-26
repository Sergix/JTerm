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

public class Ps implements Command {
    @Override
    public void execute(List<String> options) {
        if (options.contains("-h")) {
            System.out.println("Command syntax:\n\tps [-h]\n\nDisplays all current processes running on the host system.");
            return;
        }

        String command;
        if (JTerm.IS_UNIX) {
            command = "ps -e";
        } else if (JTerm.IS_WIN) {
            command = System.getenv("windir") + "\\system32\\" + "tasklist.exe";
        } else {
            throw new Error("Undefined OS");
        }

        Exec.run(command);
    }
}