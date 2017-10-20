package main.java.jterm.util;

import main.java.jterm.JTerm;

public class Util {
	public static String getRunTime(long millis) {
		long seconds = millis / 1000;
		String time = "";

		if (seconds / 86400 >= 1)
			time += String.valueOf(seconds / 86400) + " days, ";
		if ((seconds / 3600) >= 1)
			time += String.valueOf((seconds / 3600) % 24) + " hours, ";
		if ((seconds / 60) >= 1)
			time += String.valueOf((seconds / 60) % 60) + " minutes, ";

		time += String.valueOf(seconds % 60) + " seconds, ";
		time += String.valueOf(millis % 1000) + " millis";

		return time;
	}

	/**
	 * ClearLine() void
	 * <br></br><br></br>
	 * Clears a line in the console of size line.length().
	 *
	 * @param line line to be cleared
	 * @param clearPrompt choose to clear prompt along with line (only use true if prompt exists)
	 */
	public static void ClearLine(String line, boolean clearPrompt) {

		for (int i = 0; i < line.length() + (clearPrompt ? JTerm.prompt.length() / 3 : 0); i++)
			System.out.print("\b");

		for (int i = 0; i < line.length() + (clearPrompt ? JTerm.prompt.length() / 3 : 0); i++)
			System.out.print(" ");

		for (int i = 0; i < line.length() + (clearPrompt ? JTerm.prompt.length() / 3 : 0); i++)
			System.out.print("\b");

	}
}
