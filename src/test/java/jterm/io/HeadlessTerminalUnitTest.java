package jterm.io;

import jterm.JTerm;
import jterm.io.terminal.HeadlessTerminal;
import org.junit.BeforeClass;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class HeadlessTerminalUnitTest {

	private HeadlessTerminal headlessTerminal = new HeadlessTerminal();

	@BeforeClass
	public static void setup() {
		JTerm.setOS();
	}

	@Test
	public void changeDir_goBackOneDir_setDir() {
		if (JTerm.IS_UNIX) {
			JTerm.currentDirectory = "/test/welp/";
			headlessTerminal.changeDir("cd ..");
			assertEquals("/test/", JTerm.currentDirectory);
		}
	}

	@Test
	public void changeDir_goFromRootDir_setDir() {
		if (JTerm.IS_UNIX) {
			JTerm.currentDirectory = "/test/welp";
			headlessTerminal.changeDir("cd /root");
			assertEquals("/root/", JTerm.currentDirectory);
		}
	}

	@Test
	public void changeDir_goFromHomeDir_setDir() {
		if (JTerm.IS_UNIX) {
			JTerm.currentDirectory = "/test/welp";
			headlessTerminal.changeDir("cd ~/.m2");
			assertTrue(JTerm.currentDirectory.contains(".m2"));
		}
	}

	@Test
	public void changeDir_goToHomeDir_setDir() {
		if (JTerm.IS_UNIX) {
			JTerm.currentDirectory = "/test/welp";
			headlessTerminal.changeDir("cd ~");
			assertTrue(JTerm.currentDirectory.contains("home"));
		}
	}

	@Test
	public void changeDir_changeRelativeSubDir_setDir() {
		if (JTerm.IS_UNIX) {
			JTerm.currentDirectory = "/usr/";
			headlessTerminal.changeDir("cd bin/");
			assertEquals("/usr/bin/", JTerm.currentDirectory);
		}
	}
}
