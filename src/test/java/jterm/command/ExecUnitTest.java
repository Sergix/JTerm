package jterm.command;

import com.google.common.collect.Lists;
import jterm.JTerm;
import jterm.io.TestPrinter;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ExecUnitTest {
	@Test
	public void exec_execRunNonExistentJar_readErrorFromStream() {
		final String jarFile = "welp.jar";

		JTerm.out = new TestPrinter();
		JTerm.setOS();
		Exec.execute(Lists.newArrayList(jarFile));

		assertEquals("Error: Unable to access jarfile " + jarFile + "\n\n", JTerm.out.toString());
	}
}
