package jterm.io.output;

/**
 * Interface meant to replace System.out, so that custom printers can be implemented for GUI and headless mode.
 */
public interface Printer {
    void print(TextColor color, String x);

    void print(TextColor color, char x);

    void println();

    void println(TextColor color, String x);

    void println(TextColor color, char x);

    Printer printf(TextColor color, String format, Object... args);

    void printWithPrompt(TextColor color, String s);

    void printPrompt();

    void clearLine(String line, int cursorPosition, boolean clearPrompt);

    void clearAll();
}
