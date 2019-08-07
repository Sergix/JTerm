package jterm.io.output;

import jterm.JTerm;
import jterm.gui.ProtectedTextComponent;

import javax.swing.*;
import javax.swing.text.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Locale;

/**
 * Custom {@link Printer} implementation for the JTerm GUI mode.
 */
public class GuiPrinter implements Printer {
    private final JTextPane textPane;
    private ProtectedTextComponent ptc;

    public GuiPrinter(final JTextPane textPane) {
        this.textPane = textPane;
        ptc = new ProtectedTextComponent(textPane);
    }

    public void print(final TextColor color, final String str) {
        print(str, color);
    }

    @Override
    public void print(final TextColor color, final char c) {
        print(String.valueOf(c), color);
    }

    public void print(final TextColor color, final Object o) {
        print(String.valueOf(o), color);
    }

    public void println(final TextColor color) {
        print("\n", color);
    }

    public void println(final TextColor color, final String str) {
        print(str + "\n", color);
    }

    @Override
    public void println(final TextColor color, final char c) {
        print(c + "\n", color);
    }

    public void println(final TextColor color, final Object o) {
        print(o + "\n", color);
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
        if (clearPrompt || line.length() > 0) {
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
