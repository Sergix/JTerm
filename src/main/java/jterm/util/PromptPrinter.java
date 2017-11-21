package jterm.util;

import java.io.PrintStream;

public abstract class PromptPrinter extends PrintStream {

    PromptPrinter() {
        super(System.out);
    }

    public abstract void printWithPrompt(String s);

    public abstract void printlnWithPrompt(String s);
}
