package jterm.util;

import jterm.JTerm;
import jterm.io.output.TextColor;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.TreeSet;

/**
 * Class that autocompletes filenames.
 */
public class FileAutocomplete {

    private static File[] files;
    private static TreeSet<String> fileNames = new TreeSet<>();
    private static Iterator<String> iterator;
    private static String[] splitCommand = {"", "", ""};
    private static String command = "", originalCommand = "", currText = "", path = "";
    private static boolean blockClear, lockTab, resetVars, endsWithDirChar, newList;
    private static int startComplete;
    private static int cursorPos;

    private static boolean available = true;

    public static String getCommand() {
        return splitCommand[0] + command + splitCommand[2];
    }

    public static File[] getFiles() {
        return files;
    }

    public static int getCursorPos() {
        return cursorPos;
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
        FileAutocomplete.cursorPos = getCommand().length();

        if ("".equals(command[1]) && (JTerm.isHeadless() || JTerm.IS_WIN))
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

    public static void resetVars() {
        files = null;
        fileNames = new TreeSet<>();
        iterator = fileNames.iterator();
        splitCommand = new String[]{"", "", ""};
        command = "";
        originalCommand = "";
        currText = "";
        path = "";
        blockClear = false;
        lockTab = false;
        resetVars = false;
        endsWithDirChar = false;
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

        Path p = path.startsWith(JTerm.dirChar) || path.startsWith("C:")
                ? Paths.get(path) : Paths.get(JTerm.currentDirectory + path);
        files = p.toFile().listFiles();

        if (files == null)
            return;

        if (!endsWithDirChar && !currText.startsWith("~") && !command.endsWith(" ")) {
            String[] split = currText.split(JTerm.IS_WIN ? "\\\\" : "/");
            currText = split[split.length - 1];
        }

        /*
         * If ends with slash directory and list not yet cleared from previous tab, clear,
         * block clear so tab rotation works and set modText to empty string,
         * so that all files in the directory are output
         */
        else if ((endsWithDirChar || currText.startsWith("~")) && !blockClear) {
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

        cursorPos = (splitCommand[0] + command).length();
    }

    /**
     * Populates the fileNames list with all files and folders matching currText.
     */
    private static void populateFileNames() {

        // For tab rotation
        startComplete = (endsWithDirChar || currText.endsWith(" ")) ? 0 : currText.endsWith(" ") ? 0 : currText.length();

        assert files != null;
        for (File f : files) {
            String fileName = f.getName();
            if ((fileName.startsWith(currText) || " ".equals(currText)) && (!f.isHidden() || currText.startsWith(".")))
                fileNames.add(f.getName());
        }

        iterator = fileNames.iterator();
    }

    /**
     * Autocompletes a string currText using files and folders contained in path.
     * Not for rotating through a list of possibilities, this method should only run
     * when there is only one option to autocomplete.
     */
    private static void autocomplete() {
        String fileName = fileNames.first();
        String end = "";

        Path p = path.startsWith(JTerm.IS_WIN ? "C:" : "/")
                ? Paths.get(path + fileName) : Paths.get(JTerm.currentDirectory + path + fileName);

        if (Files.isDirectory(p))
            end = JTerm.dirChar;

        else if (Files.isRegularFile(p))
            end = " ";

        JTerm.out.clearLine(getCommand(), getCommand().length(), false);

        command = originalCommand + fileName.substring(startComplete) + end;
        JTerm.out.print(TextColor.INPUT, getCommand());

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
            JTerm.out.clearLine(getCommand(), getCommand().length(), true);

        // Print matching file names
        if (newList)
            for (String s : fileNames)
                JTerm.out.print(TextColor.INFO, s + "\t");

            // Rotate
        else if (!lockTab || endsWithDirChar) {
            JTerm.out.clearLine(command, getCommand().length(), false);

            String currFile = iterator.next();

            command = originalCommand + currFile.substring(startComplete);
            JTerm.out.print(TextColor.INPUT, getCommand());

            // Add to end of list (rotate through list)
            fileNames.add(currFile);
        }

        if (fileNames.size() > 0 && newList) {
            // Re-output command after clearing lines
            JTerm.out.println(TextColor.INPUT);
            JTerm.out.printWithPrompt(TextColor.INPUT, getCommand());
        }
    }

    /**
     * Returns the path to the specified directory given a string.
     *
     * @return Path found
     */
    private static String getPath() {
        boolean startsAtRoot = originalCommand.startsWith("/") || currText.startsWith("/")
                || originalCommand.startsWith("C:") || currText.startsWith("C:");
        endsWithDirChar = originalCommand.endsWith(JTerm.dirChar) || currText.endsWith(JTerm.dirChar);

        StringBuilder path = new StringBuilder(currText.startsWith("~") ? JTerm.USER_HOME_DIR : "");

        if (!"".equals(path.toString())) {
            currText = currText.substring(1);
            if (startsAtRoot)
                currText = currText.substring(JTerm.IS_UNIX ? 1 : 2);
        }

        // Get path from string (discard all text after the last '/')
        for (int i = currText.length() - 1; i >= 0; i--) {
            if (currText.charAt(i) == JTerm.dirChar.charAt(0)) {
                path.append(currText.substring(0, i));
                break;
            } else if (i == 0)
                path = new StringBuilder(JTerm.currentDirectory);
        }

        // If text passed is just "" do not add a JTerm.dirChar
        if (!"".equals(path.toString()) && !"~".equals(currText))
            path.append(JTerm.dirChar);

        // If path is just JTerm.dirChar return it (top level dir)
        if (startsAtRoot && "".equals(path.toString()))
            path = new StringBuilder(JTerm.IS_UNIX ? JTerm.dirChar : "C:");

        return path.toString();
    }
}
