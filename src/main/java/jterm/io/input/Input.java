package jterm.io.input;

// Copyright 2015 Christian d'Heureuse, Inventec Informatik AG, Zurich, Switzerland
// www.source-code.biz, www.inventec.ch/chdh
//
// This module is multi-licensed and may be used under the terms of any of the following licenses:
//
//  LGPL, GNU Lesser General Public License, V2.1 or later, http://www.gnu.org/licenses/lgpl.html
//  EPL, Eclipse Public License, V1.0 or later, http://www.eclipse.org/legal
//
// Please contact the author if you need another license.
// This module is provided "as is", without warranties of any kind.
//
// Home page: http://www.source-code.biz/snippets/java/RawConsoleInput

import jterm.JTerm;

import java.io.IOException;

/**
 * A JNA based driver for reading single characters from the console.
 * <p>
 * <p>This class is used for console mode programs.
 * It supports non-blocking reads of single key strokes without echo.
 */

public class Input {
	protected static final int invalidKey = 0xFFFE;
	protected static final String invalidKeyStr = String.valueOf((char) invalidKey);

	protected static boolean initDone;
	protected static boolean stdinIsConsole;
	protected static boolean consoleModeAltered;

	private UnixInput unixInput = new UnixInput();
	private WinInput winInput = new WinInput();

	/**
	 * Reads a character from the console without echo.
	 *
	 * @param wait <code>true</code> to wait until an input character is available,
	 *             <code>false</code> to return immediately if no character is available.
	 * @return -2 if <code>wait</code> is <code>false</code> and no character is available.
	 * -1 on EOF.
	 * Otherwise an Unicode character code within the range 0 to 0xFFFF.
	 */
	public int read(boolean wait) {
		if (JTerm.IS_WIN) {
			try {
				return winInput.readWindows(wait);
			} catch (IOException e) {
				System.err.println("Error reading input using Windows reader");
			}
		} else if (JTerm.IS_UNIX) {
			try {
				return unixInput.readUnix(wait);
			} catch (Exception e) {
				System.err.println("Error reading input using Unix reader");
			}
		}

		return -1;
	}

	/**
	 * Resets console mode to normal line mode with echo.
	 * <p>
	 * <p>On Windows this method re-enables Ctrl-C processing.
	 * <p>
	 * <p>On Unix this method switches the console back to echo mode.
	 * read() leaves the console in non-echo mode.
	 */
	private static void resetConsoleMode() throws IOException {
		if (JTerm.IS_WIN) {
			WinInput.resetConsoleModeWindows();
		} else if (JTerm.IS_UNIX) {
			try {
				UnixInput.resetConsoleModeUnix();
			} catch (Exception e) {
				System.err.println("Error resetting console mode");
			}
		}
	}

	/**
	 * Handles registering of shutdown hook to return console to normal line mode.
	 */
	protected static void registerShutdownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread(Input::shutdownHook));
	}

	private static void shutdownHook() {
		try {
			resetConsoleMode();
		} catch (Exception ignored) {
		}
	}
}