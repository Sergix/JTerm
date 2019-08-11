package jterm.io.terminal;

import jterm.JTerm;
import jterm.io.handlers.ArrowKeyHandler;
import jterm.io.handlers.InputHandler;
import jterm.io.handlers.KeyHandler;
import jterm.io.input.Input;
import jterm.io.input.Keys;
import jterm.io.output.TextColor;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Input processor for JTerm headless terminal.
 *
 * @see HeadlessTerminal
 * @see TermKeyProcessor
 * @see TermArrowKeyProcessor
 */
public class TermInputProcessor extends InputHandler {

	private HeadlessTerminal headlessTerminal;

	private final Input input = new Input();

	// Stores all entered commands
	public ArrayList<String> commandHistory = new ArrayList<>();

	private String command = "";

	private int cursorPos = 0;

	public TermInputProcessor(final HeadlessTerminal headlessTerminal) {
		super();
		this.headlessTerminal = headlessTerminal;

		keyHandler = new TermKeyProcessor(this);
		arrowKeyHandler = new TermArrowKeyProcessor(this);

		KeyHandler.initKeysMap();
	}

	public TermKeyProcessor getKeyProcessor() {
		return (TermKeyProcessor) keyHandler;
	}

	public TermArrowKeyProcessor getArrowKeyProcessor() {
		return (TermArrowKeyProcessor) arrowKeyHandler;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public void increaseCursorPos() {
		cursorPos++;
	}

	public void decreaseCursorPos() {
		cursorPos--;
	}

	public int getCursorPos() {
		return cursorPos;
	}

	protected void setCursorPos(int cursorPos) {
		this.cursorPos = cursorPos;
	}

	/**
	 * Calls appropriate method for handling input read from the input class.
	 */
	@Override
	public void process(final Keys key) {
		if (JTerm.IS_WIN) {
			arrowKeyHandler.process(ArrowKeyHandler.arrowKeyCheckWindows(key.getValue()));
			keyHandler.process(key);
		} else if (JTerm.IS_UNIX) {
			int c1, c2;
			c1 = input.read(false);
			c2 = input.read(false);

			if (c1 == -2 && c2 == -2)
				keyHandler.process(key);
			else
				arrowKeyHandler.process(ArrowKeyHandler.arrowKeyCheckUnix(key.getValue(), c1, c2));
		}
	}

	/**
	 * Sends command to terminal class for parsing, source is the newlineEvent in the key processor
	 */
	protected void parse() {
		headlessTerminal.parse(command);
	}

	/**
	 * Moves the cursor from the end of the command to where it should be (if the user is using arrow keys)
	 * Usually only used after modifying 'command'
	 */
	public void moveToCursorPos() {
		for (int i = command.length(); i > cursorPos; i--)
			JTerm.out.print(TextColor.INPUT, "\b");
	}

	/**
	 * Splits a command into 3 parts for the autocomplete function to operate properly.
	 * <p>
	 * Elements 0 and 2 are the non-relevant part of the command to the autocomplete function
	 * and are used when stitching the autocompleted command back together.
	 * <p>
	 * Element 1 is the portion of the command that needs completing, and the one on which
	 * the autocomplete class will operate on.
	 *
	 * @param command Command to split
	 * @return Returns disassembled string, with non relevant info in elements 0 and 2, and the string to autocomplete
	 * in element 1
	 */
	public static String[] disassembleCommand(final String command, final Integer cursorPos) {

		if (!command.contains("&&"))
			return new String[]{"", command, ""};

		LinkedList<Integer> ampPos = new LinkedList<>();
		for (int i = 0; i < command.length() - 1; i++) {
			if (command.substring(i, i + 2).equals("&&")) {
				ampPos.add(i);
				if (cursorPos - i == 1)
					return new String[]{"", command, ""};
			}
		}

		String[] splitCommand = new String[3];

		final String rightSideSplit = command.substring(cursorPos);
		if (ampPos.size() > 1) {
			// Deals with commands that have more than one &&
			for (int i = 0; i < ampPos.size(); i++) {
				if (ampPos.get(i) > cursorPos) {
					splitCommand[0] = command.substring(0, ampPos.get(i - 1) + 2) + " ";
					splitCommand[1] = command.substring(ampPos.get(i - 1) + 2, cursorPos);
					splitCommand[2] = " " + rightSideSplit;
				} else if (i + 1 == ampPos.size()) {
					splitCommand[0] = command.substring(0, ampPos.get(i) + 2) + " ";
					splitCommand[1] = command.substring(ampPos.get(i) + 2, cursorPos);
					splitCommand[2] = " " + rightSideSplit;
				}
			}
		} else {
			// Deals with commands that have exactly one &&
			if (cursorPos > ampPos.get(0)) {
				splitCommand[0] = command.substring(0, ampPos.get(0) + 2) + " ";
				splitCommand[1] = command.substring(ampPos.get(0) + 2, cursorPos);
				splitCommand[2] = rightSideSplit;
			} else if (cursorPos < ampPos.get(0)) {
				splitCommand[0] = "";
				splitCommand[1] = command.substring(0, cursorPos);
				splitCommand[2] = rightSideSplit;
			} else {
				String[] split = command.split("&&");
				splitCommand[0] = split[0];
				splitCommand[1] = "";
				splitCommand[2] = "&&" + split[1];
			}
		}

		// Remove spaces so that autocomplete can work properly
		splitCommand[1] = splitCommand[1].trim();

		return splitCommand;
	}
}

