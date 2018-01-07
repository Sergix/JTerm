package jterm.util;

import jterm.JTerm;
import jterm.io.output.CollectorPrinter;
import jterm.io.output.HeadlessPrinter;
import jterm.io.output.TextColor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HeadlessUtilTest {
    private static CollectorPrinter collector;

    @BeforeAll
    static void init() {
        JTerm.setheadless(true);
        JTerm.setPrompt(">> ");
        JTerm.setCurrentDirectory("/dir");
    }

    @BeforeEach
    void setup(){
        collector = new CollectorPrinter(new HeadlessPrinter());
        JTerm.out = collector;
        JTerm.out.printPrompt();
    }

    @Test
    void clearLineHeadless1() {
        //Testing clear only prompt
        JTerm.out.clearLine("", 0, true);
        assertEquals("/dir>> \b\b\b\b\b\b\b       \b\b\b\b\b\b\b", collector.export());
    }

    @Test
    void clearLineHeadless2() {
        //Testing clear prompt with stuff
        JTerm.out.print(TextColor.INPUT, "stuff");
        JTerm.out.clearLine("stuff", 5, true);
        assertEquals("/dir>> stuff\b\b\b\b\b\b\b\b\b\b\b\b            \b\b\b\b\b\b\b\b\b\b\b\b", collector.export());
    }

    @Test
    void clearLineHeadless3() {
        //Testing clear only stuff, but there is no stuff to clear
        JTerm.out.clearLine("", 0, false);
        assertEquals("/dir>> ", collector.export());
    }

    @Test
    void clearLineHeadless4() {
        //Testing clear only stuff
        JTerm.out.print(TextColor.INPUT, "stuff");
        JTerm.out.clearLine("stuff", 5, false);
        assertEquals("/dir>> stuff\b\b\b\b\b     \b\b\b\b\b", collector.export());
    }
}
