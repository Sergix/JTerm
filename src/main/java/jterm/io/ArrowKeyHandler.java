package jterm.io;

import jterm.JTerm;
import jterm.util.Util;

/**
 * Processes arrow keys for Terminal module.
 */
public class ArrowKeyHandler {

    private InputHandler inputProcessor;

    // Stores the three values of the codes that Unix systems pass when an arrow key is pressed
    protected static int[] vals = new int[3];

    // Specifies which of the three codes used in processing arrow keys in Unix the program should be expecting
    protected static int pos = 0;

    // Position on prevCommands list (used to iterate through it)
    private int commandListPosition = 0;

    // Stores current TermInputProcessor.command when iterating through prevCommands
    private String currCommand = "";

    // Last arrow key that was pressed (if any other key is pressed sets to ArrowKeys.NONE)
    protected static ArrowKeys lastArrowPress = ArrowKeys.NONE;

    ArrowKeyHandler(InputHandler inputProcessor) {
        this.inputProcessor = inputProcessor;
    }

    protected void setCurrCommand(String currCommand) {
        this.currCommand = currCommand;
    }

    protected void setCommandListPosition(int commandListPosition) {
        this.commandListPosition = commandListPosition;
    }

    /**
     * Checks if last input was arrow key (only on Windows).
     *
     * @param i integer value of last key press
     * @return arrow key pressed (or ArrowKeys.NONE if no arrow key was pressed)
     */
    public static ArrowKeys arrowKeyCheckWindows(int i) {
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
     * <br></br><br></br>
     * <p>
     * When Unix processes arrow keys, they are read as a sequence of 3 numbers, for example 27 91 65
     * which means Process is called once for each of the three numbers. The first number will be processed normally,
     * which can not be prevented, but the other two run 1ms after the previous one, which means those can be filtered out.
     * Even when holding down a key, the interval between each detection is 30ms +-1ms, which means this approach
     * causes no problems at all. If char 27 is ignored, then the program will continue to run normally, at the cost of
     * the escape character in Unix systems.
     *
     * @param i integer value of last key press
     * @return arrow key pressed (or ArrowKeys.NONE if no arrow key was pressed)
     */
    /*

	*/
    public static ArrowKeys arrowKeyCheckUnix(int i) {

        if (vals[2] > 64 && vals[2] < 69) {
            // Reset array and position tracker if key was previously returned
            vals = new int[3];
            pos = 0;
        }

        vals[pos++ % 3] = i;

        if (vals[0] == 27 && vals[1] == 91) {
            switch (vals[2]) {
                case 65:
                    return ArrowKeys.UP;
                case 66:
                    return ArrowKeys.DOWN;
                case 67:
                    return ArrowKeys.RIGHT;
                case 68:
                    return ArrowKeys.LEFT;
                default:
                    return ArrowKeys.NONE;
            }
        } else if ((vals[0] != 27 && vals[0] != 0) || (i != vals[0] && i == 91 && vals[0] != 0)) {
            // Resets vals array if either of first two positions don't align with expected pattern
            vals = new int[3];
            pos = 0;
        }

        return ArrowKeys.NONE;
    }

    /**
     * Process an arrow key press. If arrow key handling is simple enough,
     * can be used by itself to contain all the handling code.
     *
     * @param ak Arrow key to process
     * @return Arrow key that was processed
     */
    public ArrowKeys process(ArrowKeys ak) {

        if (ak != ArrowKeys.NONE) {
            switch (ak) {
                case UP:
                    processUp(); break;
                case DOWN:
                    processDown(); break;
                case LEFT:
                    processLeft(); break;
                case RIGHT:
                    processRight(); break;
                default:
                    return ak; // Should never run
            }
        }
        return ak;
    }

    protected void processUp() {
        prevCommandIterator(ArrowKeys.UP);
        inputProcessor.setCursorPos(inputProcessor.getCommand().length());
        //System.out.write(new byte[]{27, 91, 65}); // sample code for future use
    }

    protected void processDown() {
        prevCommandIterator(ArrowKeys.DOWN);
        inputProcessor.setCursorPos(inputProcessor.getCommand().length());
    }

    protected void processLeft() {
        if (inputProcessor.getCursorPos() > 0) {
            System.out.print("\b");
            inputProcessor.decreaseCursorPos();
        }
    }

    protected void processRight() {
        if (inputProcessor.getCursorPos() < inputProcessor.getCommand().length()) {
            Util.clearLine(inputProcessor.getCommand(), true);
            System.out.print(JTerm.PROMPT + inputProcessor.getCommand());
            inputProcessor.increaseCursorPos();
            inputProcessor.moveToCursorPos();
        }
    }

    /**
     * Iterates through the prevCommands list. Emulates Unix terminal behaviour when using
     * vertical arrow keys.
     *
     * @param ak Arrow key to process
     */
    private void prevCommandIterator(ArrowKeys ak) {
        if (commandListPosition == inputProcessor.getPrevCommands().size() && lastArrowPress == ArrowKeys.NONE)
            // Saves currently typed command before moving through the list of previously typed commands

            currCommand = inputProcessor.getCommand();

        if (ak == ArrowKeys.UP && commandListPosition > 0) {
            // Move through the list towards first typed command

            lastArrowPress = ak;
            Util.clearLine(inputProcessor.getCommand(), true);

            if (commandListPosition > inputProcessor.getPrevCommands().size())
                commandListPosition = inputProcessor.getPrevCommands().size();

            System.out.print(JTerm.PROMPT + inputProcessor.getPrevCommands().get(--commandListPosition));
            inputProcessor.setCommand(inputProcessor.getPrevCommands().get(commandListPosition));

        } else if (ak == ArrowKeys.DOWN && commandListPosition < inputProcessor.getPrevCommands().size() - 1) {
            // Move through list towards last typed element

            lastArrowPress = ak;
            Util.clearLine(inputProcessor.getCommand(), true);

            System.out.print(JTerm.PROMPT + inputProcessor.getPrevCommands().get(++commandListPosition));
            inputProcessor.setCommand(inputProcessor.getPrevCommands().get(commandListPosition));

        } else if (ak == ArrowKeys.DOWN && commandListPosition >= inputProcessor.getPrevCommands().size() - 1) {
            // Print command that was stored before iteration through list began

            lastArrowPress = ak;
            Util.clearLine(inputProcessor.getCommand(), true);
            commandListPosition++;

            System.out.print(JTerm.PROMPT + currCommand);
            inputProcessor.setCommand(currCommand);
        }
    }
}
