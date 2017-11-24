package jterm.util;

import jterm.JTerm;

public class PromptInterceptor extends PromptPrinter {

    @Override
    public void printWithPrompt(String s) {
        System.out.print(JTerm.PROMPT + s);
    }

    @Override
    public void printlnWithPrompt(String s) {
        System.out.println(JTerm.PROMPT + s);
    }
}
