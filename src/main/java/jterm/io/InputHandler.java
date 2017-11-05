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

/*
* Special thanks to @nanoandrew4 for this feature!
* https://github.com/Sergix/JTerm/issues/31
*/

package jterm.io;

import jterm.JTerm;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;

public class InputHandler {
    // List of files for tab rotation and printing options
    private static LinkedList<String> fileNames = new LinkedList<>();

    // Stores JTerm.command while rotating through above list
    private static String command = "";

    // Length of original input to be completed
    private static int startComplete = 0;

    // Stops autocomplete from reprinting the text it completed if tab is pressed consecutively at the end of a complete file name
    private static boolean lockTab = false;

    // Stops autocomplete from constantly erasing fileNames list when searching sub-directories
    private static boolean blockClear = false;

    /**
     * process() void
     * <p></p>
     * Calls appropriate method for handling
     * input read from the Input class, using
     * booleans in JTerm class to determine
     * what OS the program is running on.
     */
    public static void process() {
        char input = 0;

        try {
            input = (char) Input.read(true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (JTerm.IS_WIN) {
            processWin(input);
        } else if (JTerm.IS_UNIX) {
            processUnix(input);
        }
    }

    /**
     * processUnix() void
     * <p></p>
     * Processes input provided by Input class,
     * and operates based on the input it receives,
     * using the characters values used in Unix
     * systems.
     * <p>
     * char input - last character input by user
     */
    private static void processUnix(char input) {
        boolean clearFilesList = true;

        // Do not output tabs, caps lock and backspace chars
        if (input != 127 && input != 9) {
            System.out.print(input);
        }

        if (input != 9) {
            lockTab = false;
            blockClear = false;
        }

        // Back Space
        if (input == 127) {
            if (JTerm.command.length() > 0) {
                JTerm.command = JTerm.command.substring(0, JTerm.command.length() - 1);

                // delete char, add white space and move back again
                System.out.print("\b \b");
            }
        }

        // Special chars, more can be added
        else if (",./\\-_+=~".contains(String.valueOf(input))) {
            JTerm.command += input;
        }

        // Tab
        else if (input == '\t' && JTerm.command.length() > 0) {
            clearFilesList = false;

            // Split into sections
            String[] commandArr = JTerm.command.split(" ");

            // Get last element
            String currText = commandArr[commandArr.length - 1] + (JTerm.command.endsWith(" ") ? " " : "");

            // If more than one element, autocomplete file
            if (commandArr.length > 1 || JTerm.command.endsWith(" ")) {
                fileAutocomplete(currText);
            }

            // If one element, autocomplete command (to be implemented)
            else if (commandArr.length == 1 && !JTerm.command.endsWith(" ")) {
                commandAutocomplete(currText);
            }
        }

        // Enter, or new line
        else if (input == '\n') {
            if (JTerm.command.length() > 0) {
                JTerm.parse(JTerm.command);
            }

            JTerm.command = "";
            System.out.print("\n" + JTerm.prompt);
        }

        // Just print it if it is defined
        else if (Character.isDefined(input))
            JTerm.command += input;

        if (fileNames.size() > 0 && clearFilesList) {
            fileNames.clear();
            command = "";
        }
    }

    /**
     * processWin() void
     * <p></p>
     * Processes input provided by Input class,
     * and operates based on the input it receives,
     * using the characters values used in Windows systems.
     * <p>
     * char input - last character input by user
     */
    private static void processWin(char input) {
        boolean clearFilesList = true;

        if (input != 8 && input != 9) {
            System.out.print(input);
        }

        if (input != 9) {
            lockTab = false;
            blockClear = false;
        }

        // Backspace
        if (input == 8) {
            if (JTerm.command.length() > 0) {
                JTerm.command = JTerm.command.substring(0, JTerm.command.length() - 1);

                // delete char, add white space and move back again
                System.out.print("\b \b");
            }
        }

        // Tab
        else if (input == 9 && JTerm.command.length() > 0) {
            clearFilesList = false;

            // Split into sections
            String[] commandArr = JTerm.command.split(" ");

            // Get last element
            String currText = commandArr[commandArr.length - 1] + (JTerm.command.endsWith(" ") ? " " : "");

            // If more than one element, autocomplete file
            if (commandArr.length > 1) {
                fileAutocomplete(currText);
            }

            // If one element, autocomplete command (to be implemented)
            else if (commandArr.length == 1 && !JTerm.command.endsWith(" ")) {
                commandAutocomplete(currText);
            }
        }

        // New line
        else if (input == 13) {
            System.out.println("\r\n");
            if (JTerm.command.length() > 0) {
                JTerm.parse(JTerm.command);
            }

            JTerm.command = "";
            System.out.print("\r\n" + JTerm.prompt);
        }

        // just print it if it is defined
        else if (Character.isDefined(input))
            JTerm.command += input;

        if (fileNames.size() > 0 && clearFilesList) {
            fileNames.clear();
            command = "";
        }
    }

    /**
     * fileAutocomplete()
     * <p></p>
     * Using a string of text representing what has been typed presently,
     * displays all files that match the current input.
     *
     * @param currText file that is to be completed
     */
    private static void fileAutocomplete(String currText) {
        boolean newList = false;
        // whether command ends with slash or not
        boolean endsWithSlash = command.endsWith("/") ? command.endsWith("/") : JTerm.command.endsWith("/");

        // split text at slashes to get path, so that relevant files can be autocompleted or displayed
        String[] splitPath = currText.split("/");
        String path = "";
        if (splitPath.length > 0) {
            // re-create path to look in
            for (int i = 0; (i < (splitPath.length - 1) && !endsWithSlash) || (i < splitPath.length && endsWithSlash); i++) {
                path += "/" + splitPath[i];
            }
            path += "/";
        }

        // get folder at path
        File currFolder = new File(JTerm.currentDirectory + path);
        File[] files = currFolder.listFiles();

        // fixes NullPointerException (@sergix)
        if (files == null) {
            return;
        }

        // if not empty parameter or not directory
        if (!endsWithSlash && !JTerm.command.endsWith(" ")) {
            currText = splitPath[splitPath.length - 1];
        }

        // if ends with slash directory and list not yet cleared from previous tab, clear, block clear so tab rotation works and set
        // currText to empty string, so that all files in the directory are output
        else if (endsWithSlash && !blockClear) {
            fileNames.clear();
            blockClear = true;
            currText = " ";
        }

        // if command ends with empty space, output all files in path
        else if (JTerm.command.endsWith(" ")) {
            currText = " ";
        }

        // get all file names for comparison
        if (fileNames.size() == 0) {
            // For tab rotation, true means no tab rotation, false means rotate through list
            newList = true;

            // Stores original command so that JTerm.command does not keep adding to itself
            command = JTerm.command;

            // For autocomplete in tab rotation
            startComplete = (endsWithSlash || currText.endsWith(" ")) ? 0 : currText.length();

            // add all files with matching names to list
            for (File f : files) {
                if (f.getName().startsWith(currText)) {
                    fileNames.add(f.getName());
                }
            }

        }

        if (fileNames.size() != 1) {
            // Clear line
            if (fileNames.size() > 0 || currText.equals(" ")) {
                clearLine(JTerm.command);
            }

            // Print matching file names
            if (newList) {
                for (String s : fileNames) {
                    System.out.print(s + "\t");
                }
            } else if (!lockTab || endsWithSlash) {
                clearLine(JTerm.command);

                // Get first file or dir name
                String currFile = fileNames.pollFirst();

                // Autocomplete with first file or dir name
                JTerm.command = command + currFile.substring(startComplete, currFile.length());

                // Print to screen
                System.out.print(JTerm.prompt + JTerm.command);

                // Add file or dir name at end of list
                fileNames.add(currFile);

            }

            if (fileNames.size() > 0 && newList) {
                System.out.println();

                // Re-output command after clearing lines
                System.out.print(JTerm.prompt + JTerm.command);

            }

            // If no input, just output all files and folders
            if (currText.equals(" ")) {
                if (newList) {
                    for (File f : files) {
                        System.out.print(f.getName() + " \t");
                        fileNames.add(f.getName());
                    }

                    // Improve readability
                    System.out.println("\n");

                    // Re-output command after clearing lines
                    System.out.print(JTerm.prompt + JTerm.command);

                } else if (!lockTab) {
                    clearLine(JTerm.command);

                    // Get first file or dir name
                    String currFile = fileNames.pollFirst();

                    // Autocomplete with first file or dir name
                    JTerm.command = command + currFile.substring(startComplete, currFile.length());

                    // Print to screen
                    System.out.print(JTerm.prompt + JTerm.command);

                    // Add file or dir name at end of list
                    fileNames.add(currFile);
                }
            }

        } else if (!lockTab) {

            String fileName = fileNames.getFirst();
            String end = "";

            // if auto-completing directory, add slash at end
            if (Files.isDirectory(Paths.get(JTerm.currentDirectory + path + fileName))) {
                end = "/";
            }

            // if auto-completing a file, add space at end
            else if (Files.isRegularFile(Paths.get(JTerm.currentDirectory + path + fileName))) {
                end = " ";
            }

            //System.out.println("\n" + JTerm.currentDirectory + path + fileName);
            JTerm.command += fileName.substring(currText.length(), fileName.length()) + end;
            System.out.print(fileName.substring(currText.length(), fileName.length()) + end);

            // Lock tab
            lockTab = true;
        }
    }

    /**
     * clearLine() void
     * <p></p>
     * Clears a line in the console of size line.length().
     *
     * @param line line to be cleared
     */
    private static void clearLine(String line) {
        for (int i = 0; i < line.length() + JTerm.prompt.length() / 3; i++) {
            System.out.print("\b");
        }

        for (int i = 0; i < line.length() + JTerm.prompt.length() / 3; i++) {
            System.out.print(" ");
        }

        for (int i = 0; i < line.length() + JTerm.prompt.length() / 3; i++) {
            System.out.print("\b");
        }
    }

    /**
     * commandAutocomplete()
     * <p></p>
     *
     * @param currText command that is to be completed
     */
    private static void commandAutocomplete(String currText) {
    }
}