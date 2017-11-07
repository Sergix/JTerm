package jterm.util;

import jterm.JTerm;

public class Util {

    /**
     * GetRuntime() String
     * <br></br><br></br>
     * Takes an interval of time in milliseconds, and returns amount of time it represents.
     * Useful for determining runtime of a program in a more readable format.
     *
     * @param interval milliseconds to convert to dd, hh, mm, ss, SSS format
     * @return time converted to readable format
     */
    public static String GetRunTime(long interval) {
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
     * ClearLine() void
     * <br></br><br></br>
     * Clears a line in the console of size line.length().
     *
     * @param line        line to be cleared
     * @param clearPrompt choose to clear prompt along with line (only use true if prompt exists)
     */
    public static void ClearLine(String line, boolean clearPrompt) {
        if (JTerm.isHeadless()) {
            for (int i = 0; i < line.length() + (clearPrompt ? JTerm.prompt.length() / 3 : 0); i++) {
                System.out.print("\b");
            }
            for (int i = 0; i < line.length() + (clearPrompt ? JTerm.prompt.length() / 3 : 0); i++) {
                System.out.print(" ");
            }
            for (int i = 0; i < line.length() + (clearPrompt ? JTerm.prompt.length() / 3 : 0); i++) {
                System.out.print("\b");
            }
        }else{
            JTerm.getTerminal().clearLine(line);
        }
    }

    public static void setOS() {
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("windows")) {
            JTerm.IS_WIN = true;
        } else if ("linux".equals(os) || os.contains("mac") || "sunos".equals(os) || "freebsd".equals(os)) {
            JTerm.IS_UNIX = true;
        }
    }
}
