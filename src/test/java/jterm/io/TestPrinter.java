package jterm.io;

import jterm.JTerm;
import jterm.io.output.Printer;
import jterm.io.output.TextColor;

public class TestPrinter implements Printer {

	private StringBuilder stringBuilder = new StringBuilder();

	@Override
	public void print(final TextColor color, final String str) {
		stringBuilder.append(str);
	}

	@Override
	public void print(final TextColor color, final char c) {
		stringBuilder.append(c);
	}

	@Override
	public void println(final TextColor color) {
		stringBuilder.append('\n');
	}

	@Override
	public void println(final TextColor color, final String str) {
		stringBuilder.append(str).append('\n');
	}

	@Override
	public void println(final TextColor color, final char c) {
		stringBuilder.append(c).append('\n');
	}

	@Override
	public Printer printf(final TextColor color, final String format, final Object... args) {
		return null;
	}

	@Override
	public void printWithPrompt(final TextColor color, final String str) {
		printPrompt();
		stringBuilder.append(str);
	}

	@Override
	public void printPrompt() {
		stringBuilder.append(JTerm.PROMPT);
	}

	@Override
	public void clearLine(final String line, final int cursorPosition, final boolean clearPrompt) {
		final int lIdx = stringBuilder.lastIndexOf("\n");
		stringBuilder = new StringBuilder(stringBuilder.substring(0, lIdx));
	}

	@Override
	public void clearAll() {
		stringBuilder = new StringBuilder();
	}

	@Override
	public String toString() {
		return stringBuilder.toString();
	}
}
