package jterm.util;

import jterm.JTerm;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UtilTest {
    @Test
    void getRunTime() {
        assertEquals(Util.getRunTime(172799999), "1 days, 23 hours, 59 minutes, 59 seconds, 999 millis");
    }

    @Test
    void getAsArray() {
        assertEquals(Util.getAsArray("This function is just splitting on spaces"),
                Arrays.asList("This", "function", "is", "just", "splitting", "on", "spaces"));
    }

    @Test
    void getAsString() {
        assertEquals("This function is just concatenating an array",
                Util.getAsString(Arrays.asList("This", "function", "is", "just", "concatenating", "an", "array")));
    }

    @Test
    void getFullPath() {
        assertEquals("/file.txt", Util.getFullPath("/file.txt"));
        JTerm.currentDirectory = "/blah";
        assertEquals("/blah/file.txt", Util.getFullPath("file.txt"));
    }
}