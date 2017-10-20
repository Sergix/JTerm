package jterm.command;

import jterm.JTerm;

import java.io.IOException;
import java.util.ArrayList;

public class Clear {
    private static final String ANSI_CLS = "\u001b[2J";
    private static final String ANSI_HOME = "\u001b[H";

    public Clear(ArrayList<String> options) {
        if (options.contains("-h")) {
            System.out.println("Command syntax:\n\tclear [-h]\n\nClears all lines in the terminal display.");
        } else {
            if (JTerm.IS_UNIX) {
                // Use escape sequences to clear the screen for Unix OS
                System.out.print(ANSI_CLS + ANSI_HOME);
                System.out.flush();
            } else if (JTerm.IS_WIN) {
                // Invoke the command line interpreter's own 'clear' command for Windows OS
                try {
                    new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                } catch (IOException | InterruptedException e) {
                    System.out.println(e);
                }
            }
        }
    }
}
