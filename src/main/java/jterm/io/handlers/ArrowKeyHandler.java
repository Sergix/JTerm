package jterm.io.handlers;

import jterm.io.handlers.events.Event;
import jterm.io.input.Keys;

/**
 * Abstract class specifying how arrow keys should be handled.
 *
 * @see Event
 */
public abstract class ArrowKeyHandler {

	// Last arrow key that was pressed (if any other key is pressed sets to Keys.NONE)
	protected static Keys lastArrowPress = Keys.NONE;

	private long lastPress = System.currentTimeMillis();

	// Events to be implemented by any class that inherits ArrowKeyHandler
	public Event lArrEvent;
	public Event rArrEvent;
	public Event uArrEvent;
	public Event dArrEvent;

	/**
	 * Checks if last input was arrow key (only on Windows).
	 *
	 * @param i Integer value of last key press
	 * @return Arrow key pressed (or Keys.NONE if no arrow key was pressed)
	 */
	public static Keys arrowKeyCheckWindows(final int i) {
		switch (i) {
			case 57416:
				return Keys.UP;
			case 57424:
				return Keys.DOWN;
			case 57421:
				return Keys.RIGHT;
			case 57419:
				return Keys.LEFT;
			default:
				return Keys.NONE;
		}
	}

	/**
	 * Checks if input was arrow key (only on Unix).
	 * <p>
	 * When Unix processes arrow keys, they are read as a sequence of 3 numbers, for example 27 91 65
	 * which means that the implementation of InputHandler owning the implementation of ArrowKeyHandler
	 * must read 3 values, only blocking for the first. That way, if an arrow key is pressed, all three values are
	 * caught, and if not, no input is lost, since the time for catching in non-blocking mode is ~1ms, and keyboard
	 * presses are only detected every ~30ms interval.
	 *
	 * @param i Integer value of last key press
	 * @return Arrow key pressed (or Keys.NONE if no arrow key was pressed)
	 */
	public static Keys arrowKeyCheckUnix(final int... i) {

		if (i[0] == 27 && i[1] == 91) {
			switch (i[2]) {
				case 65:
					return Keys.UP;
				case 66:
					return Keys.DOWN;
				case 67:
					return Keys.RIGHT;
				case 68:
					return Keys.LEFT;
				default:
					return Keys.NONE;
			}
		}

		return Keys.NONE;
	}

	/**
	 * Process an arrow key press.
	 *
	 * @param ak Arrow key to process
	 */
	public void process(final Keys ak) {
		if (ak != Keys.NONE && System.currentTimeMillis() - lastPress > InputHandler.minWaitTime) {
			lastPress = System.currentTimeMillis();
			switch (ak) {
				case UP:
					uArrEvent.process();
					break;
				case DOWN:
					dArrEvent.process();
					break;
				case LEFT:
					lArrEvent.process();
					break;
				case RIGHT:
					rArrEvent.process();
					break;
			}
		}
	}
}

