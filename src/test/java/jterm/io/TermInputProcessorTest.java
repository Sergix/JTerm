package jterm.io;

import jterm.JTerm;
import jterm.io.handlers.InputHandler;
import jterm.io.input.Keys;
import jterm.io.output.Printer;
import jterm.io.output.TextColor;
import jterm.io.terminal.HeadlessTerminal;
import jterm.io.terminal.TermArrowKeyProcessor;
import jterm.io.terminal.TermInputProcessor;
import jterm.io.terminal.TermKeyProcessor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class TermInputProcessorTest {
	@BeforeAll
	public static void disableOutput() {
		JTerm.out = new Printer() {
			@Override
			public void print(TextColor color, String x) {
			}

			@Override
			public void print(TextColor color, char x) {
			}

			@Override
			public void println(TextColor color) {
			}

			@Override
			public void println(TextColor color, String x) {
			}

			@Override
			public void println(TextColor color, char x) {
			}

			@Override
			public Printer printf(TextColor color, String format, Object... args) {
				return null;
			}

			@Override
			public void printWithPrompt(TextColor color, String s) {
			}

			@Override
			public void printPrompt() {
			}

			@Override
			public void clearLine(String line, int cursorPosition, boolean clearPrompt) {
			}

			@Override
			public void clearAll() {
			}
		};
		InputHandler.minWaitTime = -1;
	}

	@Test
	public void disassembleCommandTest() {
		JTerm.setOS();

		assertEquals("command", TermInputProcessor.disassembleCommand("command", 0)[1]);

		assertEquals("another", TermInputProcessor.disassembleCommand("command && anothercommand", 18)[1]);

		assertEquals("d", TermInputProcessor.disassembleCommand("a && b && c && d", 16)[1]);

		assertEquals("d", TermInputProcessor.disassembleCommand("a && b && c && d && e", 16)[1]);

		assertEquals("d", TermInputProcessor.disassembleCommand("a && b && c &&d && e", 15)[1]);

		String[] cmd = TermInputProcessor.disassembleCommand("/home/username && /etc/", 14);
		assertEquals("/home/username && /etc/", cmd[0] + cmd[1] + cmd[2]);
		assertEquals("", cmd[0]);
		assertEquals("/home/username", cmd[1]);
		assertEquals(" && /etc/", cmd[2]);

		cmd = TermInputProcessor.disassembleCommand("/home/username && /etc/", 16);
		assertEquals("/home/username && /etc/", cmd[0] + cmd[1] + cmd[2]);
		assertEquals("", cmd[0]);
		assertEquals("/home/username && /etc/", cmd[1]);
		assertEquals("", cmd[2]);

		cmd = TermInputProcessor.disassembleCommand("/home/username && /etc/", 15);
		assertEquals("/home/username && /etc/", cmd[0] + cmd[1] + cmd[2]);
		assertEquals("/home/username ", cmd[0]);
		assertEquals("", cmd[1]);
		assertEquals("&& /etc/", cmd[2]);

		cmd = TermInputProcessor.disassembleCommand("cd / && cd /home", 16);
		assertEquals("cd / && cd /home", cmd[0] + cmd[1] + cmd[2]);
		assertEquals("cd / && ", cmd[0]);
		assertEquals("cd /home", cmd[1]);
		assertEquals("", cmd[2]);
	}

	/*
	 * No testing can be done on file autocomplete since it will fail on different systems.
	 * Changing this test will require modification of all other tests in this class.
	 * Tests that key processing in the HeadlessTerminal module is functional and robust.
	 */
	@Test
	public void keyTest() {
		JTerm.setOS();
		final HeadlessTerminal terminal = new HeadlessTerminal();
		final TermInputProcessor inputProcessor = terminal.inputProcessor;
		final TermKeyProcessor keyProcessor = inputProcessor.getKeyProcessor();

		inputProcessor.commandHistory.clear();

		keyProcessor.process(Keys.getKeyByValue('a'));
		assertEquals("a", inputProcessor.getCommand());

		keyProcessor.process(Keys.getKeyByValue('b'));
		assertEquals("ab", inputProcessor.getCommand());

		keyProcessor.newLineEvent.process(); // emulate newline
		terminal.parse(inputProcessor.getCommand());
		assertEquals("", inputProcessor.getCommand());

		keyProcessor.process(Keys.getKeyByValue('c'));
		keyProcessor.process(Keys.getKeyByValue('d'));
		keyProcessor.backspaceEvent.process(); // emulate backspace
		keyProcessor.process(Keys.getKeyByValue('e'));

		assertEquals("ce", inputProcessor.getCommand());

		keyProcessor.newLineEvent.process(); // emulate newline
		terminal.parse(inputProcessor.getCommand());

		assertEquals(2, inputProcessor.commandHistory.size());
	}

	/*
	 * Tests that arrow key processing in the HeadlessTerminal module is functional and robust.
	 * Current command should be "" in TermInputProcessor.
	 * prevCommands list should hold 2 strings -> "ab" and "ce"
	 */
	@Test
	public void arrowKeyTest() {
		final HeadlessTerminal terminal = new HeadlessTerminal();
		final TermInputProcessor inputProcessor = terminal.inputProcessor;
		final TermArrowKeyProcessor akProcessor = inputProcessor.getArrowKeyProcessor();

		inputProcessor.commandHistory.clear();

		inputProcessor.commandHistory.add("ab");
		inputProcessor.commandHistory.add("ce");
		inputProcessor.getArrowKeyProcessor().setCommandListPosition(2);

		akProcessor.process(Keys.NONE);
		assertEquals("", inputProcessor.getCommand());


		assertEquals(2, inputProcessor.commandHistory.size());

		akProcessor.process(Keys.UP);
		assertEquals("ce", inputProcessor.getCommand());

		akProcessor.process(Keys.UP);
		assertEquals("ab", inputProcessor.getCommand());

		akProcessor.process(Keys.DOWN);
		assertEquals("ce", inputProcessor.getCommand());

		akProcessor.process(Keys.UP);
		assertEquals("ab", inputProcessor.getCommand());

		akProcessor.process(Keys.NONE);
		assertEquals("ab", inputProcessor.getCommand());

		akProcessor.process(Keys.LEFT);
		assertEquals("ab", inputProcessor.getCommand());

		akProcessor.process(Keys.LEFT);
		assertEquals("ab", inputProcessor.getCommand());

		akProcessor.process(Keys.RIGHT);
		assertEquals("ab", inputProcessor.getCommand());

		akProcessor.process(Keys.DOWN);
		assertEquals("ce", inputProcessor.getCommand());

		inputProcessor.getArrowKeyProcessor().leftArrEvent.process();
		inputProcessor.getKeyProcessor().backspaceEvent.process();
		inputProcessor.getKeyProcessor().process(Keys.getKeyByValue('a'));
		assertEquals("ae", inputProcessor.getCommand());

		inputProcessor.getArrowKeyProcessor().leftArrEvent.process();
		inputProcessor.getArrowKeyProcessor().leftArrEvent.process();
		inputProcessor.getKeyProcessor().process(Keys.getKeyByValue('z'));
		assertEquals("zae", inputProcessor.getCommand());
		inputProcessor.getArrowKeyProcessor().rightArrEvent.process();
		inputProcessor.getArrowKeyProcessor().rightArrEvent.process();
		inputProcessor.getKeyProcessor().backspaceEvent.process();
		inputProcessor.getKeyProcessor().backspaceEvent.process();
		assertEquals("z", inputProcessor.getCommand());

		inputProcessor.commandHistory.clear();
	}

	/*
	 * Tests both key handling and arrow key handling for terminal module.
	 */
	@Test
	public void combinedTest() {
		final HeadlessTerminal terminal = new HeadlessTerminal();
		final TermInputProcessor inputProcessor = terminal.inputProcessor;

		inputProcessor.commandHistory.clear();

		// reset variables that might have been modified elsewhere needed for clean test
		inputProcessor.setCommand("");
		inputProcessor.commandHistory.clear();
		inputProcessor.getArrowKeyProcessor().setCommandListPosition(0);

		inputProcessor.getKeyProcessor().process(Keys.getKeyByValue('h'));
		inputProcessor.getKeyProcessor().process(Keys.getKeyByValue('e'));
		inputProcessor.getKeyProcessor().process(Keys.getKeyByValue('p'));
		inputProcessor.getKeyProcessor().process(Keys.getKeyByValue('l'));
		inputProcessor.getKeyProcessor().newLineEvent.process();  // simulate newline

		assertEquals(1, inputProcessor.commandHistory.size());
		assertEquals("hepl", inputProcessor.commandHistory.get(0));
		assertEquals("", inputProcessor.getCommand());

		inputProcessor.getKeyProcessor().backspaceEvent.process(); // simulate backspace
		inputProcessor.getKeyProcessor().process(Keys.getKeyByValue('t'));
		inputProcessor.getKeyProcessor().process(Keys.getKeyByValue('e'));
		inputProcessor.getKeyProcessor().process(Keys.getKeyByValue('s'));
		inputProcessor.getKeyProcessor().process(Keys.getKeyByValue('t'));
		inputProcessor.getKeyProcessor().backspaceEvent.process();  // simulate backspace
		inputProcessor.getKeyProcessor().backspaceEvent.process();  // simulate backspace
		inputProcessor.getKeyProcessor().process(Keys.getKeyByValue('s'));
		inputProcessor.getKeyProcessor().process(Keys.getKeyByValue('s'));
		inputProcessor.getKeyProcessor().newLineEvent.process();  // simulate newline

		assertEquals(2, inputProcessor.commandHistory.size());
		assertEquals("tess", inputProcessor.commandHistory.get(1));
		assertEquals("", inputProcessor.getCommand());

		inputProcessor.getArrowKeyProcessor().downArrEvent.process();
		inputProcessor.getArrowKeyProcessor().upArrEvent.process();
		assertEquals("tess", inputProcessor.getCommand()); // moved up one, so last command input
		inputProcessor.getArrowKeyProcessor().leftArrEvent.process();
		assertEquals("tess", inputProcessor.getCommand()); // no movement, so same as last time
		inputProcessor.getArrowKeyProcessor().upArrEvent.process();
		assertEquals("hepl", inputProcessor.getCommand()); // moved up one, so second last input
		inputProcessor.getArrowKeyProcessor().upArrEvent.process();
		assertEquals("hepl", inputProcessor.getCommand()); // moved up but at top of list, so same as before
		inputProcessor.getArrowKeyProcessor().downArrEvent.process();
		assertEquals("tess", inputProcessor.getCommand()); // moved down to last input
		inputProcessor.getArrowKeyProcessor().downArrEvent.process();
		inputProcessor.getArrowKeyProcessor().downArrEvent.process();
		inputProcessor.getArrowKeyProcessor().downArrEvent.process();
		assertEquals("", inputProcessor.getCommand()); // moved down past end of list, so print current input ("")
		inputProcessor.getArrowKeyProcessor().upArrEvent.process(); // leaves command equaling "tess"

		inputProcessor.getArrowKeyProcessor().leftArrEvent.process(); // move cursor one character to the left
		inputProcessor.getKeyProcessor().backspaceEvent.process(); // simulate backspace
		inputProcessor.getKeyProcessor().backspaceEvent.process(); // now command should equal "te"
		assertEquals("ts", inputProcessor.getCommand());
		inputProcessor.getKeyProcessor().newLineEvent.process(); // simulate newline

		assertEquals(3, inputProcessor.commandHistory.size());
		assertEquals("ts", inputProcessor.commandHistory.get(2));
		assertEquals("tess", inputProcessor.commandHistory.get(1));
		assertEquals("hepl", inputProcessor.commandHistory.get(0));
	}

	/*
	 * This test tries to intentionally break all possible elements of the input system for the terminal module.
	 */
	@Test
	public void attemptToBreak() {
		// reset variables that might have been modified elsewhere needed for clean test
		JTerm.setOS();
		final TermInputProcessor inputProcessor = new TermInputProcessor(new HeadlessTerminal());
		final TermKeyProcessor keyProcessor = inputProcessor.getKeyProcessor();

		inputProcessor.commandHistory.clear();

		keyProcessor.process(Keys.getKeyByValue(Integer.MAX_VALUE));
		keyProcessor.process(Keys.getKeyByValue(Integer.MIN_VALUE));
		keyProcessor.process(Keys.getKeyByValue(-1));

		for (int i = 0; i < 32; i++)
			if (i != 3 && i != 26) // (char) 26 is Ctrl+Z which calls System.exit() and causes tests to crash
				keyProcessor.process(Keys.getKeyByValue(i));

		assertEquals("", inputProcessor.getCommand());
		StringBuilder expectedCommand = new StringBuilder();
		for (int i = 48; i < 123; i++) {
			keyProcessor.process(Keys.getKeyByValue(i));
			expectedCommand.append((char) i);
		}
		assertEquals(expectedCommand.toString(), inputProcessor.getCommand());

		for (int i = 0; i < 1000; i++)
			keyProcessor.backspaceEvent.process();
		for (int i = 0; i < 1050; i++)
			keyProcessor.newLineEvent.process();
		assertEquals("", inputProcessor.getCommand());
		assertEquals(0, inputProcessor.commandHistory.size());

		for (int i = 0; i < 1000; i++) {
			int t = i % 73 + 48;
			keyProcessor.process(Keys.getKeyByValue(t == 26 ? t + 1 : t)); // To avoid crashing due to Ctrl+Z being emulated
		}

		assertNotEquals("", inputProcessor.getCommand());
		keyProcessor.newLineEvent.process();
		assertEquals(1000, inputProcessor.commandHistory.get(0).length());
	}
}
