package jterm.io.terminal;

import jterm.JTerm;
import jterm.io.handlers.InputHandler;
import jterm.io.handlers.KeyHandler;
import jterm.io.output.TextColor;

/**
 * Processes key presses (except arrow keys) for Terminal module.
 *
 * @see HeadlessTerminal
 */
public class TermKeyProcessor extends KeyHandler {

	private TermInputProcessor inputProcessor;

	TermKeyProcessor(TermInputProcessor inputProcessor) {
		this.inputProcessor = inputProcessor;
		setUpTabEvents();
		setUpNWLNEvent();
		setUpCharEvents();
		setUpBackspaceEvent();
	}

	private void setUpTabEvents() {
		tabEvent = () -> {};
	}

	private void setUpNWLNEvent() {
		newLineEvent = () -> {

			String command = inputProcessor.getCommand();

			boolean empty = "".equals(command.trim());

			if (!empty)
				inputProcessor.commandHistory.add(command);

			inputProcessor.getArrowKeyProcessor().setCommandListPosition(inputProcessor.commandHistory.size());
			inputProcessor.getArrowKeyProcessor().setCurrCommand("");
			inputProcessor.setCursorPos(0);
			inputProcessor.parse();

			inputProcessor.setCommand("");
		};
	}

	private void setUpCharEvents() {
		charEvent = (char input) -> {
			String command = inputProcessor.getCommand();
			int cursorPos = inputProcessor.getCursorPos();

			InputHandler.clearLine(command, true);

			if (inputProcessor.getCursorPos() == command.length()) {
				JTerm.out.print(TextColor.PROMPT, JTerm.PROMPT);
				JTerm.out.print(TextColor.INPUT, command + input);
				inputProcessor.setCommand(command + input);
			} else {
				inputProcessor.setCommand(new StringBuilder(command).insert(cursorPos, input).toString());
				JTerm.out.print(TextColor.PROMPT, JTerm.PROMPT);
				JTerm.out.print(TextColor.INPUT, inputProcessor.getCommand());
			}

			inputProcessor.increaseCursorPos();
			inputProcessor.moveToCursorPos();
		};
	}

	private void setUpBackspaceEvent() {
		backspaceEvent = () -> {
			if (inputProcessor.getCommand().length() > 0 && inputProcessor.getCursorPos() > 0) {
				int charToDelete = inputProcessor.getCursorPos() - 1;
				String command = inputProcessor.getCommand();

				InputHandler.clearLine(command, true);

				inputProcessor.setCommand(new StringBuilder(command).deleteCharAt(charToDelete).toString());
				JTerm.out.print(TextColor.PROMPT, JTerm.PROMPT);
				JTerm.out.print(TextColor.INPUT, inputProcessor.getCommand());

				inputProcessor.decreaseCursorPos();
				inputProcessor.moveToCursorPos();
			}
		};
	}
}
