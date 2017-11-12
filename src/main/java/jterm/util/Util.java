package jterm.util;

import jterm.JTerm;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Util {

    /**
     * getRuntime() String
     * <br></br><br></br>
     * Takes an interval of time in milliseconds, and returns amount of time it represents.
     * Useful for determining runtime of a program in a more readable format.
     *
     * @param interval milliseconds to convert to dd, hh, mm, ss, SSS format
     * @return time converted to readable format
     */
    public static String getRunTime(long interval) {
        long seconds = interval / 1000;
        String time = "";

        if (seconds / 86400 >= 1) {
            time += String.valueOf(seconds / 86400) + " days, ";
        }
        if ((seconds / 3600) >= 1) {
            time += String.valueOf((seconds / 3600) % 24) + " hours, ";
        }
        if ((seconds / 60) >= 1) {
            time += String.valueOf((seconds / 60) % 60) + " minutes, ";
        }

        time += String.valueOf(seconds % 60) + " seconds, ";
        time += String.valueOf(interval % 1000) + " millis";

        return time;
    }

    /**
     * clearLine() void
     * <br></br><br></br>
     * Clears a line in the console of size line.length().
     *
     * @param line        line to be cleared
     * @param clearPrompt choose to clear PROMPT along with line (only use true if PROMPT exists)
     */
    public static void clearLine(String line, boolean clearPrompt) {
        for (int i = 0; i < line.length() + (clearPrompt ? JTerm.PROMPT.length() / 3 : 0); i++) {
            System.out.print("\b");
        }

        for (int i = 0; i < line.length() + (clearPrompt ? JTerm.PROMPT.length() / 3 : 0); i++) {
            System.out.print(" ");
        }

        for (int i = 0; i < line.length() + (clearPrompt ? JTerm.PROMPT.length() / 3 : 0); i++) {
            System.out.print("\b");
        }
    }

    public static ArrayList<String> getAsArray(String options) {
        try (Scanner tokenizer = new Scanner(options)) {
            ArrayList<String> optionsArray = new ArrayList<>();
            while (tokenizer.hasNext()) {
                optionsArray.add(tokenizer.next());
            }
            return optionsArray;
        }
    }

    public static String getAsString(List<String> options) {
        StringBuilder result = new StringBuilder();
        for (String option : options) {
            result.append(option);
            result.append(" ");
        }
        return result.substring(0, result.length() - 1);
    }

    public static String getRest(List<String> options, int index) {
        StringBuilder outputBuilder = new StringBuilder();
        for (int i = index; i < options.size(); i++) {
            if (i != options.size() - 1) {
                outputBuilder
                        .append(options.get(i))
                        .append(" ");
            } else {
                outputBuilder.append(options.get(i));
            }
        }
        return outputBuilder.toString();
    }
}
