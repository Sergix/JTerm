package jterm.io.terminal;

import jterm.JTerm;
import jterm.io.handlers.KeyHandler;
import jterm.io.input.Keys;
import jterm.io.output.TextColor;
import jterm.util.FileAutocomplete;

/**
 * Processes key presses (except arrow keys) for JTerm headless terminal.
 *
 * @see HeadlessTerminal
 */
public class TermKeyProcessor extends KeyHandler {

	private TermInputProcessor inputProcessor;

	private boolean resetVars;

	TermKeyProcessor(final TermInputProcessor inputProcessor) {
		this.inputProcessor = inputProcessor;
		setUpTabEvents();
		setUpNWLNEvent();
		setUpCharEvents();
		setUpBackspaceEvent();
		setUpCtrlCEvent();
		setupCtrlZEvent();
	}

	private void setUpTabEvents() {
		tabEvent = () -> {
			if (resetVars)
				FileAutocomplete.resetVars();

			if (FileAutocomplete.getFiles() == null) {
				FileAutocomplete.init(TermInputProcessor.disassembleCommand(inputProcessor.getCommand(), inputProcessor.getCursorPos()),
									  false, false);
				resetVars = false;
			} else
				FileAutocomplete.fileAutocomplete();

			inputProcessor.setCommand(FileAutocomplete.getCommand());

			if (FileAutocomplete.isResetVars())
				FileAutocomplete.resetVars();

			// Get variables and set cursor position
			inputProcessor.setCursorPos(FileAutocomplete.getCursorPos());
			inputProcessor.moveToCursorPos();
		};
	}

	private void setUpNWLNEvent() {
		newLineEvent = () -> {
			final String command = inputProcessor.getCommand();
			final boolean empty = "".equals(command.trim());

			if (!empty)
				inputProcessor.commandHistory.add(command);

			inputProcessor.getArrowKeyProcessor().setCommandListPosition(inputProcessor.commandHistory.size());
			inputProcessor.getArrowKeyProcessor().setCurrCommand("");
			inputProcessor.setCursorPos(0);
			inputProcessor.parse();

			inputProcessor.setCommand("");
			resetVars = true;
		};

		Keys.NWLN.setEvent(newLineEvent);
	}

	private void setUpCharEvents() {
		charEvent = (char input) -> {
			final String command = inputProcessor.getCommand();
			int cursorPos = inputProcessor.getCursorPos();

			if (JTerm.isHeadless())
				JTerm.out.clearLine(command, cursorPos, true);

			if (inputProcessor.getCursorPos() == command.length())
				inputProcessor.setCommand(command + input);
			else
				inputProcessor.setCommand(new StringBuilder(command).insert(cursorPos, input).toString());

			if (JTerm.isHeadless())
				JTerm.out.printWithPrompt(TextColor.INPUT, inputProcessor.getCommand());

			inputProcessor.increaseCursorPos();
			inputProcessor.moveToCursorPos();
			resetVars = true;
		};

		Keys.CHAR.setCharEvent(charEvent);
	}

	private void setUpBackspaceEvent() {
		backspaceEvent = () -> {
			if (inputProcessor.getCommand().length() > 0 && inputProcessor.getCursorPos() > 0) {
				int charToDelete = inputProcessor.getCursorPos() - 1;
				final String command = inputProcessor.getCommand();

				if (JTerm.isHeadless())
					JTerm.out.clearLine(command, inputProcessor.getCursorPos(), true);

				inputProcessor.setCommand(new StringBuilder(command).deleteCharAt(charToDelete).toString());

				if (JTerm.isHeadless())
					JTerm.out.printWithPrompt(TextColor.INPUT, inputProcessor.getCommand());

				inputProcessor.decreaseCursorPos();
				inputProcessor.moveToCursorPos();
				resetVars = true;
			}
		};

		Keys.BACKSPACE.setEvent(backspaceEvent);
	}

	private void setUpCtrlCEvent() {
		ctrlCEvent = () -> {
			System.exit(130);
		};

		Keys.CTRL_C.setEvent(ctrlCEvent);
	}

	private void setupCtrlZEvent() {
		ctrlZEvent = () -> {
			System.exit(131);
		};

		Keys.CTRL_Z.setEvent(ctrlZEvent);
	}
}
