package jterm.command;

import jterm.JTerm;
import jterm.io.output.CollectorPrinter;
import jterm.io.output.Printer;
import jterm.io.output.TextColor;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regex {
    private static final Pattern regex = Pattern.compile("\"(.*)\"");

    @Command(name = {"regex", "grep"}, syntax = "regex [-m] \"regex\" command")
    public static void clearScreen(List<String> options) {
        Printer backup = JTerm.out;
        StringBuilder commandBuilder = new StringBuilder();
        boolean multiline = options.get(0).equals("-m");
        if (multiline) options.remove(0);
        options.forEach(commandBuilder::append);
        String command = commandBuilder.toString();
        Matcher m = regex.matcher(command);
        String exp;
        if (m.find()) {
            exp = m.group(1);
            command = command.replaceFirst("\".*\"", "");
            CollectorPrinter collector = new CollectorPrinter();
            JTerm.out = collector;
            JTerm.executeCommand(command);
            Pattern pattern = Pattern.compile(exp);
            boolean found = false;
            if (multiline) {
                String s = collector.export();
                Matcher matcher = pattern.matcher(s);
                while (matcher.find()) {
                    found = true;
                    for (int i = 1; i <= matcher.groupCount(); i++)
                        backup.println(TextColor.INFO, matcher.group(i));
                }
            } else {
                String[] s = collector.exportArray();
                for (String line : s) {
                    Matcher matcher = pattern.matcher(line);
                    if (matcher.find()) {
                        backup.println(TextColor.INFO, line);
                        found = true;
                    }
                }
            }
            if (!found) backup.println(TextColor.ERROR, "No matches");
        } else {
            backup.println(TextColor.ERROR, "Invalid syntax");
        }
        JTerm.out = backup;
    }
}
