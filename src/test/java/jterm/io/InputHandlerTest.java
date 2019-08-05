package jterm.io;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class InputHandlerTest {
    @Test
    void testDissassembleCommand() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        assertArrayEquals(new String[]{"", "test", ""}, disassembleCommand("test"));
    }


    private String[] disassembleCommand(String command) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
//        Method method = InputHandler.class.getDeclaredMethod("disassembleCommand", String.class);
//        method.setAccessible(true);
//        return (String[]) method.invoke(InputHandler.class, command);
		return new String[]{};
    }
}
