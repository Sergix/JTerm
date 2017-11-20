package jterm.gui;

import javax.swing.*;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Locale;

public class PrintStreamInterceptor extends PrintStream {
    private Terminal terminal;
    private PrintStream out;

    PrintStreamInterceptor(PrintStream out, Terminal terminal) {
        super(out);
        this.out = out;
        this.terminal = terminal;
    }

    @Override
    public void print(boolean b) {
        invoke(() -> terminal.print(String.valueOf(b), true));
    }

    @Override
    public void print(char c) {
        invoke(() -> terminal.print(String.valueOf(c), true));
    }

    @Override
    public void print(int i) {
        invoke(() -> terminal.print(String.valueOf(i), true));
    }

    @Override
    public void print(long l) {
        invoke(() -> terminal.print(String.valueOf(l), true));
    }

    @Override
    public void print(float f) {
        invoke(() -> terminal.print(String.valueOf(f), true));
    }

    @Override
    public void print(double d) {
        invoke(() -> terminal.print(String.valueOf(d), true));
    }

    @Override
    public void print(char[] s) {
        for (char c : s)
            invoke(() -> terminal.print(c + "", true));
    }

    @Override
    public void print(String s) {
        invoke(() -> terminal.print(s, true));
    }

    @Override
    public void print(Object obj) {
        invoke(() -> terminal.print(String.valueOf(obj), true));
    }

    @Override
    public void println() {
        invoke(() -> terminal.println("", true));
    }

    @Override
    public void println(boolean x) {
        invoke(() -> terminal.println(String.valueOf(x), true));
    }

    @Override
    public void println(char x) {
        invoke(() -> terminal.println(String.valueOf(x), true));
    }

    @Override
    public void println(int x) {
        invoke(() -> terminal.println(String.valueOf(x), true));
    }

    @Override
    public void println(long x) {
        invoke(() -> terminal.println(String.valueOf(x), true));
    }

    @Override
    public void println(float x) {
        invoke(() -> terminal.println(String.valueOf(x), true));
    }

    @Override
    public void println(double x) {
        invoke(() -> terminal.println(String.valueOf(x), true));
    }

    @Override
    public void println(char[] x) {
        for (char c : x)
            invoke(() -> terminal.print(c + "", true));
        invoke(() -> terminal.println("", true));
    }

    @Override
    public void println(String x) {
        invoke(() -> terminal.println(x, true));
    }


    @Override
    public void println(Object x) {
        invoke(() -> terminal.println(String.valueOf(x), true));
    }

    @Override
    public PrintStream printf(String format, Object... args) {
        invoke(() -> terminal.print(String.format(format, args), true));
        return this;
    }

    @Override
    public PrintStream printf(Locale l, String format, Object... args) {
        invoke(() -> terminal.print(String.format(l, format, args), true));
        return this;
    }

    @Override
    public PrintStream format(String format, Object... args) {
        invoke(() -> terminal.print(String.format(format, args), true));
        return this;
    }

    @Override
    public PrintStream format(Locale l, String format, Object... args) {
        invoke(() -> terminal.print(String.format(l, format, args), true));
        return this;
    }

    private void invoke(Runnable action) {
        try {
            SwingUtilities.invokeAndWait(action);
        } catch (InterruptedException | InvocationTargetException e) {
            e.printStackTrace(out);
        }
    }

}
