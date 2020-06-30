package jterm.command;

import jterm.JTerm;
import java.util.List;
import jterm.command.Window;

public class WindowCmd {
    @Command(name = "window", syntax = "Command syntax:\t\nwindow [-h] [-r] [-v] [-w width] [-l height] [-t title]\n\nCreates a new programmable GUI window.\nDefault title is \"JTerm Window\", and the default width and height of the window is 500px x 500px.")
    public static void WindowCmd(List<String> options){
        new Window(options);
    }
}