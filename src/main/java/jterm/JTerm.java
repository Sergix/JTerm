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

import jterm.command.Command;
import jterm.command.CommandException;
import jterm.io.InputHandler;
import jterm.util.Util;
import org.reflections.Reflections;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class JTerm {
    private static final Map<String, Command> COMMANDS = new HashMap<>();

    public static final String VERSION = "0.6.1";
    public static final String PROMPT = "   \b\b\b>> ";

    // Default value of getProperty("user.dir") is equal to the default directory set when the program starts
    // Global directory variable (use "cd" command to change)
    public static String currentDirectory = System.getProperty("user.dir");

    public static boolean IS_WIN = false;
    public static boolean IS_UNIX = false;

    static {
        Util.setOS();

        Reflections reflections = new Reflections("jterm.command");
        for (Class<? extends Command> commandClass : reflections.getSubTypesOf(Command.class)) {
            try {
                Command command = commandClass.getConstructor().newInstance();
                COMMANDS.put(commandClass.getSimpleName().toLowerCase(), command);
            } catch (Exception e) {
                System.err.println("Something went wrong...\n" + e);
            }
        }
    }

    public static BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
    public static String command = "";

    public static void main(String[] args) {
        Util.setOS();

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