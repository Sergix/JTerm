package jterm.io;

import jterm.JTerm;
import jterm.util.Util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class InputHandler {

    // List of files for tab rotation and printing options
    private static LinkedList<String> fileNames = new LinkedList<>();

    // stores all key integer values and maps them to the values in Keys enum
    private static HashMap<Integer, Keys> keymap = new HashMap<>();

    private static ArrayList<String> prevCommands = new ArrayList<>(); // stores all entered commands
    private static int commandListPosition = 0; // position on prevCommands list (used to iterate through it)
    private static String currCommand = ""; // stores current command when iterating through prevCommands
    private static String originalCommand = ""; // stores previous command before it is modified

    // Stores JTerm.command while rotating through above list
    private static String command = "";

    // Length of original input to be completed
    private static int startComplete = 0;

    // Stops autocomplete from reprinting the text it completed if tab is pressed consecutively at the end of a complete file name
    private static boolean lockTab = false;

    // Stops autocomplete from constantly erasing fileNames list when searching sub-directories
    private static boolean blockClear = false;
    private static int[] vals = new int[3]; // for use in detecting arrow presses on Unix
    private static int pos = 0; // for use in detecting arrow presses on Unix
    private static boolean resetArrowCheck = false; // for use in resetting detection of arrow presses on Unix
    // last arrow key that was pressed (if any other key is pressed sets to ArrowKeys.NONE)
    private static ArrowKeys lastArrowPress = ArrowKeys.NONE;

    // for use in detecting arrow presses on Unix, see comment block near usage in Process()
    private static long lastPress = System.currentTimeMillis();

    /**
     * Calls appropriate method for handling
     * input read from the Input class, using
     * booleans in JTerm class to determine
     * what OS the program is running on.
     * <br></br>
     *
     */
    public static void process() {

        char input = 0;
        int i = 0;

        try {
            i = Input.read(true);
            input = (char) i;
        } catch (IOException e) {
            System.out.println("Error parsing input");
        }

        Keys key = getKey(i); // check if input value maps to a key in Keys enum

        // process input for Windows systems
        if (JTerm.IS_WIN) {
            ArrowKeys ak = arrowKeyCheckWindows(i);
            processKey(key, ak, input);
        }

		/*
            When Unix processes arrow keys, they are read as a sequence of 3 numbers, for example 27 91 65
			which means Process is called once for each of the three numbers. The first number will be processed normally,
			which can not be prevented, but the other two run 1ms after the previous one, which means those can be filtered out.
			Even when holding down a key, the interval between each detection is 30ms +-1ms, which means this approach
			causes no problems at all. If char 27 is ignored, then the program will continue to run normally, at the cost of
			the escape character in Unix systems.
		 */

        else if (JTerm.IS_UNIX) {
            ArrowKeys ak = arrowKeyCheckUnix(i);
            if ((System.currentTimeMillis() - lastPress > 20 || ak != ArrowKeys.NONE) && i != 27)
                processKey(key, ak, input);
        }

        lastPress = System.currentTimeMillis();
    }

    /**
     * Loads all integer values of keys to HashMap.
     * <br></br>
     *
     */
    public static void init() {
        keymap.put(127, Keys.BCKSP); // unix backspace
        keymap.put(8, Keys.BCKSP); // win backspace
        keymap.put((int) '\t', Keys.TAB); // unix tab
        keymap.put(9, Keys.TAB); // win tab
        keymap.put((int) '\n', Keys.NWLN); // unix newline
        keymap.put(13, Keys.NWLN); // win newline
    }

    /**
     * Checks if last input was arrow key (only on Windows).
     * <br></br>
     *
     * @param i integer value of last key press
     * @return arrow key pressed (or ArrowKeys.NONE if no arrow key was pressed)
     */
    private static ArrowKeys arrowKeyCheckWindows(int i) {
        switch (i) {
            case 57416:
                return ArrowKeys.UP;
            case 57424:
                return ArrowKeys.DOWN;
            case 57421:
                return ArrowKeys.RIGHT;
            case 57419:
                return ArrowKeys.LEFT;
            default:
                return ArrowKeys.NONE;
        }
    }

    /**
     * Checks if input was arrow key (only on Unix).
     * <br></br>
     * For more information, please see comment block near method call in process() method.
     * <br></br>
     *
     * @param i integer value of last key press
     * @return arrow key pressed (or ArrowKeys.NONE if no arrow key was pressed)
     */
    private static ArrowKeys arrowKeyCheckUnix(int i) {

        if (resetArrowCheck) {
            vals = new int[3];
            resetArrowCheck = false;
            pos = 0;
        }

        vals[pos++ % 3] = i;

        if (vals[0] == 27 && vals[1] == 91 && !resetArrowCheck) {
            switch (vals[2]) {
                case 65:
                    resetArrowCheck = true;
                    return ArrowKeys.UP;
                case 66:
                    resetArrowCheck = true;
                    return ArrowKeys.DOWN;
                case 67:
                    resetArrowCheck = true;
                    return ArrowKeys.RIGHT;
                case 68:
                    resetArrowCheck = true;
                    return ArrowKeys.LEFT;
                default:
                    return ArrowKeys.NONE;
            }
        }

        return ArrowKeys.NONE;
    }

    /**
     * Processes arrow keys presses.
     * <br></br>
     *
     * @param input last character input by user
     * @param ak    arrow key pressed (if any)
     * @return true if input processing should stop, false if input processing can continue
     */
    private static boolean processArrowKey(Keys key, ArrowKeys ak, char input) {
        if (ak != ArrowKeys.NONE && ak != ArrowKeys.MOD) {
            if (commandListPosition == prevCommands.size() && lastArrowPress == ArrowKeys.NONE)
                currCommand = JTerm.command;

            if (ak == ArrowKeys.UP && commandListPosition > 0) {
                lastArrowPress = ak;
                Util.clearLine(JTerm.command, true);

                if (commandListPosition > prevCommands.size())
                    commandListPosition = prevCommands.size();

                System.out.print(JTerm.prompt + prevCommands.get(--commandListPosition));
                JTerm.command = prevCommands.get(commandListPosition);

            } else if (ak == ArrowKeys.DOWN && commandListPosition < prevCommands.size() - 1) {
                lastArrowPress = ak;
                Util.clearLine(JTerm.command, true);

                System.out.print(JTerm.prompt + prevCommands.get(++commandListPosition));
                JTerm.command = prevCommands.get(commandListPosition);

            } else if (ak == ArrowKeys.DOWN && commandListPosition >= prevCommands.size() - 1) {
                lastArrowPress = ak;
                Util.clearLine(JTerm.command, true);
                commandListPosition++;

                System.out.print(JTerm.prompt + currCommand);
                JTerm.command = currCommand;
            }
            return true;
        } else if (ak != ArrowKeys.MOD) {
            /*
			 * Only executes when an element in the prevCommands list is not being modified.
			 * ArrowKeys.MOD used to indicate to the InputHandler that it should not go back in to this block,
			 * and continue on with execution of ProcessUnix as per usual, which will modify the string currently
			 * displayed.
			 */
            vals = new int[3];
            pos = 0;
            lastArrowPress = ArrowKeys.NONE;
            if (commandListPosition < prevCommands.size() && prevCommands.size() > 0) {
                String tmp = JTerm.command; // save current command
                int prevCMDPos = commandListPosition;
                JTerm.command = prevCommands.get(prevCMDPos); // set global command to one currently displayed
                if (originalCommand.equals(""))
                    originalCommand = JTerm.command;
                processKey(key, ArrowKeys.MOD, input); // process current input and apply on currently displayed command
                if (JTerm.command.equals("")) { // if Keys.NWLN was input, set command to tmp to avoid blank lines in list
                    prevCommands.set(prevCMDPos, originalCommand);
                    originalCommand = "";
                } else { // if no newline, follow as per usual (see below)
                    prevCommands.set(prevCMDPos, JTerm.command); // set command to modified version
                    JTerm.command = tmp; // set global command back to previous state
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Returns associated key value from HashMap.
     * <br></br>
     *
     * @param i integer value of key pressed
     */
    private static Keys getKey(int i) {
        return keymap.get(i);
    }

    /**
     * Processes input provided by Input class,
     * and operates based on the input it receives,
     * using the character value passed by the process() method.
     * <br></br>
     *
     * @param input last character input by user
     * @param ak    arrow key pressed (if any)
     */
    private static void processKey(Keys key, ArrowKeys ak, char input) {

        if (processArrowKey(key, ak, input))
            return;

        boolean clearFilesList = true;

        // Do not output tabs, caps lock and backspace chars
        if (key != Keys.BCKSP && key != Keys.TAB)
            System.out.print(input);

        // reset if input is not tab
        if (key != Keys.TAB) {
            lockTab = false;
            blockClear = false;
        }

        // Back Space
        if (key == Keys.BCKSP) {
            if (JTerm.command.length() > 0) {
                JTerm.command = JTerm.command.substring(0, JTerm.command.length() - 1);

                // Delete char, add white space and move back again
                System.out.print("\b \b");
            }
        }

        // Special chars, more can be added
        //else if (",./\\-_+=~".contains(String.valueOf(input)))
        //    JTerm.command += input;

        // Tab
        else if (key == Keys.TAB && JTerm.command.length() > 0) {
            clearFilesList = false;

            // Split into sections
            String[] commandArr = JTerm.command.split(" ");

            // Get last element
            String currText = commandArr[commandArr.length - 1] + (JTerm.command.endsWith(" ") ? " " : "");

            // If more than one element, autocomplete file
            if (commandArr.length > 1 || JTerm.command.endsWith(" "))
                fileAutocomplete(currText);

                // If one element, autocomplete command (to be implemented)
            else if (commandArr.length == 1 && !JTerm.command.endsWith(" "))
                commandAutocomplete(currText);
        }

        // Enter, or new line
        else if (key == Keys.NWLN) {
            boolean empty = containsOnlySpaces(JTerm.command);
            if (JTerm.command.length() > 0 && !empty)
                JTerm.parse(JTerm.command);

            if (!empty)
                prevCommands.add(JTerm.command);
            commandListPosition = prevCommands.size();
            currCommand = "";
            JTerm.command = "";
            System.out.print("\n" + JTerm.prompt);

        }

        // Just print it if it is defined
        else if (Character.isDefined(input))
            JTerm.command += input;

        // clear fileNames list and reset command string
        if (fileNames.size() > 0 && clearFilesList) {
            fileNames.clear();
            command = "";
        }
    }

    /**
     * Determines if a string is composed only of spaces.
     * <br></br>
     *
     * @param s string to check
     * @return true if s is composed of only spaces, false if there is a character in it
     */
    private static boolean containsOnlySpaces(String s) {
        for (int i = 0; i < s.length(); i++)
            if (s.charAt(i) != ' ')
                return false;
        return true;
    }

    /**
     * Using a string of text representing what has been typed presently,
     * displays all files that match the current input.
     * <br></br>
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
            for (int i = 0; (i < (splitPath.length - 1) && !endsWithSlash) || (i < splitPath.length && endsWithSlash); i++)
                path += "/" + splitPath[i];
            path += "/";
        }

        // get folder at path
        File currFolder = new File(JTerm.currentDirectory + path);
        File[] files = currFolder.listFiles();

        // if not empty parameter or not directory
        if (!endsWithSlash && !JTerm.command.endsWith(" "))
            currText = splitPath[splitPath.length - 1];

            // if ends with slash directory and list not yet cleared from previous tab, clear, block clear so tab rotation works and set
            // currText to empty string, so that all files in the directory are output
        else if (endsWithSlash && !blockClear) {
            fileNames.clear();
            blockClear = true;
            currText = " ";
        }

        // if command ends with empty space, output all files in path
        else if (JTerm.command.endsWith(" "))
            currText = " ";

        // get all file names for comparison
        if (fileNames.size() == 0) {
            // For tab rotation, true means no tab rotation, false means rotate through list
            newList = true;

            // Stores original command so that JTerm.command does not keep adding to itself
            command = JTerm.command;

            // For autocomplete in tab rotation
            startComplete = (endsWithSlash || currText.endsWith(" ")) ? 0 : currText.length();

            // add all files with matching names to list
            assert files != null;
            for (File f : files)
                if (f.getName().startsWith(currText))
                    fileNames.add(f.getName());

        }

        if (fileNames.size() != 1) {
            // Clear line
            if (fileNames.size() > 0 || currText.equals(" "))
                Util.clearLine(JTerm.command, true);

            // Print matching file names
            if (newList)
                for (String s : fileNames)
                    System.out.print(s + "\t");

            else if (!lockTab || endsWithSlash) {
                Util.clearLine(JTerm.command, true);

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
                    Util.clearLine(JTerm.command, true);

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
            if (Files.isDirectory(Paths.get(JTerm.currentDirectory + path + fileName)))
                end = "/";

                // if auto-completing a file, add space at end
            else if (Files.isRegularFile(Paths.get(JTerm.currentDirectory + path + fileName)))
                end = " ";

            //System.out.println("\n" + JTerm.currentDirectory + path + fileName);
            JTerm.command += fileName.substring(currText.length(), fileName.length()) + end;
            System.out.print(fileName.substring(currText.length(), fileName.length()) + end);

            // Lock tab
            lockTab = true;
        }
    }

    /**
     * @param currText command that is to be completed
     */
    private static void commandAutocomplete(String currText) {
        // TODO: autocomplete commands
    }
}

enum ArrowKeys {
    UP, DOWN, LEFT, RIGHT, NONE, MOD // for info on MOD use, please see ProcessArrowKey()
}

enum Keys {
    TAB, BCKSP, NWLN, NONE
}