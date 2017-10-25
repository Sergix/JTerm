package jterm.io;

import jterm.JTerm;
import jterm.util.Util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;

public class InputHandler {

    // List of files for tab rotation and printing options
    static LinkedList<String> fileNames = new LinkedList<>();

    static ArrayList<String> prevCommands = new ArrayList<>(); // stores all entered commands
    static int commandListPosition = 0; // position on prevCommands list (used to iterate through it)
    static String currCommand = ""; // stores current command when iterating through prevCommands

    // Stores JTerm.command while rotating through above list
    static String command = "";

    // Length of original input to be completed
    private static int startComplete = 0;

    // Stops autocomplete from reprinting the text it completed if tab is pressed consecutively at the end of a complete file name
    static boolean lockTab = false;

    // Stops autocomplete from constantly erasing fileNames list when searching sub-directories
    static boolean blockClear = false;

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

        Keys key = KeyHandler.getKey(i); // check if input value maps to a key in Keys enum

        // process input for Windows systems
        if (JTerm.IS_WIN) {
            ArrowKeys ak = ArrowKeyHandler.arrowKeyCheckWindows(i);
            KeyHandler.processKey(key, ak, input);
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
            ArrowKeys ak = ArrowKeyHandler.arrowKeyCheckUnix(i);
            if ((System.currentTimeMillis() - lastPress > 20 || ak != ArrowKeys.NONE) && i != 27)
                KeyHandler.processKey(key, ak, input);
        }

        lastPress = System.currentTimeMillis();
    }

    /**
     * Loads all integer values of keys to HashMap.
     * <br></br>
     *
     */
    public static void init() {
        KeyHandler.init();
    }

    /**
     * Using a string of text representing what has been typed presently,
     * displays all files that match the current input.
     * <br></br>
     *
     * @param currText file that is to be completed
     */
    static void fileAutocomplete(String currText) {

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
    static void commandAutocomplete(String currText) {
        // TODO: autocomplete commands
    }
}

