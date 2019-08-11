package jterm.command;

import com.google.common.collect.Lists;
import jterm.JTerm;
import jterm.io.TestPrinter;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Collections;

import static junit.framework.Assert.assertNotSame;

public class FilesUnitTest {

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
	public void move_testMoveExistingDir_moveSuccess() {
		Dir.md(Collections.singletonList("test"));
		Files.move(Lists.newArrayList("test", "/tmp/test"));
		Dir.rm(Lists.newArrayList("-r", "/tmp/test"));
	}

	@Test
	public void rename_testRenameExistingFolder_renameSuccess() {
		Dir.md(Collections.singletonList("test"));
		Files.rename(Lists.newArrayList("test", "testing"));
		Dir.rm(Lists.newArrayList("-r", "testing"));
	}

	@Test
	public void delete_testDeleteExistingFolder_deleteSuccess() {
		Dir.md(Collections.singletonList("test"));
		Files.delete(Collections.singletonList("test"));
	}

	@Test
	public void read_readExistingFile_readSuccess() {
		final String prevTerminalVal = JTerm.out.toString();
		Files.read(Collections.singletonList("/etc/passwd"));
		assertNotSame(prevTerminalVal, JTerm.out.toString());
	}

	@Test
	public void download_downloadFromURL_downloadSuccess() {
		final String url = "https://www.google.com/doodles";
		Files.download(Collections.singletonList(url));
		Files.delete(Collections.singletonList("doodles.html"));
	}
}
