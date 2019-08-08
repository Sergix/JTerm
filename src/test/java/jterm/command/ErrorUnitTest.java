package jterm.command;

import com.google.common.collect.Lists;
import jterm.JTerm;
import jterm.io.TestPrinter;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ErrorUnitTest {
	@Test
	public void exec_execRunNonExistentJar_readErrorFromStream() {
		final String jarFile = "welp.jar";

		JTerm.out = new TestPrinter();
		Exec.execute(Lists.newArrayList(jarFile));

		assertEquals("Error: Unable to access jarfile " + jarFile + "\n", JTerm.out.toString());
	}
}
