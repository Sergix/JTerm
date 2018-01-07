package jterm.util;

import jterm.JTerm;
import jterm.gui.Terminal;
import jterm.io.output.GuiPrinter;
import jterm.io.output.TextColor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GuiUtilTest {
    private static Document doc;

    @BeforeAll
    static void init() {
        JTerm.setheadless(false);
    }

    @BeforeEach
    void setup() {
        Terminal terminal = new Terminal();
        terminal.setTitle("JTerm");
        terminal.setSize(720, 480);
        terminal.setVisible(true);
        JTerm.setTerminal(terminal);
        JTerm.out = new GuiPrinter(terminal.getTextPane());
        doc = terminal.getTextPane().getDocument();
        JTerm.currentDirectory = "/dir";
        JTerm.out.clearAll();
        JTerm.out.printPrompt();
    }

    @Test
    void clearLineGUI1() throws BadLocationException {
        JTerm.out.clearLine("", 0, true);
        assertEquals("", doc.getText(0, doc.getLength()));
    }

    @Test
    void clearLineGUI2() throws BadLocationException {
        JTerm.out.clearLine("", 0, false);
        assertEquals("/dir>> ", doc.getText(0, doc.getLength()));
    }

    @Test
    void clearLineGUI3() throws BadLocationException {
        JTerm.out.print(TextColor.INPUT, "stuff");
        JTerm.out.clearLine("stuff", 0, true);
        assertEquals("", doc.getText(0, doc.getLength()));
    }

    @Test
    void clearLineGUI4() throws BadLocationException {
        JTerm.out.print(TextColor.INPUT, "stuff");
        JTerm.out.clearLine("stuff", 0, false);
        assertEquals("/dir>> ", doc.getText(0, doc.getLength()));
    }

}
