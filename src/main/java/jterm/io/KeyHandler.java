package jterm.io;

import jterm.JTerm;

import java.util.HashMap;

public class KeyHandler extends InputHandler {

    // stores all key integer values and maps them to the values in Keys enum
    static HashMap<Integer, Keys> keymap = new HashMap<>();

    /**
     * Loads all integer values of keys to HashMap.
     * <br></br>
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
     * Returns associated key value from HashMap.
     * <br></br>
     *
     * @param i integer value of key pressed
     */
    static Keys getKey(int i) {
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
    static void processKey(Keys key, ArrowKeys ak, char input) {

        if (ArrowKeyHandler.processArrowKey(key, ak, input))
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
                InputHandler.fileAutocomplete(currText);

                // If one element, autocomplete command (to be implemented)
            else if (commandArr.length == 1 && !JTerm.command.endsWith(" "))
                InputHandler.commandAutocomplete(currText);
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
}
