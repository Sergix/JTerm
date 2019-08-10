package jterm.command;

import jterm.JTerm;
import jterm.io.TestPrinter;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Collections;

public class PingUnitTest {

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
	public void ping_testPing_pingSuccess() {
		Ping.ping(Collections.singletonList("www.google.es"));
	}
}
