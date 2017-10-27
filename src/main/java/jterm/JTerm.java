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

// package = folder :P
package jterm;

import jterm.command.*;
import jterm.io.InputHandler;
import jterm.util.Util;
import org.apache.commons.lang3.SystemUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class JTerm {
    private static final Map<String, Command> COMMANDS = new HashMap<>(14);

    static {
        COMMANDS.put("clear", new Clear());
        COMMANDS.put("date",  new Date());
        COMMANDS.put("dir",   new Dir());
        COMMANDS.put("echo",  new Echo());
        COMMANDS.put("exec",  new Exec());
        COMMANDS.put("exit",  new Exit());
        COMMANDS.put("files", new Files());
        COMMANDS.put("help",  new Help());
        COMMANDS.put("pause", new Pause());
        COMMANDS.put("ping",  new Ping());
        COMMANDS.put("ps",    new Ps());
        COMMANDS.put("set",   new Set());
        COMMANDS.put("time",  new Time());
    }


    public static final String VERSION = "0.6.1";
    public static final String PROMPT = "   \b\b\b>> ";
    public static final boolean IS_WIN = SystemUtils.IS_OS_WINDOWS;
    public static final boolean IS_UNIX = SystemUtils.IS_OS_UNIX || SystemUtils.IS_OS_LINUX || SystemUtils.IS_OS_FREE_BSD;

    public static BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

    public static String currentDirectory = "./";
    public static String command = "";

    public static void main(String[] args) {
        System.out.println(
                "JTerm Copyright (C) 2017 Sergix, NCSGeek, chromechris\n"
                        + "This program comes with ABSOLUTELY NO WARRANTY.\n"
                        + "This is free software, and you are welcome to redistribute it\n"
                        + "under certain conditions.\n");


        System.out.print(PROMPT);
        while (true) {
            InputHandler.process();
        }
    }

    public static void executeCommand(String options) {
        ArrayList<String> optionsArray = Util.getAsArray(options);

        if (optionsArray.size() == 0) {
            return;
        }

        String command = optionsArray.get(0);
        optionsArray.remove(0);

        if (!COMMANDS.containsKey(command)) {
            System.out.println("Command \"" + command + "\" is not available");
            return;
        }

        try {
            COMMANDS.get(command).execute(optionsArray);
        } catch (CommandException e) {
            System.err.println(e.getMessage());
        }
    }
}