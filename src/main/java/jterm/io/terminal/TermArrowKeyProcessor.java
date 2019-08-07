package jterm.io.terminal;

import jterm.JTerm;
import jterm.io.handlers.ArrowKeyHandler;
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

	TermArrowKeyProcessor(final TermInputProcessor inputProcessor) {
		this.inputProcessor = inputProcessor;
		setLArrowBehaviour();
		setRArrowBehaviour();
		setUArrowBehaviour();
		setDArrowBehaviour();
	}

	protected void setCurrCommand(final String currCommand) {
		this.currCommand = currCommand;
	}

	public void setCommandListPosition(final int commandListPosition) {
		this.commandListPosition = commandListPosition;
	}

	private void setLArrowBehaviour() {
		leftArrEvent = () -> {
			if (inputProcessor.getCursorPos() > 0) {
				JTerm.out.print(TextColor.INPUT, "\b");
				inputProcessor.decreaseCursorPos();
			}
		};

		Keys.LEFT.setEvent(leftArrEvent);
	}

	private void setRArrowBehaviour() {
		rightArrEvent = () -> {
			if (inputProcessor.getCursorPos() < inputProcessor.getCommand().length()) {
				JTerm.out.clearLine(inputProcessor.getCommand(), inputProcessor.getCursorPos(), true);
				JTerm.out.printWithPrompt(TextColor.INPUT, inputProcessor.getCommand());
				inputProcessor.increaseCursorPos();
				inputProcessor.moveToCursorPos();
			}
		};

		Keys.RIGHT.setEvent(rightArrEvent);
	}

	private void setUArrowBehaviour() {
		upArrEvent = () -> {
			prevCommandIterator(Keys.UP);
			inputProcessor.setCursorPos(inputProcessor.getCommand().length());
		};

		Keys.UP.setEvent(upArrEvent);
	}

	private void setDArrowBehaviour() {
		downArrEvent = () -> {
			prevCommandIterator(Keys.DOWN);
			inputProcessor.setCursorPos(inputProcessor.getCommand().length());
		};

		Keys.DOWN.setEvent(downArrEvent);
	}

	/**
	 * Iterates through the prevCommands list. Emulates Unix terminal behaviour when using
	 * vertical arrow keys in the terminal.
	 *
	 * @param ak Arrow key to process
	 */
	private void prevCommandIterator(final Keys ak) {
		if (inputProcessor.commandHistory.size() == 0)
			return;

		int cmdHistorySize = inputProcessor.commandHistory.size() - 1;

		if (commandListPosition == inputProcessor.commandHistory.size() && lastArrowPress == Keys.NONE)
			currCommand = inputProcessor.getCommand();

		if (ak == Keys.UP && commandListPosition > 0) {
			// Move through the list towards first typed command

			lastArrowPress = ak;
			JTerm.out.clearLine(inputProcessor.getCommand(), inputProcessor.getCursorPos(), true);

			if (commandListPosition > inputProcessor.commandHistory.size())
				commandListPosition = inputProcessor.commandHistory.size();

			JTerm.out.print(TextColor.PROMPT, JTerm.PROMPT);
			JTerm.out.print(TextColor.INPUT, inputProcessor.commandHistory.get(--commandListPosition));
			inputProcessor.setCommand(inputProcessor.commandHistory.get(commandListPosition));

		} else if (ak == Keys.DOWN) {
			lastArrowPress = ak;

			if (commandListPosition < cmdHistorySize) {
				// Move through list towards last typed element
				JTerm.out.clearLine(inputProcessor.getCommand(), inputProcessor.getCursorPos(), true);

				JTerm.out.print(TextColor.PROMPT, JTerm.PROMPT);
				JTerm.out.print(TextColor.INPUT, inputProcessor.commandHistory.get(++commandListPosition));
				inputProcessor.setCommand(inputProcessor.commandHistory.get(commandListPosition));
			} else if (!inputProcessor.getCommand().equals(currCommand)) {
				// Print command that was stored before iteration through list began
				JTerm.out.clearLine(inputProcessor.getCommand(), inputProcessor.getCursorPos(), true);
				commandListPosition++;

				JTerm.out.print(TextColor.PROMPT, JTerm.PROMPT);
				JTerm.out.print(TextColor.INPUT, currCommand);
				inputProcessor.setCommand(currCommand);
			}
		}
	}
}
