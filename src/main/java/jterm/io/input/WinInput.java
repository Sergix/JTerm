package jterm.io.input;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

import java.io.IOException;

import static jterm.io.input.Input.*;

public class WinInput {

// The Windows version uses _kbhit() and _getwch() from msvcrt.dll.

	private static Msvcrt msvcrt;
	private static Kernel32 kernel32;
	private static Pointer consoleHandle;
	private static int originalConsoleMode;

	protected static int readWindows(boolean wait) throws IOException {
		initWindows();
		if (!stdinIsConsole) {
			int c = msvcrt.getwchar();
			if (c == 0xFFFF) {
				c = -1;
			}
			return c;
		}
		consoleModeAltered = true;
		setConsoleMode(consoleHandle, originalConsoleMode & ~Kernel32Defs.ENABLE_PROCESSED_INPUT);
		// ENABLE_PROCESSED_INPUT must remain off to prevent Ctrl-C from beeing processed by the system
		// while the program is not within getwch().
		if (!wait && msvcrt.kbhit() == 0) {
			return -2;
		}                                         // no key available
		return getwch();
	}

	private static int getwch() {
		int c = msvcrt.getwch();
		if (c == 0 || c == 0xE0) {                              // Function key or arrow key
			c = msvcrt.getwch();
			if (c >= 0 && c <= 0x18FF) {
				return 0xE000 + c;
			}                              // construct key code in private Unicode range
			return invalidKey;
		}
		if (c < 0 || c > 0xFFFF) {
			return invalidKey;
		}
		return c;
	}                                             // normal key

	private static synchronized void initWindows() throws IOException {
		if (initDone) {
			return;
		}
		msvcrt = Native.loadLibrary("msvcrt", Msvcrt.class);
		kernel32 = Native.loadLibrary("kernel32", Kernel32.class);
		try {
			consoleHandle = getStdInputHandle();
			originalConsoleMode = getConsoleMode(consoleHandle);
			stdinIsConsole = true;
		} catch (IOException e) {
			stdinIsConsole = false;
		}
		if (stdinIsConsole) {
			registerShutdownHook();
		}
		initDone = true;
	}

	private static Pointer getStdInputHandle() throws IOException {
		Pointer handle = kernel32.getStdHandle(Kernel32Defs.STD_INPUT_HANDLE);
		if (Pointer.nativeValue(handle) == 0 || Pointer.nativeValue(handle) == Kernel32Defs.INVALID_HANDLE_VALUE) {
			throw new IOException("GetStdHandle(STD_INPUT_HANDLE) failed.");
		}
		return handle;
	}

	private static int getConsoleMode(Pointer handle) throws IOException {
		IntByReference mode = new IntByReference();
		int rc = kernel32.getConsoleMode(handle, mode);
		if (rc == 0) {
			throw new IOException("GetConsoleMode() failed.");
		}
		return mode.getValue();
	}

	private static void setConsoleMode(Pointer handle, int mode) throws IOException {
		int rc = kernel32.setConsoleMode(handle, mode);
		if (rc == 0) {
			throw new IOException("SetConsoleMode() failed.");
		}
	}

	protected static void resetConsoleModeWindows() throws IOException {
		if (!initDone || !stdinIsConsole || !consoleModeAltered) {
			return;
		}
		setConsoleMode(consoleHandle, originalConsoleMode);
		consoleModeAltered = false;
	}

	private interface Msvcrt extends Library {
		int kbhit();

		int getwch();

		int getwchar();
	}

	private static class Kernel32Defs {
		private static final int STD_INPUT_HANDLE = -10;
		private static final long INVALID_HANDLE_VALUE = (Pointer.SIZE == 8) ? -1 : 0xFFFFFFFFL;
		private static final int ENABLE_PROCESSED_INPUT = 0x0001;
	}

	private interface Kernel32 extends Library {
		int getConsoleMode(Pointer hConsoleHandle, IntByReference lpMode);

		int setConsoleMode(Pointer hConsoleHandle, int dwMode);

		Pointer getStdHandle(int nStdHandle);
	}

}
