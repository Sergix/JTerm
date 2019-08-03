package jterm.io.input;

import com.sun.jna.LastErrorException;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Structure;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.util.Arrays;
import java.util.List;

import static jterm.io.input.Input.*;

public class UnixInput {

// The Unix version uses tcsetattr() to switch the console to non-canonical mode,
// System.in.available() to check whether data is available and System.in.read()
// to read bytes from the console.
// A CharsetDecoder is used to convert bytes to characters.

	private static final int stdinFd = 0;
	private static Libc libc;
	private static CharsetDecoder charsetDecoder;
	private static Termios originalTermios;
	private static Termios rawTermios;
	private static Termios intermediateTermios;

	protected static int readUnix(boolean wait) throws Exception {
		initUnix();
		if (!stdinIsConsole) {                                  // STDIN is not a console
			return readSingleCharFromByteStream(System.in);
		}
		consoleModeAltered = true;
		setTerminalAttrs(stdinFd, rawTermios);                  // switch off canonical mode, echo and signals
		try {
			if (!wait && System.in.available() == 0) {
				return -2;
			}                                      // no input available
			return readSingleCharFromByteStream(System.in);
		} finally {
			setTerminalAttrs(stdinFd, intermediateTermios);
		}
	}   // reset some console attributes

	private static Termios getTerminalAttrs(int fd) throws Exception {
		Termios termios = new Termios();
		try {
			int rc = libc.tcgetattr(fd, termios);
			if (rc != 0) {
				throw new Exception("tcgetattr() failed.");
			}
		} catch (LastErrorException e) {
			throw new IOException("tcgetattr() failed.", e);
		}
		return termios;
	}

	private static void setTerminalAttrs(int fd, Termios termios) throws Exception {
		try {
			int rc = libc.tcsetattr(fd, LibcDefs.TCSANOW, termios);
			if (rc != 0) {
				throw new Exception("tcsetattr() failed.");
			}
		} catch (LastErrorException e) {
			throw new IOException("tcsetattr() failed.", e);
		}
	}

	private static int readSingleCharFromByteStream(InputStream inputStream) throws IOException {
		byte[] inBuf = new byte[4];
		int inLen = 0;
		while (true) {
			if (inLen >= inBuf.length) {                         // input buffer overflow
				return invalidKey;
			}
			int b = inputStream.read();                          // read next byte
			if (b == -1) {                                       // EOF
				return -1;
			}
			inBuf[inLen++] = (byte) b;
			int c = decodeCharFromBytes(inBuf, inLen);
			if (c != -1) {
				return c;
			}
		}
	}

	// (This method is synchronized because the charsetDecoder must only be used by a single thread at once.)
	private static synchronized int decodeCharFromBytes(byte[] inBytes, int inLen) {
		charsetDecoder.reset();
		charsetDecoder.onMalformedInput(CodingErrorAction.REPLACE);
		charsetDecoder.replaceWith(invalidKeyStr);
		ByteBuffer in = ByteBuffer.wrap(inBytes, 0, inLen);
		CharBuffer out = CharBuffer.allocate(1);
		charsetDecoder.decode(in, out, false);
		if (out.position() == 0) {
			return -1;
		}
		return out.get(0);
	}

	private static synchronized void initUnix() throws Exception {
		if (initDone) {
			return;
		}
		libc = Native.loadLibrary("c", Libc.class);
		stdinIsConsole = libc.isatty(stdinFd) == 1;
		charsetDecoder = Charset.defaultCharset().newDecoder();
		if (stdinIsConsole) {
			originalTermios = getTerminalAttrs(stdinFd);
			rawTermios = new Termios(originalTermios);
			rawTermios.c_lflag &= ~(LibcDefs.ICANON | LibcDefs.ECHO | LibcDefs.ECHONL | LibcDefs.ISIG);
			intermediateTermios = new Termios(rawTermios);
			intermediateTermios.c_lflag |= LibcDefs.ICANON;
			// Canonical mode can be switched off between the read() calls, but echo must remain disabled.
			registerShutdownHook();
		}
		initDone = true;
	}

	protected static void resetConsoleModeUnix() throws Exception {
		if (!initDone || !stdinIsConsole || !consoleModeAltered) {
			return;
		}
		setTerminalAttrs(stdinFd, originalTermios);
		consoleModeAltered = false;
	}

	protected static class Termios extends Structure {         // termios.h
		public int c_iflag;
		public int c_oflag;
		public int c_cflag;
		public int c_lflag;
		public byte c_line;
		public byte[] filler = new byte[64];                  // actual length is platform dependent

		@Override
		protected List<String> getFieldOrder() {
			return Arrays.asList("c_iflag", "c_oflag", "c_cflag", "c_lflag", "c_line", "filler");
		}

		Termios() {
		}

		Termios(Termios t) {
			c_iflag = t.c_iflag;
			c_oflag = t.c_oflag;
			c_cflag = t.c_cflag;
			c_lflag = t.c_lflag;
			c_line = t.c_line;
			filler = t.filler.clone();
		}
	}

	private static class LibcDefs {
		// termios.h
		private static final int ISIG = 0000001;
		private static final int ICANON = 0000002;
		private static final int ECHO = 0000010;
		private static final int ECHONL = 0000100;
		private static final int TCSANOW = 0;
	}

	private interface Libc extends Library {
		// termios.h
		int tcgetattr(int fd, Termios termios) throws LastErrorException;

		int tcsetattr(int fd, int opt, Termios termios) throws LastErrorException;

		// unistd.h
		int isatty(int fd);
	}
}
