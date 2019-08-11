package jterm.io;

import jterm.io.terminal.TermInputProcessor;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class TermInputProcessorTest {
	@Test
	void testDissassembleCommand() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		assertArrayEquals(new String[]{"", "test", ""}, disassembleCommand("test"));
	}


	private String[] disassembleCommand(final String command) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		Method method = TermInputProcessor.class.getDeclaredMethod("disassembleCommand", String.class, Integer.class);
		method.setAccessible(true);
		return (String[]) method.invoke(TermInputProcessor.class, command, command.length());
	}
}
