package jterm.io.handlers;

import jterm.JTerm;
import jterm.io.handlers.events.CharEvent;
import jterm.io.handlers.events.Event;
import jterm.io.input.Keys;

import java.util.HashMap;

/**
 * Abstract class specifying methods needed to process key events properly.
 */
public abstract class KeyHandler {

	// Stores all key integer values and maps them to the values in Keys enum
	private static HashMap<Integer, Keys> keymap = new HashMap<>();

	private long lastPress = System.currentTimeMillis();

	// Events to implemented by each class that inherits KeyHandler
	public Event tabEvent;
	public Event newLineEvent;
	public CharEvent charEvent;
	public Event backspaceEvent;
	public Event ctrlCEvent;
	public Event ctrlZEvent;

	// Returns a map pairing key values stored as ints to values in Keys enum
	public static HashMap<Integer, Keys> getKeymap() {
		return keymap;
	}

	/**
	 * Processes all input by relegating it to the appropriate lambda expression
	 * and triggering events so that other Modules can react appropriately.
	 *
	 * @param key Key pressed
	 */
	public void process(final Keys key) {
		if (System.currentTimeMillis() - lastPress < InputHandler.minWaitTime)
			return;
		lastPress = System.currentTimeMillis();

		if (key == Keys.BACKSPACE)
			backspaceEvent.process();
		else if (key == Keys.TAB)
			tabEvent.process();
		else if (key == Keys.NWLN)
			newLineEvent.process();
		else if (key == Keys.CHAR)
			charEvent.process((char) key.getValue());

		signalCatch(key);
	}

	/**
	 * Catches system signals, such as Ctrl+C.
	 * Useful for running while waiting for some process to finish, so user can cancel if they wish.
	 * For use in above use case, loop while process is not done and pass input.read(false) to this method.
	 *
	 * @param key Key pressed
	 * @return true if process should be cancelled, false if no signals were caught
	 */
	public static Keys signalCatch(final Keys key) {
		if (key == Keys.CTRL_C || key == Keys.CTRL_Z)
			key.executeAction();
		return Keys.NONE;
	}

	/**
	 * Loads all integer values of keys to keymap.
	 */
	public static void initKeysMap() {
		if (JTerm.IS_WIN) {
			keymap.put(8, Keys.BACKSPACE);
			keymap.put(9, Keys.TAB);
			keymap.put(13, Keys.NWLN);
		} else if (JTerm.IS_UNIX) {
			keymap.put(127, Keys.BACKSPACE);
			keymap.put((int) '\t', Keys.TAB);
			keymap.put((int) '\n', Keys.NWLN);
		}
	}

	/**
	 * Returns associated key value from keymap.
	 *
	 * @param i Integer value of key pressed
	 */
	protected static Keys getKey(int i) {
		return keymap.get(i);
	}
}

