package jterm.util;

import jterm.JTerm;
import jterm.io.TestPrinter;
import jterm.io.output.TextColor;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;

import static junit.framework.Assert.assertEquals;

public class UtilUnitTest {

    @Test
    public void getRunTime() {
        assertEquals(Util.getRunTime(172799999), "1 days, 23 hours, 59 minutes, 59 seconds, 999 millis");
    }

    @BeforeClass
    public static void classSetup() {
        JTerm.setOS();
    }

    @Before
    public void setup() {
        JTerm.out = new TestPrinter();
        JTerm.currentDirectory = "/dir";
    }

    @After
    public void cleanup() {
        JTerm.out.clearAll();
    }

    @Test
    public void clearLineHeadless() {
        JTerm.setHeadless(true);

        JTerm.out.printPrompt();
        JTerm.out.clearLine("", 0, true);
        assertEquals("", JTerm.out.toString());

        JTerm.out.printWithPrompt(TextColor.INPUT, "stuff");
        JTerm.out.clearLine("stuff", 5, true);
        assertEquals("", JTerm.out.toString());

        JTerm.out.printPrompt();
        JTerm.out.clearLine("", 0, false);
        assertEquals("/dir>> ", JTerm.out.toString());

        JTerm.out.printWithPrompt(TextColor.INPUT, "stuff");
        JTerm.out.clearLine("stuff", 5, false);
        assertEquals("/dir>> ", JTerm.out.toString());
    }

    @Test
    public void getAsArray() {
        assertEquals(Util.getAsArray("This function is just splitting on spaces"),
                Arrays.asList("This", "function", "is", "just", "splitting", "on", "spaces"));
    }

    @Test
    public void getAsString() {
        assertEquals("This function is just concatenating an array",
                Util.getAsString(Arrays.asList("This", "function", "is", "just", "concatenating", "an", "array")));
    }

    @Test
    public void getFullPath() {
        assertEquals("/file.txt", Util.getFullPath("/file.txt"));
        JTerm.currentDirectory = "/blah";
        assertEquals("/blah/file.txt", Util.getFullPath("file.txt"));
    }

    private void compareTimes(Runnable r1, Runnable r2) {
        averageTimeOfExecution(r1);
        long r1time = averageTimeOfExecution(r1);
        randomBranches();
        averageTimeOfExecution(r2);
        long r2time = averageTimeOfExecution(r2);
        System.out.println(String.format("R1: %d, R2: %d", r1time, r2time));
    }

    private void randomBranches() {
        int rand = 0;
        for (int i = 0; i < 10000; i++) {
            if (i + (Math.random() * 100) < i + (Math.random() * 100)) {
                rand++;
            } else {
                rand--;
            }
        }
    }

    private long averageTimeOfExecution(Runnable r) {
        long before, after;
        long total = 0;
        for (int i = 0; i < 1000; i++) {
            before = System.nanoTime();
            r.run();
            after = System.nanoTime();
            total += after - before;
        }
        return total / 1000;
    }
}