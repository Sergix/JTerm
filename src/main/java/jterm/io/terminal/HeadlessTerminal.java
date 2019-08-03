package jterm.io.terminal;

import jterm.JTerm;
import jterm.io.handlers.InputHandler;
import jterm.io.output.TextColor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Terminal module. Interfaces directly with bash, so it is as if you were running a normal terminal session.
 */
public class HeadlessTerminal {

	// Input handler for this module
	public TermInputProcessor inputProcessor;

	// Hard cap on lines in history file. If more are added after this, the oldest ones are deleted
	private final static int maxLinesInHistory = 10000;

	private boolean exit;

	public HeadlessTerminal() {
		inputProcessor = new TermInputProcessor(this);

		importJTermHistory();
	}

	public void run() {
		exit = false;
		JTerm.PROMPT = JTerm.currentDirectory + " >> ";

		JTerm.out.println(TextColor.INFO, "Entered terminal mode");
		JTerm.out.print(TextColor.PROMPT, JTerm.PROMPT);
		while (!exit)
			inputProcessor.process(InputHandler.getKey());

		writeCommandsToFile();
		JTerm.PROMPT = ">> ";
	}

	public void parse(String rawCommand) {
		final String[] split = rawCommand.split("&&");
		JTerm.out.println(TextColor.INFO);

		for (String command : split) {
			command = command.trim();

			/*
			 * cd command has to be interpreted separately, since once the JVM runs any cd commands do not take effect
			 * inside the program, nor once it exists.
			 */
			if (command.startsWith("cd ")) {
				changeDir(command);
				continue;
			}

			if ("exit".equals(command)) {
				inputProcessor.setCursorPos(0);
				inputProcessor.setCommand("");
				exit = true;
				return;
			}

			final ProcessBuilder pb;
			final Process p;
			try { // Assumes unix, Windows would require a separate implementation...
				pb = new ProcessBuilder("/bin/bash", "-c", command);
				pb.inheritIO(); // Make program and process share IO to allow user to interact with program
				pb.directory(new File(JTerm.currentDirectory)); // Set working directory for command
				p = pb.start();
				p.waitFor();
				p.destroy();
			} catch (IOException | IllegalArgumentException | InterruptedException e) {
				System.err.println("Parsing command \"" + command + "\" failed, enter \"t-help\" for help using module.");
			}

			JTerm.currentDirectory = System.getProperty("user.dir") + "/";
		}

		JTerm.out.print(TextColor.PROMPT, JTerm.PROMPT);
	}

	/**
	 * Reads JTerm history file and stores all the previous commands in the commandHistory list.
	 * <p>
	 * If for some reason this fails, it will try to read the ~/.bash_history file
	 */
	private void importJTermHistory() {
		try {
			List<String> history = Files.readAllLines(Paths.get(JTerm.USER_HOME_DIR + "/.jterm_history"));
			inputProcessor.commandHistory.addAll(history);
			inputProcessor.getArrowKeyProcessor().setCommandListPosition(history.size());
			if (history.size() > 0)
				return;
		} catch (IOException e) {
			System.err.println("Error reading JTerm history file");
			importBashHistory();
			return;
		}

		importBashHistory();
	}

	/**
	 * Reads bash history file and stores all command listed in the commandHistory list.
	 */
	private void importBashHistory() {
		System.out.println("You currently have an empty or non-existent history file, importing bash history...");
		try {
			List<String> bashHistory = Files.readAllLines(Paths.get(JTerm.USER_HOME_DIR + "/.bash_history"));
			inputProcessor.commandHistory.addAll(bashHistory);
			inputProcessor.getArrowKeyProcessor().setCommandListPosition(bashHistory.size());
			writeCommandsToFile();
		} catch (IOException e) {
			System.err.println(JTerm.USER_HOME_DIR + "/.bash_history");
			System.err.println("Error reading bash history file. Aborting import...");
		}
	}

	/**
	 * Writes the commandHistory list to the ~/.jterm_history file. If this file does not exist, it will
	 * be created.
	 */
	private void writeCommandsToFile() {
		File historyFile = new File(JTerm.USER_HOME_DIR + "/.jterm_history");

		try {
			historyFile.createNewFile();
		} catch (IOException e) {
			System.err.println("Error creating new history file in directory: " + JTerm.USER_HOME_DIR);
			return;
		}

		List<String> original;
		try {
			original = Files.readAllLines(Paths.get(historyFile.getAbsolutePath()));
		} catch (IOException e) {
			System.err.println("Error reading lines from original history file");
			return;
		}

		if (historyFile.delete()) {
			historyFile = new File(JTerm.USER_HOME_DIR + "/.jterm_history");

			final PrintWriter pw;
			try {
				pw = new PrintWriter(historyFile);
			} catch (FileNotFoundException e) {
				System.err.println("Error creating print writer");
				return;
			}

			final int startPos = original.size() +
								 inputProcessor.commandHistory.size() > maxLinesInHistory ? inputProcessor.commandHistory.size() : 0;
			for (int i = startPos; i < original.size(); i++)
				pw.println(original.get(i));

			for (String s : inputProcessor.commandHistory)
				pw.println(s);

			pw.close();
		}
	}

	/**
	 * Changes the terminals directory, since the system does not interpret chdir commands.
	 * Attempts to emulate the "cd" command.
	 *
	 * @param command cd command to parse
	 */
	public void changeDir(String command) {
		final String[] chdirSplit = command.split(" ");

		if (chdirSplit.length != 2 || !"cd".equals(chdirSplit[0])) {
			JTerm.out.println(TextColor.PROMPT, "Invalid chdir command passed");
		} else {
			final String dirChange = chdirSplit[1];
			final String currDir = JTerm.currentDirectory;
			final File f;

			// "cd .."
			if ("..".equals(dirChange) && !"/".equals(JTerm.currentDirectory)) {
				final String[] dirSplit = JTerm.currentDirectory.split("/");
				final StringBuilder newPath = new StringBuilder();

				for (int i = 0; i < dirSplit.length - 1; i++)
					newPath.append(dirSplit[i]).append("/");

				System.setProperty("user.dir", newPath.toString());
				JTerm.currentDirectory = newPath.toString();
				JTerm.PROMPT = JTerm.currentDirectory + " >> ";
				return;
			}

			// "cd /home/username/example/"
			if (dirChange.startsWith("/"))
				f = Paths.get(dirChange).toFile();

				// "cd ~/example/"
			else if (dirChange.startsWith("~") && dirChange.length() > 1)
				f = Paths.get(JTerm.USER_HOME_DIR + dirChange.substring(1)).toFile();

				// "cd ~"
			else if ("~".equals(dirChange))
				f = Paths.get(JTerm.USER_HOME_DIR).toFile();

				// "cd example/src/morexamples/"
			else
				f = Paths.get(currDir + dirChange).toFile();

			if (f.exists() && f.isDirectory()) {
				System.setProperty("user.dir", f.getAbsolutePath());
				JTerm.currentDirectory = f.getAbsolutePath() + "/";
				JTerm.PROMPT = JTerm.currentDirectory + " >> ";
			} else {
				JTerm.out.println(TextColor.INFO, "Please enter a valid directory to change to");
			}
		}
	}
}