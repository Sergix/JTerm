package jterm.io;

class KeyInterpreter {
	private static final boolean isWindows = System.getProperty("os.name").startsWith("Windows");

	static Keys interpret(int input) {
		switch(input){
			case 3:
				return Keys.CTRL_C;
			case 26:
				return Keys.CTRL_Z;
			case 9:
				return Keys.TAB;
		}
		if (isWindows) {
			switch (input) {
				case 57416:
					return Keys.UP;
				case 57424:
					return Keys.DOWN;
				case 57421:
					return Keys.RIGHT;
				case 57419:
					return Keys.LEFT;
				case 8:
					return Keys.BACKSPACE;
				case 13:
					return Keys.NWLN;
				default:
					return Keys.CHAR;
			}
		} else {
			switch (input) {
				case 127:
					return Keys.BACKSPACE;
				case 10:
					return Keys.NWLN;
				default:
					return Keys.CHAR;
			}
		}
	}

	static Keys interpret(int c1, int c2, int c3) {
		switch (c1 + c2 + c3) {
			case 183:
				return Keys.UP;
			case 184:
				return Keys.DOWN;
			case 185:
				return Keys.RIGHT;
			case 186:
				return Keys.LEFT;
		}
		//Unreachable, probably
		return Keys.CHAR;
	}
}