package jterm.io;

import jterm.JTerm;
import jterm.util.Util;

public class ArrowKeyHandler extends InputHandler{

    private static int[] vals = new int[3]; // for use in detecting arrow presses on Unix
    private static int pos = 0; // for use in detecting arrow presses on Unix
    private static boolean resetArrowCheck = false; // for use in resetting detection of arrow presses on Unix
    // last arrow key that was pressed (if any other key is pressed sets to ArrowKeys.NONE)
    private static ArrowKeys lastArrowPress = ArrowKeys.NONE;

    private static String originalCommand = ""; // stores previous command before it is modified

    /**
     * Checks if last input was arrow key (only on Windows).
     * <br></br>
     *
     * @param i integer value of last key press
     * @return arrow key pressed (or ArrowKeys.NONE if no arrow key was pressed)
     */
    static ArrowKeys arrowKeyCheckWindows(int i) {
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
    static ArrowKeys arrowKeyCheckUnix(int i) {

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
    static boolean processArrowKey(Keys key, ArrowKeys ak, char input) {
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
                KeyHandler.processKey(key, ArrowKeys.MOD, input); // process current input and apply on currently displayed command
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
}
