package jterm.io;

import jterm.JTerm;
import jterm.io.input.Keys;
import jterm.io.input.RawConsoleInput;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

public class IOHeadlessInit {
	public static void init() {
		File f = new File(JTerm.USER_HOME_DIR + "/.jterm_config");
		if (f.exists())
			loadVals();
		else
			setupVals();
	}

	public static void setupVals() {
		try {
			LinkedList<String> vals = new LinkedList<>();
			Keys[] keys = {Keys.BACKSPACE, Keys.UP, Keys.DOWN, Keys.LEFT, Keys.RIGHT, Keys.TAB, Keys.NWLN, Keys.CTRL_C, Keys.CTRL_Z};
			String[] keyNames = {"BACKSPACE", "UP_ARR", "DOWN_ARR", "LEFT_ARR", "RIGHT_ARR", "TAB", "NEWLINE", "SIGTERM", "SIGKILL"};
			for (int k = 0; k < keyNames.length; k++) { // Store all keycodes for the different keys in list, to be written later
				System.out.print("Press the following key: " + keyNames[k]);
				vals.add(keyNames[k] + "=" + assignKeyCode(keys[k]));
				System.out.print("\r                                                       \r");
			}
			PrintWriter pw = new PrintWriter(JTerm.USER_HOME_DIR + "/.jterm_config");
			for (String s : vals)
				pw.println(s);
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private static String assignKeyCode(Keys key) {
		try {
			StringBuilder input = new StringBuilder();
			input.append(RawConsoleInput.read(true));
			int val = RawConsoleInput.read(false);
			/*
			 * Read and append keycodes for the key until no more left (RawConsoleInput returns -2 when there
			 * is no more input to read)
			 */
			while (val != -2) {
				input.append("-");
				input.append(val);
				val = RawConsoleInput.read(false);
			}
			key.setValue(input.toString());
			return input.toString();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private static void loadVals() {
		try {
			List<String> config = Files.readAllLines(Paths.get(JTerm.USER_HOME_DIR + "/.jterm_config"));
			for (String s : config) {
				String[] split = s.split("=");
				switch(split[0]) {
					case "BACKSPACE":
						Keys.BACKSPACE.setValue(split[1]);
						break;
					case "UP_ARR":
						Keys.UP.setValue(split[1]);
						break;
					case "DOWN_ARR":
						Keys.DOWN.setValue(split[1]);
						break;
					case "LEFT_ARR":
						Keys.LEFT.setValue(split[1]);
						break;
					case "RIGHT_ARR":
						Keys.RIGHT.setValue(split[1]);
						break;
					case "TAB":
						Keys.TAB.setValue(split[1]);
						break;
					case "NEWLINE":
						Keys.NWLN.setValue(split[1]);
						break;
					case "SIGTERM":
						Keys.CTRL_C.setValue(split[1]);
						break;
					case "SIGKILL":
						Keys.CTRL_Z.setValue(split[1]);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
