package jterm.io.output;

public interface Printer {
    void print(TextColor color, String x);

    void print(TextColor color, char x);

    void println(TextColor color);

    void println(TextColor color, String x);

    void println(TextColor color, char x);

    Printer printf(TextColor color, String format, Object... args);

    void printWithPrompt(TextColor color, String s);

    void printPrompt();

    void clearLine(String line, int cursorPosition, boolean clearPrompt);

    void clearAll();
}
