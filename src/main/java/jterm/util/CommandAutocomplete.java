package jterm.util;

import jterm.JTerm;
import jterm.io.output.TextColor;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

/**
 * Class that autocompletes filenames.
 */
public class CommandAutocomplete {

    private static String[] possibleCommands;
    private static String[] splitCommand = {"", "", ""};
    private static String command = "";
    private static int cursorPos;

    private static boolean resetVars = false;

    private static boolean available = true;

    public static int getCursorPos() {
        return cursorPos;
    }

    public static boolean isResetVars() {
        return resetVars;
    }

    public static String getCommand() {
        return splitCommand[0] + command + splitCommand[2];
    }

    public static String[] getPossibleCommands() {
        return possibleCommands;
    }

    public static void init(String[] commands, boolean blockClear, boolean lockTab) {
        if (!available)
            return;

        available = false;
        resetVars = false;

        CommandAutocomplete.possibleCommands = commands;

        commandAutocomplete();

        available = true;
    }

    public static void commandAutocomplete() {
        Set<String> commandList = JTerm.getCommands();
        Set<String> possibilities;

        if(CommandAutocomplete.possibleCommands == null) {
            return;
        }

        for (String compareCommand : commandList) {
            for (String inputCommand : CommandAutocomplete.possibleCommands) {
                if (compareCommand.startsWith(inputCommand)) {

                }
            }
        }
    }

    private static void autocomplete() {
        // String command = commands.getFirst();

        // Util.clearLine(command, true);

        // JTerm.out.printWithPrompt(command);
    }

    public static void resetVars() {
        possibleCommands = null;
        command = "";
    }

}
