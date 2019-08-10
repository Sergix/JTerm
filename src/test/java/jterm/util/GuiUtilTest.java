package jterm.util;

import jterm.JTerm;
import jterm.gui.Terminal;
import jterm.io.output.GuiPrinter;
import jterm.io.output.TextColor;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GuiUtilTest {
    private static Document doc;

    @BeforeClass
    public static void init() {
        JTerm.setHeadless(false);
    }

    @Before
    public void setup() {
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
    public void clearLineGUI1() throws BadLocationException {
        JTerm.out.clearLine("", 0, true);
        assertEquals("", doc.getText(0, doc.getLength()));
    }

    @Test
    public void clearLineGUI2() throws BadLocationException {
        JTerm.out.clearLine("", 0, false);
        assertTrue(doc.getText(0, doc.getLength()).startsWith("/dir>>"));
    }

    @Test
    public void clearLineGUI3() throws BadLocationException {
        JTerm.out.print(TextColor.INPUT, "stuff");
        JTerm.out.clearLine("stuff", 0, true);
        assertEquals("", doc.getText(0, doc.getLength()));
    }

    @Test
    public void clearLineGUI4() throws BadLocationException {
        JTerm.out.print(TextColor.INPUT, "stuff");
        JTerm.out.clearLine("stuff", 0, false);
        assertTrue(doc.getText(0, doc.getLength()).startsWith("/dir>>"));
    }
}
