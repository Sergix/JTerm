package jterm.io.output;

import java.awt.*;

public enum TextColor {
    INPUT, PATH, PROMPT, INFO, ERROR;

    String ansi;
    Color color;

    public static void initHeadless() {
        //TODO: Switch these back when ANSI is fixed in terminal
        INPUT.ansi = "";
        PATH.ansi = "";
        PROMPT.ansi = "";
        INFO.ansi = "";
        ERROR.ansi = "";
//        INPUT.ansi = "\\u001b[31m";
//        PATH.ansi = "\\u001b[31m";
//        PROMPT.ansi = "\\u001b[31m";
//        INFO.ansi = "\\u001b[31m";
//        ERROR.ansi = "\\u001b[31m";
    }

    public static void initGui() {
        INPUT.color = new Color(255, 255, 255);
        PATH.color = new Color(142, 114, 77);
        PROMPT.color = new Color(193, 122, 27);
        INFO.color = new Color(150, 150, 150);
        ERROR.color = new Color(140, 40, 40);
    }

    public Color getColor() {
        return color;
    }
}
