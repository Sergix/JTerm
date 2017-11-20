package jterm.io;

import jterm.JTerm;
import jterm.util.FileAutocomplete;
import jterm.util.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class InputHandler {
    private Input input = new RawConsoleInput();

    // Position on prevCommands list (used to iterate through it)
    private int commandListPosition = 0;

    // Stores current TermInputProcessor.command when iterating through prevCommands
    private String currCommand = "";

    // Last arrow key that was pressed (if any other key is pressed sets to Keys.NONE)
    protected static Keys lastArrowPress = Keys.NONE;

    protected void setCurrCommand(String currCommand) {
        this.currCommand = currCommand;
    }

    protected void setCommandListPosition(int commandListPosition) {
        this.commandListPosition = commandListPosition;
    }

    // Stores all entered commands
    private ArrayList<String> prevCommands = new ArrayList<>();

    private String command = "";

    // Stops autocomplete from reprinting command it completed if tab is pressed at the end of a complete file name
    private boolean lockTab = false;

    // Stops autocomplete from constantly erasing fileNames list when searching sub-directories
    private boolean blockClear = false;

    // For resetting all variables in FileAutocomplete once a key press other than a tab is registered
    private boolean resetVars = false;

    private int cursorPos = 0;

    public InputHandler(Input input) {
        this.input = input;
    }

    public void process() throws IOException {
        int c1 = input.read(true);
        int c2 = input.read(false);
        int c3 = input.read(false);
        Keys keyType;
        if (c2 == -2 && c3 == -2) {
            keyType = KeyInterpreter.interpret(c1);
        } else {
            keyType = KeyInterpreter.interpret(c1, c2, c3);
        }
        switch (keyType) {
            case UP:
                processUp();
                return;
            case DOWN:
                processDown();
                return;
            case LEFT:
                processLeft();
                return;
            case RIGHT:
                processRight();
                return;
        }
        lastArrowPress = Keys.NONE;
        switch (keyType) {
            case BACKSPACE:
                backspaceEvent();
                return;
            case TAB:
                tabEvent();
                return;
            case NWLN:
                newLineEvent();
                return;
            case CHAR:
                charEvent((char) c1);
                return;
            case CTRL_C:
                System.exit(0);
                //handle ctrl c
                return;
            case CTRL_Z:
                //handle ctrl z
        }
    }

    private void processUp() {
        prevCommandIterator(Keys.UP);
        setCursorPos(getCommand().length());
    }

    private void processDown() {
        prevCommandIterator(Keys.DOWN);
        setCursorPos(getCommand().length());
    }

    private void processLeft() {
        if (getCursorPos() > 0) {
            System.out.print("\b");
            decreaseCursorPos();
        }
    }

    private void processRight() {
        if (getCursorPos() < getCommand().length()) {
            Util.clearLine(getCommand(), true);
            System.out.print(JTerm.PROMPT + getCommand());
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
    private void prevCommandIterator(Keys ak) {
        // Saves currently typed command before moving through the list of previously typed commands
        if (lastArrowPress == Keys.NONE)
            currCommand = getCommand();
        lastArrowPress = ak;
        Util.clearLine(getCommand(), true);
        commandListPosition += ak == Keys.UP ? -1 : 1;
        commandListPosition = Math.max(commandListPosition, 0);
        if (commandListPosition >= getPrevCommands().size()) {
            commandListPosition = Math.min(commandListPosition, getPrevCommands().size());
            System.out.print(JTerm.PROMPT + currCommand);
            setCommand(currCommand);
        } else {
            System.out.print(JTerm.PROMPT + getPrevCommands().get(commandListPosition));
            setCommand(getPrevCommands().get(commandListPosition));
        }
    }

    private void tabEvent() {
        fileAutocomplete();
        setResetVars(false);
    }

    private void newLineEvent() {
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
        System.out.print(JTerm.PROMPT);
    }

    private void charEvent(char input) {
        String command = getCommand();
        int cursorPos = getCursorPos();

        if (getCursorPos() == getCommand().length()) {
            System.out.print(input);
            setCommand(getCommand() + input);
        } else {
            Util.clearLine(getCommand(), true);
            setCommand(new StringBuilder(command).insert(cursorPos, input).toString());
            System.out.print(JTerm.PROMPT + getCommand());
        }

        increaseCursorPos();
        moveToCursorPos();
        setResetVars(true);
    }

    private void backspaceEvent() {
        if (getCommand().length() > 0 && getCursorPos() > 0) {
            int charToDelete = getCursorPos() - 1;
            String command = getCommand();

            Util.clearLine(getCommand(), true);

            setCommand(new StringBuilder(command).deleteCharAt(charToDelete).toString());
            System.out.print(JTerm.PROMPT + getCommand());

            decreaseCursorPos();
            moveToCursorPos();
            setResetVars(true);
        }
    }

    /**
     * Sends command to terminal class for parsing, source is the newlineEvent in the key processor
     */
    protected void parse() {
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
    private void moveToCursorPos() {
        for (int i = command.length(); i > cursorPos; i--)
            System.out.print("\b");
    }

    /**
     * Autocompletes desired file name similar to how terminals do it.
     */
    private void fileAutocomplete() {

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
    private String[] disassembleCommand(String command) {

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

    private ArrayList<String> getPrevCommands() {
        return prevCommands;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    private void setResetVars(boolean resetVars) {
        this.resetVars = resetVars;
    }

    private void increaseCursorPos() {
        cursorPos++;
    }

    private void decreaseCursorPos() {
        cursorPos--;
    }

    private int getCursorPos() {
        return cursorPos;
    }

    private void setCursorPos(int cursorPos) {
        this.cursorPos = cursorPos;
    }
}
