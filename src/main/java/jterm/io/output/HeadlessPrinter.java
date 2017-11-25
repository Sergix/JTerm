package jterm.io.output;

import jterm.JTerm;
import jterm.command.CommandException;

import java.io.IOException;

public class HeadlessPrinter implements Printer {
    private static final String ANSI_CLS = "\u001b[2J";
    private static final String ANSI_HOME = "\u001b[H";

    private static final String DEFAULT = (char) 27 + "[0m";

    @Override
    public void print(TextColor color, String x) {
        System.out.print(TextColor.getANSIColor() + x);
    }

    @Override
    public void print(TextColor color, char x) {
        System.out.print(TextColor.getANSIColor() + x);
    }

    @Override
    public void println(TextColor color) {
        System.out.println(TextColor.getANSIColor());
    }

    @Override
    public void println(TextColor color, String x) {
        System.out.println(TextColor.getANSIColor() + x);
    }

    @Override
    public void println(TextColor color, char x) {
        System.out.println(TextColor.getANSIColor() + x);
    }

    @Override
    public Printer printf(TextColor color, String format, Object... args) {
        System.out.println(TextColor.getANSIColor() + String.format(format, args));
        return this;
    }

    public void printPrompt() {
        System.out.print(TextColor.getANSIColor() + JTerm.currentDirectory);
        System.out.print(TextColor.getANSIColor() + JTerm.PROMPT);
    }

    public void printWithPrompt(TextColor color, String s) {
        printPrompt();
        System.out.print(TextColor.getANSIColor() + s);
    }

    @Override
    public void clearLine(String line, int cursorPosition, boolean clearPrompt) {
        int promptLength = JTerm.PROMPT.length() + JTerm.currentDirectory.length();

        for (int i = 0; i < cursorPosition + (clearPrompt ? promptLength : 0); i++)
            JTerm.out.print(TextColor.INFO, '\b');
        for (int i = 0; i < line.length() + (clearPrompt ? promptLength : 0); i++)
            JTerm.out.print(TextColor.INFO, ' ');
        for (int i = 0; i < line.length() + (clearPrompt ? promptLength : 0); i++)
            JTerm.out.print(TextColor.INFO, '\b');
    }

    @Override
    public void clearAll() {
        if (JTerm.IS_UNIX) { // escape sequences to clear the screen
            System.out.print(ANSI_CLS + ANSI_HOME);
            System.out.flush();
        } else if (JTerm.IS_WIN) { // Invoke the command line interpreter's own 'clear' command for Windows OS
            try {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } catch (IOException | InterruptedException e) {
                throw new CommandException("Can't clear screen...", e);
            }
        }
    }
}
