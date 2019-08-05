package jterm.io.handlers;


import jterm.JTerm;
import jterm.io.input.Input;
import jterm.io.input.Keys;

import java.io.IOException;

/**
 * Abstract class specifying how input should be handled.
 */
public abstract class InputHandler {

	protected ArrowKeyHandler arrowKeyHandler;
	protected KeyHandler keyHandler;

	/**
	 * Determines how long the program will ignore user input (in ms).
	 * Prevents program from going visually insane, by not overloading the system with too much output.
	 */
	public static int minWaitTime = 10;

	/**
	 * Create key handler object with null arrow key handler and key handler.
	 * Only call this constructor if you plan to manually assign the handlers
	 * straight after this constructor returns.
	 */
	public InputHandler() {
		arrowKeyHandler = null;
		keyHandler = null;
	}

	/**
	 * Set KeyHandler and ArrowKeyHandler for JTerm.
	 * <p>
	 * Classes extending KeyHandler and ArrowKeyHandler should passed as parameters,
	 * so they can be later used to process input.
	 *
	 * @param kh  Key handler to use
	 * @param akh Arrow key handler to use
	 */
	public InputHandler(KeyHandler kh, ArrowKeyHandler akh) {
		this.arrowKeyHandler = akh;
		this.keyHandler = kh;
	}

	/**
	 * Code to run when processing input for JTerm headless mode.
	 * Can (and should) make use of keyHandler and/or arrowKeyHandler for input processing.
	 */
	public abstract void process(final Keys key);

	/**
	 * Returns key char value of last key pressed.
	 *
	 * @return Char value of key pressed
	 */
	public static Keys getKey() {
		try {
			return Keys.getKeyByValue(Input.read(true));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return Keys.NONE;
	}

	/**
	 * Clears a line in the console of size line.length().
	 *
	 * @param line        line to be cleared
	 * @param clearPrompt choose to clear prompt along with line (only use true if prompt exists)
	 */
	public static void clearLine(String line, boolean clearPrompt) {
		for (int i = 0; i < line.length() + (clearPrompt ? JTerm.PROMPT.length() : 0); i++)
			System.out.print("\b");

		for (int i = 0; i < line.length() + (clearPrompt ? JTerm.PROMPT.length() : 0); i++)
			System.out.print(" ");

		for (int i = 0; i < line.length() + (clearPrompt ? JTerm.PROMPT.length() : 0); i++)
			System.out.print("\b");
	}
}
