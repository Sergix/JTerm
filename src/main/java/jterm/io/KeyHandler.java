package jterm.io;

import jterm.JTerm;
import jterm.util.FileAutocomplete;
import jterm.util.Util;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Processes key presses (except arrow keys).
 * 
 */
public class KeyHandler {

    private InputHandler inputProcessor;

    // Stores all key integer values and maps them to the values in Keys enum
    private static HashMap<Integer, Keys> keymap = new HashMap<>();

    public static HashMap<Integer, Keys> getKeymap() {
        return keymap;
    }

    KeyHandler(InputHandler inputProcessor) {
        this.inputProcessor = inputProcessor;
    }

    /**
     * Loads all integer values of keys to HashMap.
     */
    public static void initKeysMap() {
        if (JTerm.IS_WIN) {
            keymap.put(8, Keys.BCKSP);
            keymap.put(9, Keys.TAB);
            keymap.put(13, Keys.NWLN);
        } else if (JTerm.IS_UNIX) {
            keymap.put(127, Keys.BCKSP);
            keymap.put((int) '\t', Keys.TAB);
            keymap.put((int) '\n', Keys.NWLN);
        }
    }

    /**
     * Returns associated key value from HashMap.
     *
     * @param i integer value of key pressed
     */
    protected static Keys getKey(int i) {
        return keymap.get(i);
    }

    /**
     * Processes input provided by input class,
     * and operates based on the input it receives,
     * using the character value passed by the process() method.
     *
     * @param input Last character input by user
     */
    public void process(int input) {

        Keys key = getKey(input);

        if (key != Keys.TAB) {
            inputProcessor.setLockTab(false);
            inputProcessor.setBlockClear(false);
            FileAutocomplete.resetVars();
        }

        // Back Space
        if (key == Keys.BCKSP)
            backspaceEvent();

            // Tab
        else if (key == Keys.TAB)
            tabEvent();

            // Enter, or new line
        else if (key == Keys.NWLN)
            newLineEvent();

            // Character input
        else if (input > 31 && input < 127)
            charEvent((char)input);

        signalCatch(input);
    }

    /**
     * Catches system signals, such as Ctrl+C.
     * Useful for running while waiting for some process to finish, so user can cancel if they wish.
     * For use in above use case, loop while process is not done and pass input.read(false) to this method.
     *
     * @param input ASCII key code value of key pressed
     * @return true if process should be cancelled, false if no signals were caught
     */
    public static Command signalCatch(int input) {
        if (input == 3) // Ctrl+C
            return Command.CTRLC;
        if (input == 26) { // Ctrl+Z -> force quit program (not really yet...)
            System.out.println();
            return Command.CTRLZ;
        }
        return Command.NONE;
    }
    
    public void tabEvent() {
        inputProcessor.fileAutocomplete();
        inputProcessor.setResetVars(false);
    }
    
    public void newLineEvent() {
        boolean empty = Util.containsOnlySpaces(inputProcessor.getCommand());

        String command = inputProcessor.getCommand();
        ArrayList<String> prevCommands = inputProcessor.getPrevCommands();

        if (!empty)
            prevCommands.add(command);

        inputProcessor.getArrowKeyProcessor().setCommandListPosition(prevCommands.size());
        inputProcessor.getArrowKeyProcessor().setCurrCommand("");
        inputProcessor.setCursorPos(0);
        inputProcessor.setResetVars(true);
        inputProcessor.parse();
        inputProcessor.setCommand("");
        System.out.print(JTerm.PROMPT);
    }
    
    public void charEvent(char input) {
        String command = inputProcessor.getCommand();
        int cursorPos = inputProcessor.getCursorPos();

        if (inputProcessor.getCursorPos() == inputProcessor.getCommand().length()) {
            System.out.print(input);
            inputProcessor.setCommand(inputProcessor.getCommand() + input);
        } else {
            Util.clearLine(inputProcessor.getCommand(), true);
            inputProcessor.setCommand(new StringBuilder(command).insert(cursorPos, input).toString());
            System.out.print(JTerm.PROMPT + inputProcessor.getCommand());
        }

        inputProcessor.increaseCursorPos();
        inputProcessor.moveToCursorPos();
        inputProcessor.setResetVars(true);
    }

    public void backspaceEvent() {
        if (inputProcessor.getCommand().length() > 0 && inputProcessor.getCursorPos() > 0) {
            int charToDelete = inputProcessor.getCursorPos() - 1;
            String command = inputProcessor.getCommand();

            Util.clearLine(inputProcessor.getCommand(), true);

            inputProcessor.setCommand(new StringBuilder(command).deleteCharAt(charToDelete).toString());
            System.out.print(JTerm.PROMPT + inputProcessor.getCommand());

            inputProcessor.decreaseCursorPos();
            inputProcessor.moveToCursorPos();
            inputProcessor.setResetVars(true);
        }
    }
}
