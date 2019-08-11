package jterm.io.output;

import jterm.JTerm;
import jterm.command.CommandException;

import java.io.IOException;

/**
 * Custom {@link Printer} implementation for the headless JTerm mode.
 */
public class HeadlessPrinter implements Printer {
	private static final String ANSI_CLS = "\u001b[2J";
	private static final String ANSI_HOME = "\u001b[H";

	@Override
	public void print(final TextColor color, final String str) {
		System.out.print(color.getANSIColor() + str);
	}

	@Override
	public void print(final TextColor color, final char c) {
		System.out.print(color.getANSIColor() + c);
	}

	@Override
	public void println() {
		System.out.println();
	}

	@Override
	public void println(final TextColor color, final String str) {
		System.out.println(color.getANSIColor() + str);
	}

	@Override
	public void println(final TextColor color, final char c) {
		System.out.println(color.getANSIColor() + c);
	}

	@Override
	public Printer printf(final TextColor color, final String format, final Object... args) {
		System.out.println(color.getANSIColor() + String.format(format, args));
		return this;
	}

	public void printPrompt() {
		System.out.print(TextColor.PATH.getANSIColor() + JTerm.currentDirectory);
		System.out.print(TextColor.PROMPT.getANSIColor() + JTerm.PROMPT);
	}

	public void printWithPrompt(final TextColor color, final String s) {
		printPrompt();
		System.out.print(color.getANSIColor() + s);
	}

	@Override
	public void clearLine(final String line, final int cursorPosition, final boolean clearPrompt) {
		final int charsToClear = line.length() + (clearPrompt ? JTerm.PROMPT.length() + JTerm.currentDirectory.length() : 0);
		for (int i = 0; i < charsToClear; i++)
			System.out.print('\b');
		for (int i = 0; i < charsToClear; i++)
			System.out.print(' ');
		for (int i = 0; i < charsToClear; i++)
			System.out.print('\b');
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
