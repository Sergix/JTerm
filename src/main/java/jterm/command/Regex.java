package jterm.command;

import jterm.JTerm;
import jterm.util.PrintStreamCollector;
import jterm.util.PromptPrinter;
import jterm.util.Util;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regex {

    private static final Pattern regex = Pattern.compile("\"(.*)\"");

    @Command(name = {"regex", "grep"}, syntax = "regex [-m] \"regex\" command")
    public static void clearScreen(List<String> options) {
        PromptPrinter backup = JTerm.out;
        StringBuilder commandBuilder = new StringBuilder();
        boolean multiline = options.get(0).equals("-m");

        if (multiline) {
            options.remove(0);
        }

        String command = Util.getAsString(options);
        Matcher m = regex.matcher(command);
        String exp;
        if (m.find()) {
            exp = m.group(1);
            command = command.replaceFirst("\".*\"", "");

            PrintStreamCollector collector = new PrintStreamCollector();
            JTerm.out = collector;

            JTerm.executeCommand(command);

            Pattern pattern = Pattern.compile(exp);
            boolean found = false;
            if (multiline) {
                String s = collector.export();
                Matcher matcher = pattern.matcher(s);

                while (matcher.find()) {
                    found = true;

                    for (int i = 1; i <= matcher.groupCount(); i++) {
                        backup.println(matcher.group(i));
                    }
                }
            } else {
                for (String line : collector.exportArray()) {
                    Matcher matcher = pattern.matcher(line);

                    if (matcher.find()) {
                        backup.println(line);
                        found = true;
                    }
                }
            }

            if (!found) {
                backup.println("No matches");
            }
        } else {
            backup.println("Invalid syntax");
        }

        JTerm.out = backup;
    }
}
