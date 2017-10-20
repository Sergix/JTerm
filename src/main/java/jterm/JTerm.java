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

import jterm.command.Exec;
import org.apache.commons.lang3.SystemUtils;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class JTerm {
    // Global version variable
    // TODO: maybe better to get the version from some property file?
    // like: version = Utils.getProperty("project.version");
    public static final String version = "0.5.2";

    // Global directory variable (use "cd" command to change)
    // Default value "./" is equal to the default directory set when the program starts
    public static String currentDirectory = "./";
    public static final boolean isWin = SystemUtils.IS_OS_WINDOWS;
    public static final boolean isUnix = SystemUtils.IS_OS_UNIX || SystemUtils.IS_OS_LINUX || SystemUtils.IS_OS_FREE_BSD;

    // User input variable used among all parts of the application
    public static BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

    // Boolean to determine if caps lock is on, since input system does not distinguish between character cases
    // Command string which the input system will aggregate characters to
    public static boolean capsOn = Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK);
    public static String command = "";

    public static void main(String[] args) {
        // Print licensing information
        System.out.println(
                "JTerm Copyright (C) 2017 Sergix, NCSGeek, chromechris\n"
                        + "This program comes with ABSOLUTELY NO WARRANTY.\n"
                        + "This is free software, and you are welcome to redistribute it\n"
                        + "under certain conditions.\n");

		/*
        * Wait until "exit" is typed in to exit
		* Sends last char received from Input class to Process function
		*/
        while (true) {
            InputHandler.Process();
        }
    }

    /*
    * parse() boolean
    *
    * Checks input and passes command options to the function
    * that runs the requested command.
    *
    * ArrayList<String> options - command options
    */
    public static boolean parse(String options) {
        ArrayList<String> optionsArray = getAsArray(options);

        // Default to process/help command if function is not found
        String method = "Process";

        // Get the first string in the options array, which is the command,
        // and capitalize the first letter of the command
        String original = optionsArray.get(0).toLowerCase(), command = original;
        String classChar = command.substring(0, 1);
        classChar = classChar.toUpperCase();
        command = command.substring(1);
        command = "jterm.command." + classChar + command;
        optionsArray.remove(0);

        // Get the method name
        if (optionsArray.toArray().length >= 1) {
            method = optionsArray.get(0);
        } else {
            optionsArray.add(method);
        }

        classChar = method.substring(0, 1);
        classChar = classChar.toUpperCase();
        method = method.substring(1);
        method = classChar + method;

        try {
            // Get the class of the command
            Class<?> clazz = Class.forName(command);
            Constructor<?> constructor = clazz.getConstructor(ArrayList.class);
            Object obj = constructor.newInstance(optionsArray);

            ArrayList<Method> methods = new ArrayList<Method>(Arrays.asList(obj.getClass().getDeclaredMethods()));

            // Invoke the correct method of the class to run, but only if it contains that method
            Method m = obj.getClass().getMethod(method, ArrayList.class);
            if (methods.contains(m)) {
                optionsArray.remove(0);
                m.invoke(options.getClass(), optionsArray);
            }
        } catch (ClassNotFoundException e) {
            ArrayList<String> execFile = new ArrayList<>();
            execFile.add(original);
            if (Exec.Run(execFile)) {
                System.out.println("Unknown Command \"" + original + "\"");
            }
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
            System.out.println(e);
        } catch (NoSuchMethodException e) {
            // ignore exception
        }

        return false;
        // 	// Commands to skip in batch files
        // 	case "bcdedit":
        // 	case "chkdsk":
        // 	case "chkntfs":
        // 	case "cls":
        // 	case "cmd":
        // 	case "color":
        // 	case "convert":
        // 	case "diskpart":
        // 	case "driverquery":
        // 	case "format":
        // 	case "fsutil":
        // 	case "gpresult":
        // 	case "mode":
        // 	case "sc":
        // 	case "shutdown":
        // 	case "start":
        // 	case "tasklist":
        // 	case "taskkill":
        // 	case "ver":
        // 	case "vol":
        // 	case "wmic":
        // 		break;
    }

    /*
    * getAsArray() ArrayList<String>
    *
    * Returns a String as an ArrayList of
    * Strings (spaces as delimiters)
    *
    * String options - String to be split
    */
    public static ArrayList<String> getAsArray(String options) {
        try (Scanner tokenizer = new Scanner(options)) {
            ArrayList<String> optionsArray = new ArrayList<>();
            while (tokenizer.hasNext()) {
                optionsArray.add(tokenizer.next());
            }
            return optionsArray;
        }
    }

    /*
    * getAsString() String
    *
    * Returns an ArrayList of Strings
    * as a String (separated with spaces)
    *
    * ArrayList<String> options - array to be split
    */
    public static String getAsString(ArrayList<String> options) {
        StringBuilder result = new StringBuilder();
        for (String option : options) {
            result.append(option);
            result.append(" ");
        }
        return result.substring(0, result.length() - 1);
    }
}