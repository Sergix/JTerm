package jterm.io.input;

import jterm.io.handlers.events.CharEvent;
import jterm.io.handlers.events.Event;

public enum Keys {
	UP,
	DOWN,
	LEFT,
	RIGHT,
	TAB(9),
	BACKSPACE,
	NWLN,
	CHAR,
	CTRL_C(3),
	CTRL_Z(26),
	NONE(-1);
	int value;
	Event event;
	CharEvent charEvent;

	Keys() {
	}

	Keys(final int value) {
		this.value = value;
	}

	public boolean executeAction() {
		if (event != null) {
			event.process();
			return true;
		}
		return false;
	}

	public boolean executeAction(final char c) {
		if (charEvent != null) {
			charEvent.process(c);
			return true;
		}
		return false;
	}

	public int getValue() {
		return value;
	}

	public void setValue(final int value) {
		this.value = value;
	}

	public void setEvent(final Event event) {
		this.event = event;
	}

	public void setCharEvent(CharEvent charEvent) {
		this.charEvent = charEvent;
	}

	public static void initWindows() {
		UP.value = 57416;
		DOWN.value = 57424;
		RIGHT.value = 57421;
		LEFT.value = 57419;
		BACKSPACE.value = 8;
		NWLN.value = 13;
	}

	public static void initUnix() {
		UP.value = -183;
		DOWN.value = -184;
		RIGHT.value = -185;
		LEFT.value = -186;
		BACKSPACE.value = 127;
		NWLN.value = 10;
	}

	public static void initGUI() {
		UP.value = -38;
		DOWN.value = -40;
		RIGHT.value = -39;
		LEFT.value = -37;
		BACKSPACE.value = 8;
		NWLN.value = 10;
	}

	public static Keys getKeyByValue(final int c) {
		final Keys[] keys = Keys.values();
		for (Keys key : keys) {
			if (c == (key.value))
				return key;
		}
		if ((c >= 48 && c < 128) || c == 32) {
			CHAR.setValue(c);
			return CHAR;
		}
		return NONE;
	}
}
