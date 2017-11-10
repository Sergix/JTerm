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
import jterm.util.Util;

import java.io.IOException;
import java.util.List;

import static jterm.JTerm.log;
import static jterm.JTerm.logln;


public class Pause implements Command {
    @Override
    public void execute(List<String> options) {
        for (String option : options) {
            if (option.equals("-h")) {
                logln("Command syntax:\n\tpause [-h] [input]", false);
                return;
            } else {
                log(Util.getRest(options, 0), true);
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