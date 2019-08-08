package jterm.util;

import jterm.JTerm;
import jterm.gui.Terminal;
import jterm.io.output.CollectorPrinter;
import jterm.io.output.GuiPrinter;
import jterm.io.output.HeadlessPrinter;
import jterm.io.output.TextColor;
import org.junit.After;
import org.junit.Test;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.util.Arrays;

import static junit.framework.Assert.assertEquals;

public class UtilUnitTest {

    @Test
    public void getRunTime() {
        assertEquals(Util.getRunTime(172799999), "1 days, 23 hours, 59 minutes, 59 seconds, 999 millis");
    }

    @After
    public void cleanup() {
        JTerm.setPrompt(">>");
        JTerm.out.clearAll();
    }

    @Test
    public void clearLineHeadless() {
        JTerm.setHeadless(true);
        CollectorPrinter collector = new CollectorPrinter(new HeadlessPrinter());
        JTerm.out = collector;
        JTerm.setPrompt("/dir>> ");

        JTerm.out.printPrompt();
        JTerm.out.clearLine("", 0, true);
        assertEquals("/dir>> ", collector.export());

        JTerm.out.printWithPrompt(TextColor.INPUT, "stuff");
        JTerm.out.clearLine("stuff", 5, true);
        assertEquals("/dir>> stuff", collector.export());

        JTerm.out.printPrompt();
        JTerm.out.clearLine("", 0, false);
        assertEquals("/dir>> ", collector.export());

        JTerm.out.printWithPrompt(TextColor.INPUT, "stuff");
        JTerm.out.clearLine("stuff", 5, false);
        assertEquals("/dir>> stuff", collector.export());
    }

    @Test
    public void clearLineGUI() throws BadLocationException {
        JTerm.setHeadless(false);
        Terminal terminal = new Terminal();
        terminal.setTitle("JTerm");
        terminal.setSize(720, 480);
        terminal.setVisible(true);
        JTerm.setTerminal(terminal);
        JTerm.out = new GuiPrinter(terminal.getTextPane());
        Document doc = terminal.getTextPane().getDocument();
        JTerm.currentDirectory = "/dir";

        JTerm.out.clearAll();
        JTerm.out.printPrompt();
        JTerm.out.clearLine("", 0, true);
        assertEquals("", doc.getText(0, doc.getLength()));

        JTerm.out.clearAll();
        JTerm.out.printPrompt();
        JTerm.out.clearLine("", 0, false);
        assertEquals("/dir>>", doc.getText(0, doc.getLength()));

        JTerm.out.clearAll();
        JTerm.out.printWithPrompt(TextColor.INPUT, "stuff");
        JTerm.out.clearLine("stuff", 0, true);
        assertEquals("", doc.getText(0, doc.getLength()));

        JTerm.out.clearAll();
        JTerm.out.printWithPrompt(TextColor.INPUT, "stuff");
        JTerm.out.clearLine("stuff", 0, false);
        assertEquals("/dir>>", doc.getText(0, doc.getLength()));
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