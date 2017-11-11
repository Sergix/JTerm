package jterm.gui;

import jterm.JTerm;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Terminal extends JFrame {
    private JPanel contentPane = new JPanel();
    private JTextPane textPane = new JTextPane();
    private AttributeSet asWhite;
    private AttributeSet asOffWhite;
    public static String prompt = ">>";
    private ProtectedTextComponent ptc;
    private int preTypeLength = 0;

    public Terminal() {
        setContentPane(contentPane);

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        textPane.setBackground(new Color(28, 28, 28));

        StyleContext sc = StyleContext.getDefaultStyleContext();
        //Color 1 - White
        asWhite = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, new Color(255, 255, 255));
        asWhite = sc.addAttribute(asWhite, StyleConstants.FontFamily, "Lucida Console");
        asWhite = sc.addAttribute(asWhite, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);
        sc = StyleContext.getDefaultStyleContext();
        //Color 2 - Off White
        asOffWhite = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, new Color(130, 130, 130));
        asOffWhite = sc.addAttribute(asOffWhite, StyleConstants.FontFamily, "Lucida Console");
        asOffWhite = sc.addAttribute(asOffWhite, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

        textPane.setEditable(true);
        ptc = new ProtectedTextComponent(textPane);
        println(JTerm.LICENSE, false);
        showPrompt();
        overrideEnter();
    }

    private void onCancel() {
        dispose();
        System.exit(0);
    }

    private void overrideEnter() {
        textPane.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "enter");
        textPane.getActionMap().put("enter", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread(() -> {
                    String text = textPane.getText();
                    String command = text.substring(preTypeLength, text.length());
                    println("", true);
                    System.out.println(command);
                    JTerm.executeCommand(command);
                    showPrompt();
                }).start();
            }
        });
    }

    private void showPrompt() {
        print(prompt, false);
        print(" ", true);
        int promptIndex = textPane.getDocument().getLength();
        textPane.setCaretPosition(promptIndex);
        preTypeLength = textPane.getText().length();
        ptc.protectText(0, promptIndex - 1);
    }

    public void clear() {
        ptc.clearProtections();
        textPane.setText("");
    }

    public void clearLine(String line) {
        ptc.clearProtections();
        String text = textPane.getText().replaceAll("\r", "");
        int ix = text.lastIndexOf("\n") + 1;
        try {
            textPane.getDocument().remove(ix, line.length());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public void print(String s, boolean white) {
        int len = textPane.getDocument().getLength();
        textPane.setCaretPosition(len);
        textPane.setCharacterAttributes(white ? asWhite : asOffWhite, false);
        textPane.replaceSelection(s);
    }

    public void println(String s, boolean white) {
        print(s + "\n", white);
    }
}
