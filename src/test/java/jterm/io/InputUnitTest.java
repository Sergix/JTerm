package jterm.io;

import jterm.JTerm;
import jterm.io.input.Input;
import jterm.io.input.UnixInput;
import jterm.io.input.WinInput;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class InputUnitTest {
	@InjectMocks
	private Input input;

	@Mock
	private UnixInput unixInput;

	@Mock
	private WinInput winInput;

	@Before
	public void setup() {
		try {
			when(winInput.readWindows(anyBoolean())).thenReturn(1);
			when(unixInput.readUnix(anyBoolean())).thenReturn(101);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void readInput_isWindowsFlagFalse_returnUnixInput() {
		boolean prevWindowsVal = JTerm.IS_WIN;
		boolean prevUnixVal = JTerm.IS_UNIX;
		JTerm.IS_WIN = false;
		JTerm.IS_UNIX = true;

		final int ret = input.read(false);
		assertEquals(101, ret);

		JTerm.IS_WIN = prevWindowsVal;
		JTerm.IS_UNIX = prevUnixVal;
	}

	@Test
	public void readInput_isUnixFlagFalse_returnWindowsInput() {
		boolean prevWindowsVal = JTerm.IS_WIN;
		boolean prevUnixVal = JTerm.IS_UNIX;
		JTerm.IS_WIN = true;
		JTerm.IS_UNIX = false;

		final int ret = input.read(false);
		assertEquals(1, ret);

		JTerm.IS_WIN = prevWindowsVal;
		JTerm.IS_UNIX = prevUnixVal;
	}
}
