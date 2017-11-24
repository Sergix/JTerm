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

    public void print(TextColor color, String x) {
        print(x, color);
    }

    @Override
    public void print(TextColor color, char x) {
        print(String.valueOf(x), color);
    }

    public void print(TextColor color, Object x) {
        print(String.valueOf(x), color);
    }

    public void println(TextColor color) {
        print("\n", color);
    }

    public void println(TextColor color, String x) {
        print(x + "\n", color);
    }

    @Override
    public void println(TextColor color, char x) {
        print(String.valueOf(x) + "\n", color);
    }

    public void println(TextColor color, Object x) {
        print(String.valueOf(x) + "\n", color);
    }

    public GuiPrinter printf(TextColor color, String format, Object... args) {
        print(String.format(format, args), color);
        return this;
    }

    public GuiPrinter printf(TextColor color, Locale l, String format, Object... args) {
        print(String.format(l, format, args), color);
        return this;
    }


    @Override
    public void printWithPrompt(TextColor color, String s) {
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

    public void clearLine(String line, int cursorPosition, boolean clearPrompt) {
        if (clearPrompt) ptc.clearProtections();
        String text = textPane.getText().replaceAll("\r", "");
        int ix = text.lastIndexOf("\n") + 1;
        int len = line.length();
        int fullPromptLen = JTerm.PROMPT.length() + JTerm.currentDirectory.length();
        if (clearPrompt) len += fullPromptLen;
        else ix += fullPromptLen;
        if (ix >= text.length()) return;
        try {
            textPane.getDocument().remove(ix, len);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clearAll() {
        ptc.clearProtections();
        textPane.setText("");
    }

    private void print(String s, TextColor c) {
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet color = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c.getColor());
        int len = textPane.getDocument().getLength();
        textPane.setCaretPosition(len);
        textPane.setCharacterAttributes(color, false);
        textPane.replaceSelection(s);
    }

    private void invoke(Runnable action) {
        try {
            SwingUtilities.invokeAndWait(action);
        } catch (InterruptedException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
