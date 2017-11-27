package jterm.io.output;

import jterm.JTerm;
import jterm.command.CommandException;

import java.io.IOException;

public class HeadlessPrinter implements Printer {
    private static final String ANSI_CLS = "\u001b[2J";
    private static final String ANSI_HOME = "\u001b[H";

    private static final String RESET = (char) 27 + "[0m";

    @Override
    public void print(TextColor color, String x) {
        System.out.print(color.getANSIColor() + x + RESET + RESET);
    }

    @Override
    public void print(TextColor color, char x) {
        System.out.print(color.getANSIColor() + x + RESET);
    }

    @Override
    public void println() {
        System.out.println();
    }

    @Override
    public void println(TextColor color, String x) {
        System.out.println(color.getANSIColor() + x + RESET);
    }

    @Override
    public void println(TextColor color, char x) {
        System.out.println(color.getANSIColor() + x + RESET);
    }

    @Override
    public Printer printf(TextColor color, String format, Object... args) {
        System.out.println(color.getANSIColor() + String.format(format, args) + RESET);
        return this;
    }

    public void printPrompt() {
        System.out.print(TextColor.PATH.getANSIColor() + JTerm.currentDirectory + RESET);
        System.out.print(TextColor.PATH.getANSIColor() + JTerm.PROMPT + RESET);
    }

    public void printWithPrompt(TextColor color, String s) {
        printPrompt();
        System.out.print(color.getANSIColor() + s + RESET);
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
