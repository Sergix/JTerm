package jterm.util;

import jterm.JTerm;

public class Util {

    /**
     * GetRuntime() String
     * <br></br><br></br>
     * Takes an interval of time in milliseconds, and returns amount of time it represents.
     * Useful for determining runtime of a program in a more readable format.
     * <br></br>
     *
     * @param interval milliseconds to convert to dd, hh, mm, ss, SSS format
     * @return time converted to readable format
     */
    public static String getRunTime(long interval) {
        long seconds = interval / 1000;
        String time = "";

        if (seconds / 86400 >= 1)
            time += String.valueOf(seconds / 86400) + " days, ";
        if ((seconds / 3600) >= 1)
            time += String.valueOf((seconds / 3600) % 24) + " hours, ";
        if ((seconds / 60) >= 1)
            time += String.valueOf((seconds / 60) % 60) + " minutes, ";

        time += String.valueOf(seconds % 60) + " seconds, ";
        time += String.valueOf(interval % 1000) + " millis";

        return time;
    }

    /**
     * ClearLine() void
     * <br></br><br></br>
     * Clears a line in the console of size line.length().
     * <br></br>
     *
     * @param line        line to be cleared
     * @param clearPrompt choose to clear prompt along with line (only use true if prompt exists)
     */
    public static void clearLine(String line, boolean clearPrompt) {

        for (int i = 0; i < line.length() + (clearPrompt ? JTerm.prompt.length() / 3 : 0); i++)
            System.out.print("\b");

        for (int i = 0; i < line.length() + (clearPrompt ? JTerm.prompt.length() / 3 : 0); i++)
            System.out.print(" ");

        for (int i = 0; i < line.length() + (clearPrompt ? JTerm.prompt.length() / 3 : 0); i++)
            System.out.print("\b");

    }
}
