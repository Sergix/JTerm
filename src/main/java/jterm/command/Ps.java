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

import static jterm.JTerm.logln;

public class Ps implements Command {
    private static final String PS_COMMAND;

    static {
        if (JTerm.IS_UNIX) {
            PS_COMMAND = "ps -e";
        } else if (JTerm.IS_WIN) {
            PS_COMMAND = System.getenv("windir") + "\\system32\\" + "tasklist.exe";
        } else {
            throw new Error("Undefined operating system");
        }
    }

    @Override
    public void execute(List<String> options) {
        if (options.contains("-h")) {
            logln("Command syntax:\n\tps [-h]\n\nDisplays all current processes running on the host system.", false);
            return;
        }
        Exec.run(PS_COMMAND);
    }
}