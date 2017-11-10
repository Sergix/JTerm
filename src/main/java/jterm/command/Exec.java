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

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import static jterm.JTerm.logln;


public class Exec implements Command {
    @Override
    public void execute(List<String> options) {
        if (options.size() == 0) {
            logln("Command usage: exec <executable>", false);
            return;
        }

        String command = options.get(0);
        if (!command.startsWith("java") && command.endsWith(".jar")) {
            command = "java -jar " + command;
        }

        run(command);
    }

    public static void run(String command) {
        try {
            Process process = Runtime.getRuntime().exec(command);
            Scanner in = new Scanner(process.getInputStream());

            while (process.isAlive() && in.hasNextLine()) {
                logln(in.nextLine(), true);
            }

            while (in.hasNextLine()) {
                logln(in.nextLine(), true);
            }

            in.close();
        } catch (IOException e) {
            throw new CommandException("Failed to execute command \"" + command + "\"");
        }
    }
}