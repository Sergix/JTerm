package jterm.command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import jterm.JTerm;
import jterm.io.output.TextColor;

/**
 * Represents the {@code top} linux command <br>
 * for now this shows only the currently running process in windows
 * 
 * @author abdul.mohsin
 *
 */
public class Top {

	private static final String SYSTEM32 = "\\system32\\";

	@Command(name = "top", syntax = "top")
	public static void top(List<String> options) {
		try {
			String line;
			Process p = getProcess();
			Reader inputStreamReader = new InputStreamReader(p.getInputStream());
			BufferedReader input = new BufferedReader(inputStreamReader);
			while ((line = input.readLine()) != null) {
				JTerm.out.println(TextColor.ERROR, line); // <-- Parse data
															// here.
			}
			input.close();
		} catch (Exception err) {
			JTerm.out.println(TextColor.ERROR, err.getMessage());
			err.printStackTrace();
		}
	}

	private static Process getProcess() throws IOException {
		String windDir = System.getenv("windir");
		String nativeCommand = windDir + SYSTEM32 + "tasklist.exe";
		Process p = Runtime.getRuntime().exec(nativeCommand);
		return p;
	}
}
