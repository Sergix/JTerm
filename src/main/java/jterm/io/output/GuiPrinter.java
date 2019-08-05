package jterm.io.output;

import jterm.JTerm;
import jterm.gui.ProtectedTextComponent;

import javax.swing.*;
import javax.swing.text.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Locale;

public class GuiPrinter implements Printer {
    private final JTextPane textPane;
    private ProtectedTextComponent ptc;

    public GuiPrinter(JTextPane textPane) {
        this.textPane = textPane;
        ptc = new ProtectedTextComponent(textPane);
    }

    public void print(final TextColor color, String x) {
        print(x, color);
    }

    @Override
    public void print(final TextColor color, final char x) {
        print(String.valueOf(x), color);
    }

    public void print(final TextColor color, final Object x) {
        print(String.valueOf(x), color);
    }

    public void println(final TextColor color) {
        print("\n", color);
    }

    public void println(final TextColor color, final String x) {
        print(x + "\n", color);
    }

    @Override
    public void println(final TextColor color, final char x) {
        print(String.valueOf(x) + "\n", color);
    }

    public void println(final TextColor color, final Object x) {
        print(String.valueOf(x) + "\n", color);
    }

    public GuiPrinter printf(final TextColor color, final String format, final Object... args) {
        print(String.format(format, args), color);
        return this;
    }

    public GuiPrinter printf(final TextColor color, final Locale l, final String format, final Object... args) {
        print(String.format(l, format, args), color);
        return this;
    }


    @Override
    public void printWithPrompt(final TextColor color, final String s) {
        printPrompt();
        print(s, color);
    }

    @Override
    public void printPrompt() {
        invoke(() -> {
            print(JTerm.currentDirectory, TextColor.PATH);
            print(JTerm.PROMPT, TextColor.PROMPT);
            print("", TextColor.INPUT);
            int promptIndex = textPane.getDocument().getLength();
            textPane.setCaretPosition(promptIndex);
            ptc.protectText(0, promptIndex - 1);
        });
    }

    public void clearLine(final String line, final int cursorPosition, final boolean clearPrompt) {
        if (clearPrompt)
            ptc.clearProtections();

        String textToClear = "";
        if (clearPrompt)
            textToClear += JTerm.currentDirectory + JTerm.PROMPT;
        textToClear += line;

        try {
            textPane.getDocument().remove(textPane.getText().lastIndexOf(textToClear), textToClear.length());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clearAll() {
        ptc.clearProtections();
        textPane.setText("");
    }

    private void print(final String s, final TextColor c) {
        final StyleContext sc = StyleContext.getDefaultStyleContext();
        final AttributeSet color = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c.getColor());
        final int len = textPane.getDocument().getLength();
        textPane.setCaretPosition(len);
        textPane.setCharacterAttributes(color, false);
        textPane.replaceSelection(s);
    }

    private void invoke(final Runnable action) {
        try {
            SwingUtilities.invokeAndWait(action);
        } catch (InterruptedException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
