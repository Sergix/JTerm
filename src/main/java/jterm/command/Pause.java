package jterm.command;

import java.util.ArrayList;
import java.io.IOException;

import jterm.JTerm;

public class Pause {
    /*
    * Pause() void
    *
    * Pauses the interpreter until the user
    * hits the "Enter" key.
    *
    * ArrayList<String> options - command options
    *
    * message
    * 	Pause message to be printed
    */
    public Pause(ArrayList<String> options) {
        if (options.size() == 0) {
            System.out.print("Press enter to continue...");
        } else {
            System.out.print(Exec.getRest(options, 0));
        }

        try {
            JTerm.userInput.read();
            JTerm.userInput.skip(1);
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}