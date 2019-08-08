package jterm.io.output;

/**
 * Interface meant to replace System.out, so that custom printers can be implemented for GUI and headless mode.
 */
public interface Printer {
	void print(final TextColor color, final String str);

	void print(final TextColor color, final char c);

	void println(final TextColor color);

	void println(final TextColor color, final String str);

	void println(final TextColor color, final char c);

	Printer printf(final TextColor color, final String format, final Object... args);

	void printWithPrompt(final TextColor color, final String str);

    void printPrompt();

	void clearLine(final String line, final int cursorPosition, final boolean clearPrompt);

    void clearAll();
}
