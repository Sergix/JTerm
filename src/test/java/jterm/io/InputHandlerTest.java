package jterm.io;

import jterm.io.input.InputHandler;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class InputHandlerTest {
    @Test
    void testDissassembleCommand1e() {
        //Testing single command, cursor at the very end
        InputHandler.setCursorPos(5);
        //                                                             cursor:    v
        assertArrayEquals(new String[]{"", "cd JT", ""}, disassembleCommand("cd JT"));
    }

    @Test
    void testDissassembleCommand1t() {
        //Testing single command, cursor in the autocomplete target
        InputHandler.setCursorPos(4);
        //                                                            cursor:    v
        assertArrayEquals(new String[]{"", "cd J", "T"}, disassembleCommand("cd JT"));
    }

    @Test
    void testDissassembleCommand2e() {
        //Testing two commands, cursor at the very end
        InputHandler.setCursorPos(14);
        //                                                                               cursor:    v
        assertArrayEquals(new String[]{"cd .. && ", "cd JT", ""}, disassembleCommand("cd .. && cd JT"));
    }

    @Test
    void testDissassembleCommand2t() {
        //Testing two commands, cursor in the autocomplete target
        InputHandler.setCursorPos(13);
        //                                                                              cursor:    v
        assertArrayEquals(new String[]{"cd .. && ", "cd J", "T"}, disassembleCommand("cd .. && cd JT"));
    }

    @Test
    void testDissassembleCommand3e() {
        //Testing three commands, cursor at the very end
        InputHandler.setCursorPos(26);
        //                                                                                                       cursor:    v
        assertArrayEquals(new String[]{"cd .. && cd JTerm && ", "cd ou", ""}, disassembleCommand("cd .. && cd JTerm && cd ou"));
    }

    @Test
    void testDissassembleCommand3t() {
        //Testing three commands, cursor in the autocomplete target
        InputHandler.setCursorPos(25);
        //                                                                                                      cursor:    v
        assertArrayEquals(new String[]{"cd .. && cd JTerm && ", "cd o", "u"}, disassembleCommand("cd .. && cd JTerm && cd ou"));
    }

    @Test
    void testDissassembleCommand3m() {
        //Testing three commands, cursor in the middle command and in the autocomplete target
        InputHandler.setCursorPos(13);
        //                                                                                        cursor:    v
        assertArrayEquals(new String[]{"cd .. && ", "cd J", "T && cd out"}, disassembleCommand("cd .. && cd JT && cd out"));
    }


    private String[] disassembleCommand(String command) {
        try {
            Method method = InputHandler.class.getDeclaredMethod("disassembleCommand", String.class);
            method.setAccessible(true);
            return (String[]) method.invoke(InputHandler.class, command);
        } catch (Exception e) {
           System.out.println("Exception thrown");
           return null;
        }
    }
}
