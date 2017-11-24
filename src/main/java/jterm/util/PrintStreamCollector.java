package jterm.util;

import jterm.JTerm;

import java.io.PrintStream;
import java.util.Locale;

public class PrintStreamCollector extends PromptPrinter {

    private StringBuilder lines;

    public PrintStreamCollector() {
        lines = new StringBuilder();
    }

    @Override
    public void print(boolean b) {
        lines.append(b);
    }

    @Override
    public void print(char c) {
        lines.append(c);
    }

    @Override
    public void print(int i) {
        lines.append(i);
    }

    @Override
    public void print(long l) {
        lines.append(l);
    }

    @Override
    public void print(float f) {
        lines.append(f);
    }

    @Override
    public void print(double d) {
        lines.append(d);
    }

    @Override
    public void print(char[] s) {
        for (char c : s)
            lines.append(c);
    }

    @Override
    public void print(String s) {
        lines.append(s);
    }

    @Override
    public void print(Object obj) {
        lines.append(obj);
    }

    @Override
    public void println() {
        lines.append("\n");
    }

    @Override
    public void println(boolean x) {
        lines.append(x).append("\n");
    }

    @Override
    public void println(char x) {
        lines.append(x).append("\n");
    }

    @Override
    public void println(int x) {
        lines.append(x).append("\n");
    }

    @Override
    public void println(long x) {
        lines.append(x).append("\n");
    }

    @Override
    public void println(float x) {
        lines.append(x).append("\n");
    }

    @Override
    public void println(double x) {
        lines.append(x).append("\n");
    }

    @Override
    public void println(char[] x) {
        lines.append(x).append("\n");
    }

    @Override
    public void println(String x) {
        lines.append(x).append("\n");
    }


    @Override
    public void println(Object x) {
        lines.append(x).append("\n");
    }

    @Override
    public PrintStream printf(String format, Object... args) {
        lines.append(String.format(format, args));
        return this;
    }

    @Override
    public PrintStream printf(Locale l, String format, Object... args) {
        lines.append(String.format(l, format, args));
        return this;
    }

    @Override
    public PrintStream format(String format, Object... args) {
        lines.append(String.format(format, args));
        return this;
    }

    @Override
    public PrintStream format(Locale l, String format, Object... args) {
        lines.append(String.format(l, format, args));
        return this;
    }

    @Override
    public void printWithPrompt(String s) {
        lines.append(JTerm.PROMPT).append(s);
    }

    @Override
    public void printlnWithPrompt(String s) {
        lines.append(JTerm.PROMPT).append(s).append("\n");
    }

    public String[] exportArray(){
        String[] out = lines.toString().split("\n");
        lines= new StringBuilder();
        return out;
    }
    public String export(){
        String out = lines.toString();
        lines= new StringBuilder();
        return out;
    }
}
