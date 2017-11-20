package jterm.io;

import jterm.JTerm;
import jterm.util.FileAutocomplete;
import jterm.util.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class InputHandler {

    // Position on prevCommands list (used to iterate through it)
    private static int commandListPosition = 0;
    // Stores current TermInputProcessor.command when iterating through prevCommands
    private static String currCommand = "";
    // Stores all entered commands
    private static ArrayList<String> prevCommands = new ArrayList<>();
    private static String command = "";
    // Stops autocomplete from reprinting command it completed if tab is pressed at the end of a complete file name
    private static boolean lockTab = false;
    // Stops autocomplete from constantly erasing fileNames list when searching sub-directories
    private static boolean blockClear = false;
    // For resetting all variables in FileAutocomplete once a key press other than a tab is registered
    private static boolean resetVars = false;
    private static int cursorPos = 0;
    private static char lastChar;

    // Last arrow key that was pressed (if any other key is pressed sets to Keys.NONE)
    private static Keys lastArrowPress = Keys.NONE;

    private static void setCurrCommand(String curr) {
        currCommand = curr;
    }

    private static void setCommandListPosition(int commandListPos) {
        commandListPosition = commandListPos;
    }


    public static void read() throws IOException {
        int c1 = RawConsoleInput.read(true);
        int c2 = RawConsoleInput.read(false);
        int c3 = RawConsoleInput.read(false);
        Keys keyType;
        if (c2 == -2 && c3 == -2) {
            keyType = Keys.getKeyByValue(c1);
        } else {
            keyType = Keys.getKeyByValue((c1 + c2 + c3) * -1);
        }
        process(keyType, (char) c1);
    }

    public static void process(Keys key, char c) {
        lastChar = c;
        key.executeAction();
    }

    static void ctrlCEvent() {
        System.exit(0);
    }

    static void ctrlZEvent() {

    }

    static void processUp() {
        prevCommandIterator(Keys.UP);
        setCursorPos(getCommand().length());
    }

    static void processDown() {
        prevCommandIterator(Keys.DOWN);
        setCursorPos(getCommand().length());
    }

    static void processLeft() {
        if (getCursorPos() > 0) {
            JTerm.out.print("\b");
            decreaseCursorPos();
        }
    }

    static void processRight() {
        if (getCursorPos() < getCommand().length()) {
            Util.clearLine(getCommand(), true);
            JTerm.out.printWithPrompt(getCommand());
            increaseCursorPos();
            moveToCursorPos();
        }
    }

    /**
     * Iterates through the prevCommands list. Emulates Unix terminal behaviour when using
     * vertical arrow keys.
     *
     * @param ak Arrow key to process
     */
    private static void prevCommandIterator(Keys ak) {
        // Saves currently typed command before moving through the list of previously typed commands
        if (lastArrowPress == Keys.NONE)
            currCommand = getCommand();
        lastArrowPress = ak;
        Util.clearLine(getCommand(), true);
        commandListPosition += ak == Keys.UP ? -1 : 1;
        commandListPosition = Math.max(commandListPosition, 0);
        if (commandListPosition >= getPrevCommands().size()) {
            commandListPosition = Math.min(commandListPosition, getPrevCommands().size());
            JTerm.out.printWithPrompt(currCommand);
            setCommand(currCommand);
        } else {
            JTerm.out.printWithPrompt(getPrevCommands().get(commandListPosition));
            setCommand(getPrevCommands().get(commandListPosition));
        }
    }

    static void tabEvent() {
        lastArrowPress = Keys.NONE;
        fileAutocomplete();
        setResetVars(false);
    }

    static void newLineEvent() {
        lastArrowPress = Keys.NONE;
        boolean empty = Util.containsOnlySpaces(getCommand());

        String command = getCommand();
        ArrayList<String> prevCommands = getPrevCommands();

        if (!empty)
            prevCommands.add(command);

        setCommandListPosition(prevCommands.size());
        setCurrCommand("");
        setCursorPos(0);
        setResetVars(true);
        parse();
        setCommand("");
        JTerm.out.printWithPrompt("");
    }

    static void charEvent() {
        lastArrowPress = Keys.NONE;
        String command = getCommand();
        int cursorPos = getCursorPos();

        if (getCursorPos() == getCommand().length()) {
            if (JTerm.isHeadless()) JTerm.out.print(lastChar);
            setCommand(getCommand() + lastChar);
        } else {
            Util.clearLine(getCommand(), true);
            setCommand(new StringBuilder(command).insert(cursorPos, lastChar).toString());
            JTerm.out.printWithPrompt(getCommand());
        }

        increaseCursorPos();
        moveToCursorPos();
        setResetVars(true);
    }

    static void backspaceEvent() {
        lastArrowPress = Keys.NONE;
        if (getCommand().length() > 0 && getCursorPos() > 0) {
            int charToDelete = getCursorPos() - 1;
            String command = getCommand();
            if (JTerm.isHeadless()) Util.clearLine(getCommand(), false);
            setCommand(new StringBuilder(command).deleteCharAt(charToDelete).toString());
            if (JTerm.isHeadless()) JTerm.out.print(getCommand());
            decreaseCursorPos();
            moveToCursorPos();
            setResetVars(true);
        }
    }

    /**
     * Sends command to terminal class for parsing, source is the newlineEvent in the key processor
     */
    protected static void parse() {
        String[] commands = command.split("&&");
        for (String command : commands) {
            command = Util.removeSpaces(command);
            JTerm.executeCommand(command);
        }
    }

    /**
     * Moves the cursor from the end of the command to where it should be (if the user is using arrow keys)
     * Usually only used after modifying 'command'
     */
    private static void moveToCursorPos() {
        for (int i = command.length(); i > cursorPos; i--)
            JTerm.out.print("\b");
    }

    /**
     * Autocompletes desired file name similar to how terminals do it.
     */
    private static void fileAutocomplete() {

        if (FileAutocomplete.getFiles() == null) {
            FileAutocomplete.init(disassembleCommand(command), blockClear, lockTab);
            resetVars = false;
        } else
            FileAutocomplete.fileAutocomplete();

        command = FileAutocomplete.getCommand();

        if (FileAutocomplete.isResetVars() || resetVars)
            FileAutocomplete.resetVars();

        // Get variables and set cursor position
        blockClear = FileAutocomplete.isBlockClear();
        lockTab = FileAutocomplete.isLockTab();
        setCursorPos(command.length());
    }

    /**
     * Splits a command into 3 parts for the autocomplete function to operate properly.
     * <p>
     * Elements 0 and 2 are the non-relevant part of the command to the autocomplete function
     * and are used when stitching the autocompleted command back together.
     * <p>
     * Element 1 is the portion of the command that needs completing, and the one on which
     * the autocomplete class will operate on.
     *
     * @param command Command to split
     * @return Returns disassembled string, with non relevant info in elements 0 and 2, and the string to autocomplete
     * in element 1
     */
    private static String[] disassembleCommand(String command) {

        if (!command.contains("&&"))
            return new String[]{"", command, ""};

        LinkedList<Integer> ampPos = new LinkedList<>();
        for (int i = 0; i < command.length() - 1; i++) {
            if (command.substring(i, i + 2).equals("&&")) {
                ampPos.add(i);
                if (cursorPos - i < 2 && cursorPos - i > 0)
                    return new String[]{"", command, ""};
            }
        }

        String[] splitCommand = new String[3];

        if (ampPos.size() > 1) {
            // Deals with commands that have more than one &&
            for (int i = 0; i < ampPos.size(); i++) {
                if (ampPos.get(i) > cursorPos) {
                    splitCommand[0] = command.substring(0, ampPos.get(i - 1) + 2) + " ";
                    splitCommand[1] = command.substring(ampPos.get(i - 1) + 2, cursorPos);
                    splitCommand[2] = " " + command.substring(cursorPos, command.length());
                } else if (i + 1 == ampPos.size()) {
                    splitCommand[0] = command.substring(0, ampPos.get(i) + 2) + " ";
                    splitCommand[1] = command.substring(ampPos.get(i) + 2, cursorPos);
                    splitCommand[2] = " " + command.substring(cursorPos, command.length());
                }
            }
        } else {
            // Deals with commands that have exactly one &&
            if (cursorPos > ampPos.get(0)) {
                splitCommand[0] = command.substring(0, ampPos.get(0) + 2) + " ";
                splitCommand[1] = command.substring(ampPos.get(0) + 2, cursorPos);
                splitCommand[2] = command.substring(cursorPos, command.length());
            } else if (cursorPos < ampPos.get(0)) {
                splitCommand[0] = "";
                splitCommand[1] = command.substring(0, cursorPos);
                splitCommand[2] = command.substring(cursorPos, command.length());
            } else {
                String[] split = command.split("&&");
                splitCommand[0] = split[0];
                splitCommand[1] = "";
                splitCommand[2] = "&&" + split[1];
            }
        }

        // Remove spaces so that autocomplete can work properly
        splitCommand[1] = Util.removeSpaces(splitCommand[1]);

        return splitCommand;
    }

    private static ArrayList<String> getPrevCommands() {
        return prevCommands;
    }

    public static String getCommand() {
        return command;
    }

    public static void setCommand(String comm) {
        command = comm;
    }

    private static void setResetVars(boolean resetVa) {
        resetVars = resetVa;
    }

    private static void increaseCursorPos() {
        cursorPos++;
    }

    private static void decreaseCursorPos() {
        cursorPos--;
    }

    private static int getCursorPos() {
        return cursorPos;
    }

    private static void setCursorPos(int cursorPosition) {
        cursorPos = cursorPosition;
    }
}
