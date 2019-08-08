package jterm.io.output;

import jterm.JTerm;

public class CollectorPrinter implements Printer {
    private StringBuilder lines = new StringBuilder();
    private Printer printer;

    public CollectorPrinter() {
    }

    public CollectorPrinter(Printer printer) {
        this.printer = printer;
    }

    @Override
	public void print(TextColor color, String str) {
		lines.append(str);
    }

    @Override
	public void print(TextColor color, char c) {
		lines.append(c);
    }

    @Override
    public void println(TextColor color) {
        lines.append("\n");
    }

    @Override
	public void println(TextColor color, String str) {
		lines.append(str);
    }

    @Override
	public void println(TextColor color, char c) {
		lines.append(c);
    }

    @Override
    public Printer printf(TextColor color, String format, Object... args) {
        lines.append(String.format(format, args));
        return this;
    }

    @Override
    public void clearLine(String line, int cursorPosition, boolean clearPrompt) {
        printer.clearLine(line,cursorPosition,clearPrompt);
    }

    @Override
    public void clearAll() {
        lines = new StringBuilder();
    }

    @Override
    public void printPrompt() {
        lines.append(JTerm.PROMPT);
    }

    @Override
	public void printWithPrompt(TextColor c, String str) {
		lines.append(JTerm.PROMPT).append(str);
    }

    public String[] exportArray() {
        String[] out = lines.toString().split("\n");
        lines = new StringBuilder();
        return out;
    }

    public String export() {
        String out = lines.toString();
        lines = new StringBuilder();
        return out;
    }
}
