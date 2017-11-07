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

import java.util.ArrayList;
import java.io.IOException;

import jterm.JTerm;

import static jterm.JTerm.log;
import static jterm.JTerm.logln;

public class Pause {
    /*
    * Pause() void
    *
    * Pauses the interpreter until the user
    * hits the "Enter" key.
    *
    * ArrayList<String> options - command options
    *
    * message
    * 	Pause message to be printed
    */
    public Pause(ArrayList<String> options) {
        for (String option : options) {
            if (option.equals("-h")) {
                logln("Command syntax:\n\tpause [-h] [input]\n\nPauses the terminal and awaits a keypress.", false);
                return;

            } else {
                log(Exec.getRest(options, 0), true);
            }
        }

        if (options.size() == 0) {
            log("Press enter to continue...", true);
        }
        //TODO: Figure out what this is doing and how to do it in GUI
        try {
            JTerm.userInput.read();
            JTerm.userInput.skip(1);
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}