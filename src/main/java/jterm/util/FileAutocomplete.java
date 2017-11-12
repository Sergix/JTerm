package jterm.util;

import jterm.JTerm;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;

/**
 * Class that autocompletes filenames.
 */
public class FileAutocomplete {

    private static File[] files;
    private static LinkedList<String> fileNames = new LinkedList<>();
    private static String[] splitCommand = {"", "", ""};
    private static String command = "", originalCommand = "", currText = "", path = "";
    private static boolean blockClear, lockTab, resetVars, endsWithSlash, newList;
    private static int startComplete;

    private static boolean available = true;

    public static String getCommand() {
        return splitCommand[0] + command + splitCommand[2];
    }

    public static File[] getFiles() {
        return files;
    }

    public static boolean isBlockClear() {
        return blockClear;
    }

    public static boolean isLockTab() {
        return lockTab;
    }

    public static boolean isResetVars() {
        return resetVars;
    }

    /**
     * Sets all variables needed for attempting to autocomplete a file or directory name, and then runs the algorithm.
     *
     * @param command    Command with complete path to autocomplete (not necessarily the same as currText)
     * @param blockClear True if method should not delete the file names currently stored, which enables tab rotation
     * @param lockTab    Whether the tab key should be processed or not
     */
    public static void init(String[] command, boolean blockClear, boolean lockTab) {

        if (!available)
            return;

        available = false;
        resetVars = false;

        FileAutocomplete.splitCommand = command;
        FileAutocomplete.command = command[1];
        FileAutocomplete.blockClear = blockClear;
        FileAutocomplete.lockTab = lockTab;

        if ("".equals(command[1]))
            FileAutocomplete.command = " ";

        // Stores original command so that command does not keep adding to itself
        originalCommand = FileAutocomplete.command;

        fileAutocomplete();

        available = true;
    }

    public static void setCommand(String command) {
        FileAutocomplete.command = command;

        String[] commandArr = command.split(" ");
        FileAutocomplete.currText = command.endsWith(" ") ? "" : commandArr[commandArr.length - 1];
    }

    protected static void setCurrText(String currText) {
        FileAutocomplete.currText = currText;
    }

    public static void resetVars() {
        files = null;
        fileNames = new LinkedList<>();
        splitCommand = new String[]{"", "", ""};
        command = "";
        originalCommand = "";
        currText = "";
        path = "";
        blockClear = false;
        lockTab = false;
        resetVars = false;
        endsWithSlash = false;
        newList = false;
        startComplete = 0;
    }

    /**
     * Using a string of text representing what has been typed presently,
     * displays all files that match the current input, if more than one options are available.
     * Otherwise autocompletes the file name.
     */
    public static void fileAutocomplete() {

        String[] commandArr = originalCommand.split(" ");
        FileAutocomplete.currText = originalCommand.endsWith(" ") ? " " : commandArr[commandArr.length - 1];

        // Whether a new list of files should be created or not
        newList = false;

        // Path to directory in which to find files to complete with
        path = getPath();

        Path p = path.startsWith("/") ? Paths.get(path) : Paths.get(JTerm.currentDirectory + path);
        files = p.toFile().listFiles();

        if (files == null)
            return;

        if ((!endsWithSlash && !currText.startsWith("~")) && !command.endsWith(" "))
            currText = currText.split("/")[currText.split("/").length - 1];

        /*
         * If ends with slash directory and list not yet cleared from previous tab, clear,
         * block clear so tab rotation works and set modText to empty string,
         * so that all files in the directory are output
         */
        else if ((endsWithSlash || currText.startsWith("~")) && !blockClear) {
            fileNames.clear();
            blockClear = true;
            currText = " ";
        }

        if (fileNames.size() == 0) {
            newList = true;
            populateFileNames();
        }

        if (fileNames.size() != 1)
            fileNamesIterator();
        else if (!lockTab)
            autocomplete();
    }

    /**
     * Populates the fileNames list with all files and folders matching currText.
     */
    private static void populateFileNames() {

        // For tab rotation
        startComplete = (endsWithSlash || currText.endsWith(" ")) ? 0 : currText.endsWith(" ") ? 0 : currText.length();

        assert files != null;
        for (File f : files) {
            String fileName = f.getName();
            if ((fileName.startsWith(currText) || " ".equals(currText)) && (!f.isHidden() || currText.startsWith(".")))
                fileNames.add(f.getName());
        }
    }

    /**
     * Autocompletes a string currText using files and folders contained in path.
     * Not for rotating through a list of possibilities, this method should only run
     * when there is only one option to autocomplete.
     */
    private static void autocomplete() {
        String fileName = fileNames.getFirst();
        String end = "";

        Path p = path.startsWith("/") ? Paths.get(path + fileName) : Paths.get(JTerm.currentDirectory + path + fileName);

        if (Files.isDirectory(p))
            end = "/";

        else if (Files.isRegularFile(p))
            end = " ";

        Util.clearLine(getCommand(), true);

        command = originalCommand + fileName.substring(startComplete) + end;
        System.out.print(JTerm.PROMPT + getCommand());

        lockTab = true;

        resetVars = true;
    }

    /**
     * First will display all files and directories matching 'currText' when tab key is pressed.
     * <br></br>
     * <p>
     * After this has happened, provided no other keys have been pressed (excluding tab),
     * it will iterate through all the file and directory names that match 'currText', printing the
     * missing characters to the terminal so the user can see, and appending those autocompleted
     * characters to the end of the command variable.
     */
    private static void fileNamesIterator() {

        // Clear line
        if (fileNames.size() > 0 || " ".equals(currText))
            Util.clearLine(getCommand(), true);

        // Print matching file names
        if (newList)
            for (String s : fileNames)
                System.out.print(s + "\t");

            // Rotate
        else if (!lockTab || endsWithSlash) {
            Util.clearLine(command, true);

            String currFile = fileNames.pollFirst();

            command = originalCommand + currFile.substring(startComplete);
            System.out.print(JTerm.PROMPT + getCommand());

            // Add to end of list (rotate through list)
            fileNames.add(currFile);
        }

        if (fileNames.size() > 0 && newList)
            // Re-output command after clearing lines
            System.out.print("\n" + JTerm.PROMPT + getCommand());
    }

    /**
     * Returns the path to the specified directory given a string.
     *
     * @return Path found
     */
    protected static String getPath() {
        boolean startsWithSlash = originalCommand.startsWith("/") || currText.startsWith("/");
        endsWithSlash = originalCommand.endsWith("/") || currText.endsWith("/");

        StringBuilder path = new StringBuilder(currText.startsWith("~") ? JTerm.USER_HOME_DIR : "");

        if (!"".equals(path.toString())) {
            currText = currText.substring(1);
            if (startsWithSlash)
                currText = currText.substring(1);
        }

        // Get path from string (discard all text after the last '/')
        for (int i = currText.length() - 1; i >= 0; i--) {
            if (currText.charAt(i) == '/') {
                path.append(currText.substring(0, i));
                break;
            } else if (i == 0)
                path = new StringBuilder(JTerm.currentDirectory);
        }

        // If text passed is just "" do not add a "/"
        if (!"".equals(path.toString()) && !"~".equals(currText))
            path.append("/");

        // If path is just "/" return it (top level dir)
        if (startsWithSlash && "".equals(path.toString()))
            path = new StringBuilder("/");

        return path.toString();
    }
}
