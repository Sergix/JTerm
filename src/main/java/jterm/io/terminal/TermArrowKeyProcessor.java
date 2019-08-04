package jterm.io.terminal;

import jterm.JTerm;
import jterm.io.handlers.ArrowKeyHandler;
import jterm.io.handlers.InputHandler;
import jterm.io.input.Keys;
import jterm.io.output.TextColor;

/**
 * Processes arrow keys for JTerm headless terminal.
 *
 * @see HeadlessTerminal
 * @see TermInputProcessor
 */
public class TermArrowKeyProcessor extends ArrowKeyHandler {

	// Input processor owning this class
	private TermInputProcessor inputProcessor;

	// Position on prevCommands list (used to iterate through it)
	private int commandListPosition = 0;

	// Stores current TermInputProcessor.command when iterating through prevCommands
	private String currCommand = "";

	TermArrowKeyProcessor(TermInputProcessor inputProcessor) {
		this.inputProcessor = inputProcessor;
		setLArrowBehaviour();
		setRArrowBehaviour();
		setUArrowBehaviour();
		setDArrowBehaviour();
	}

	protected void setCurrCommand(String currCommand) {
		this.currCommand = currCommand;
	}

	protected void setCommandListPosition(int commandListPosition) {
		this.commandListPosition = commandListPosition;
	}

	private void setLArrowBehaviour() {
		lArrEvent = () -> {
			if (inputProcessor.getCursorPos() > 0) {
				JTerm.out.print(TextColor.INPUT, "\b");
				inputProcessor.decreaseCursorPos();
			}
		};
	}

	private void setRArrowBehaviour() {
		rArrEvent = () -> {
			if (inputProcessor.getCursorPos() < inputProcessor.getCommand().length()) {
				InputHandler.clearLine(inputProcessor.getCommand(), true);
				JTerm.out.print(TextColor.PROMPT, JTerm.PROMPT);
				JTerm.out.print(TextColor.INPUT, inputProcessor.getCommand());
				inputProcessor.increaseCursorPos();
				inputProcessor.moveToCursorPos();
			}
		};
	}

	private void setUArrowBehaviour() {
		uArrEvent = () -> {
			prevCommandIterator(Keys.UP);
			inputProcessor.setCursorPos(inputProcessor.getCommand().length());
		};
	}

	private void setDArrowBehaviour() {
		dArrEvent = () -> {
			prevCommandIterator(Keys.DOWN);
			inputProcessor.setCursorPos(inputProcessor.getCommand().length());
		};
	}

	/**
	 * Iterates through the prevCommands list. Emulates Unix terminal behaviour when using
	 * vertical arrow keys in the terminal.
	 *
	 * @param ak Arrow key to process
	 */
	private void prevCommandIterator(Keys ak) {
		if (inputProcessor.commandHistory.size() == 0)
			return;

		int cmdHistorySize = inputProcessor.commandHistory.size() - 1;

		if (commandListPosition == inputProcessor.commandHistory.size() && lastArrowPress == Keys.NONE)
			currCommand = inputProcessor.getCommand();

		if (ak == Keys.UP && commandListPosition > 0) {
			// Move through the list towards first typed command

			lastArrowPress = ak;
			InputHandler.clearLine(inputProcessor.getCommand(), true);

			if (commandListPosition > inputProcessor.commandHistory.size())
				commandListPosition = inputProcessor.commandHistory.size();

			JTerm.out.print(TextColor.PROMPT, JTerm.PROMPT);
			JTerm.out.print(TextColor.INPUT, inputProcessor.commandHistory.get(--commandListPosition));
			inputProcessor.setCommand(inputProcessor.commandHistory.get(commandListPosition));

		} else if (ak == Keys.DOWN) {
			lastArrowPress = ak;

			if (commandListPosition < cmdHistorySize) {
				// Move through list towards last typed element
				InputHandler.clearLine(inputProcessor.getCommand(), true);

				JTerm.out.print(TextColor.PROMPT, JTerm.PROMPT);
				JTerm.out.print(TextColor.INPUT, inputProcessor.commandHistory.get(++commandListPosition));
				inputProcessor.setCommand(inputProcessor.commandHistory.get(commandListPosition));
			} else if (!inputProcessor.getCommand().equals(currCommand)) {
				// Print command that was stored before iteration through list began
				InputHandler.clearLine(inputProcessor.getCommand(), true);
				commandListPosition++;

				JTerm.out.print(TextColor.PROMPT, JTerm.PROMPT);
				JTerm.out.print(TextColor.INPUT, currCommand);
				inputProcessor.setCommand(currCommand);
			}
		}
	}
}
