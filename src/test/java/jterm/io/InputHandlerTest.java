package jterm.io;

import jterm.io.input.InputHandler;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class InputHandlerTest {
    @Test
    void testDissassembleCommand() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        assertArrayEquals(new String[]{"", "test", ""}, disassembleCommand("test"));
    }


    private String[] disassembleCommand(String command) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = InputHandler.class.getDeclaredMethod("disassembleCommand", String.class);
        method.setAccessible(true);
        return (String[]) method.invoke(InputHandler.class, command);
    }
}
