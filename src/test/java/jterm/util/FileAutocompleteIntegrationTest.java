package jterm.util;

import jterm.JTerm;
import jterm.io.handlers.InputHandler;
import jterm.io.input.Keys;
import jterm.io.output.HeadlessPrinter;
import jterm.io.output.TextColor;
import jterm.io.terminal.HeadlessTerminal;
import jterm.io.terminal.TermInputProcessor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileAutocompleteIntegrationTest {

	private TermInputProcessor termInputProcessor;

	@BeforeAll
	private static void setupTests() {
		JTerm.setOS();
		TextColor.initHeadless();
		try {
			Files.createFile(Paths.get("jtermTestFile.txt"));
			Files.createFile(Paths.get("jtermAnotherTestFile.txt"));
			Files.createFile(Paths.get("/tmp/jtermTestTmpFile.txt"));
			Files.createDirectory(Paths.get("jtermTestDir"));
			Files.createDirectory(Paths.get("jtermAnotherTestDir"));
			Files.createDirectory(Paths.get("/tmp/jtermTmpTestDir"));
			Files.createDirectory(Paths.get("/tmp/jtermAnotherTmpTestDir"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@BeforeEach
	private void setup() {
		termInputProcessor = new TermInputProcessor(new HeadlessTerminal());
		JTerm.out = new HeadlessPrinter();
	}

	@AfterAll
	private static void cleanupTests() {
		try {
			Files.deleteIfExists(Paths.get("jtermTestFile.txt"));
			Files.deleteIfExists(Paths.get("jtermAnotherTestFile.txt"));
			Files.deleteIfExists(Paths.get("/tmp/jtermTestTmpFile.txt"));
			Files.deleteIfExists(Paths.get("jtermTestDir"));
			Files.deleteIfExists(Paths.get("jtermAnotherTestDir"));
			Files.deleteIfExists(Paths.get("/tmp/jtermTmpTestDir"));
			Files.deleteIfExists(Paths.get("/tmp/jtermAnotherTmpTestDir"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testAutocompleteInCurrentDir() {
		inputCommand("jterm");
		termInputProcessor.getKeyProcessor().tabEvent.process();
		termInputProcessor.getKeyProcessor().tabEvent.process();

		assertTrue(termInputProcessor.getCommand().contains("jterm"));
	}

	@Test
	public void testAutocompleteInTmpDir() {
		inputCommand("/tmp/jterm");

		termInputProcessor.getKeyProcessor().tabEvent.process();
		termInputProcessor.getKeyProcessor().tabEvent.process();

		assertTrue(termInputProcessor.getCommand().contains("/tmp/jterm"));
	}

	@Test
	public void testAutocompleteInCurrentDirWithChainedCommands() {
		inputCommand("ls && jterm");
		termInputProcessor.getKeyProcessor().tabEvent.process();
		termInputProcessor.getKeyProcessor().tabEvent.process();

		assertTrue(termInputProcessor.getCommand().contains("ls && jterm"));
	}

	@Test
	public void testAutocompleteInTmpDirWithChainedCommands() {
		inputCommand("ls && /tmp/jterm");
		termInputProcessor.getKeyProcessor().tabEvent.process();
		termInputProcessor.getKeyProcessor().tabEvent.process();

		assertTrue(termInputProcessor.getCommand().contains("ls && /tmp/jterm"));
	}

	@Test
	public void testAutocompleteInCurrentDirWithMultipleChainedCommands() {
		inputCommand("ls && jterm && welp");
		setCursorPos("ls && jterm".length());
		termInputProcessor.getKeyProcessor().tabEvent.process();
		termInputProcessor.getKeyProcessor().tabEvent.process();

		assertTrue(termInputProcessor.getCommand().startsWith("ls && jterm"));
		assertTrue(termInputProcessor.getCommand().endsWith(" && welp"));
	}

	@Test
	public void testAutocompleteInTmpDirWithMultipleChainedCommands() {
		inputCommand("ls && /tmp/jterm && welp");
		setCursorPos("ls && /tmp/jterm".length());
		termInputProcessor.getKeyProcessor().tabEvent.process();
		termInputProcessor.getKeyProcessor().tabEvent.process();

		assertTrue(termInputProcessor.getCommand().startsWith("ls && /tmp/jterm"));
		assertTrue(termInputProcessor.getCommand().endsWith(" && welp"));
	}

	private void inputCommand(final String command) {
		for (int i = 0; i < command.length(); i++) {
			try {
				Thread.sleep(InputHandler.minWaitTime + 1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			final Keys keys = Keys.CHAR;
			keys.setValue(command.charAt(i));
			termInputProcessor.getKeyProcessor().process(keys);
		}
	}

	private void setCursorPos(int targetPos) {
		while (termInputProcessor.getCursorPos() < targetPos)
			termInputProcessor.increaseCursorPos();

		while (termInputProcessor.getCursorPos() > targetPos)
			termInputProcessor.decreaseCursorPos();
	}
}
