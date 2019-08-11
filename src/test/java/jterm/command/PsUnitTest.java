package jterm.command;

import jterm.JTerm;
import jterm.io.TestPrinter;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertTrue;

public class PsUnitTest {

	@BeforeClass
	public static void setupBeforeClass() {
		JTerm.out = new TestPrinter();
		JTerm.setOS();
	}

	@Before
	public void setup() {
		JTerm.currentDirectory = System.getProperty("user.home");
	}

	@Test
	public void ps_testPrintRunningProcesses_printSuccessfully() {
		Ps.printRunningProcesses(Collections.emptyList());

		assertTrue(JTerm.out.toString().contains("java"));
		assertTrue(JTerm.out.toString().contains("PID"));
		assertTrue(JTerm.out.toString().contains("TTY"));
	}
}
