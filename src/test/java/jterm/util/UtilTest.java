package jterm.util;

import jterm.JTerm;
import jterm.gui.Terminal;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.*;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class UtilTest {
    @Test
    void getRunTime() {
        assertEquals(Util.getRunTime(172799999), "1 days, 23 hours, 59 minutes, 59 seconds, 999 millis");
    }

    @Test
    void clearLineHeadless() throws BadLocationException {
        JTerm.setheadless(true);
        PrintStreamCollector collector = new PrintStreamCollector();
        JTerm.out = collector;

        JTerm.out.print(">> ");
        Util.clearLine("", 0, true);
        assertEquals(">> \b\b\b   \b\b\b", collector.export());

        JTerm.out.print(">> stuff");
        Util.clearLine("stuff", 5, true);
        assertEquals(">> stuff\b\b\b\b\b\b\b\b        \b\b\b\b\b\b\b\b", collector.export());

        JTerm.out.print(">> stuff");
        Util.clearLine("stuff", 5, false);
        assertEquals(">> stuff\b\b\b\b\b     \b\b\b\b\b", collector.export());
    }

    @Test
    void clearLineTerminal() throws BadLocationException {
        JTerm.setheadless(false);
        Terminal terminal = new Terminal();
        terminal.setTitle("JTerm");
        terminal.setSize(720, 480);
        terminal.setVisible(true);
        JTerm.setTerminal(terminal);
        JTerm.out = new PrintStreamInterceptor(terminal);
        Document doc = terminal.getTextPane().getDocument();

        terminal.clear();
        JTerm.out.println();
        JTerm.out.printWithPrompt("");
        Util.clearLine("", 0, true);
        assertEquals("\n", doc.getText(0, doc.getLength()));

        terminal.clear();
        terminal.print("\n", true);
        JTerm.out.printWithPrompt("stuff");
        Util.clearLine("stuff", 0, true);
        assertEquals("\n", doc.getText(0, doc.getLength()));

        terminal.clear();
        terminal.print("\n", true);
        JTerm.out.printWithPrompt("stuff");
        Util.clearLine("stuff", 0, false);
        assertEquals("\n>> ", doc.getText(0, doc.getLength()));
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
    void getRest() {
        assertEquals("is just concatenating an array",
                Util.getRest(Arrays.asList("This", "function", "is", "just", "concatenating", "an", "array"), 2));
    }

    @Test
    void getFullPath() {
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