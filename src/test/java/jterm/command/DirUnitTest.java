package jterm.command;

import com.google.common.collect.Lists;
import jterm.JTerm;
import jterm.io.TestPrinter;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

public class DirUnitTest {

	@BeforeClass
	public static void setupBeforeClass() {
		JTerm.out = new TestPrinter();
		JTerm.setOS();
	}

	@Before
	public void setup() {
		JTerm.currentDirectory = System.getProperty("user.home");
	}

	@After
	public void cleanup() {
		JTerm.out.clearAll();
	}

	@Test
	public void ls_printCorrectData() {
		Dir.ls(new LinkedList<>());

		assertTrue(JTerm.out.toString().length() > 0);
	}

	@Test
	public void cd_changeDirs() {
		final String prevDir = JTerm.currentDirectory;

		Dir.cd(Lists.newArrayList(".."));

		assertNotSame(prevDir, JTerm.currentDirectory);
	}

	@Test
	public void pwd_PrintWorkingDirectory() {
		Dir.pwd(new LinkedList<>());

		assertTrue(JTerm.out.toString().contains(JTerm.currentDirectory));
	}

	@Test
	public void createAndDeleteDirectory() {
		Dir.md(Lists.newArrayList("testDir"));
		Dir.rm(Lists.newArrayList("-r", "testDir"));
		System.err.println(JTerm.out.toString());
	}
}
