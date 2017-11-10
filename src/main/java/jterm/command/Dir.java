/*
* JTerm - a cross-platform terminal
* Copyright (C) 2017 Sergix, NCSGeek
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.

* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.

* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package jterm.command;

import jterm.JTerm;
import jterm.util.Util;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static jterm.JTerm.logln;

public class Dir implements Command {
    @FunctionalInterface
    private interface FilePrinter {
        void print(File file);
    }

    private static final Map<String, Consumer<List<String>>> FUNCTIONS = new HashMap<>(5);

    private static final FilePrinter SIMPLE_PRINTER = (file) -> logln("\t" + file.getName(), true);

    private static final FilePrinter FULL_PRINTER = (file) -> logln("\t"
            + (file.isFile() ? "F" : "D") + " "
            + (file.canRead() ? "R" : "")
            + (file.canWrite() ? "W" : "")
            + (file.isHidden() ? "H" : "")
            + "\t" + file.getName()
            + (file.getName().length() < 8 ? "\t\t\t" : (file.getName().length() > 15 ? "\t" : "\t\t"))
            + (file.length() / 1024) + " KB", true);

    static {
        FUNCTIONS.put("ls", Dir::ls);
        FUNCTIONS.put("cd", Dir::cd);
        FUNCTIONS.put("chdir", Dir::cd);
        FUNCTIONS.put("pwd", Dir::pwd);
        FUNCTIONS.put("md", Dir::md);
    }

    @Override
    public void execute(List<String> options) {
        if (options.size() == 0) {
            logln("Available commands:", true);
            for (String command : FUNCTIONS.keySet()) {
                logln("\t" + command, true);
            }
        } else if (FUNCTIONS.containsKey(options.get(0))) {
            FUNCTIONS.get(options.remove(0)).accept(options);
        } else {
            throw new CommandException("No command \"" + options.get(0) + "\" found");
        }
    }

    /*
    * ls() void (@pmorgan3)
    *
    * Prints the contents of a specified directory
    * to a file.
    *
    * ArrayList<String> options - command options
    *
    * -f
    * 	Changes the output format to only file
    * 	and directory names
    * -h
    * 	Prints help information
    * directory [...]
    * 	Prints this directory rather than the
    * 	current working directory.
    *
    * Examples
    *
    *   ls(options);
    *     => [Contents of "dir/"]
    *     =>     F RW 	myFile.txt		2 KB
    */
    public static void ls(List<String> options) {
        if (options.contains("-h")) {
            logln("Command syntax:\n\tdir [-f] [-h] [directory]\n\n", false);
            return;
        }
        File[] files = new File(JTerm.currentDirectory).listFiles();

        if (files == null) {
            return;
        }

        FilePrinter printer;
        if (options.contains("-f")) {
            printer = FULL_PRINTER;
        } else {
            printer = SIMPLE_PRINTER;
        }

        logln("[Contents of \"" + JTerm.currentDirectory + "\"]", true);
        for (File file : files) {
            printer.print(file);
        }
    }

    /*
    * cd() void
    *
    * Changes the working directory to the specified
    * input.
    *
    * ArrayList<String> options - command options
    *
    * -h
    * 	Prints help information
    * directory [...]
    * 	Path to change the working directory to.
    */

    public static void cd(List<String> options) {
        if (options.contains("-h")) {
            logln("Command syntax:\n\tcd [-h] directory\n\nChanges the working directory to the path specified.", false);
            return;
        }
        String newDirectory = Util.getAsString(options).trim();
        if (newDirectory.startsWith("\"") && newDirectory.endsWith("\"")) {
            newDirectory = newDirectory.substring(1, newDirectory.length() - 1);
        }

        if (newDirectory.equals("")) {
            logln("Path not specified. Type \"cd -h\" for more information.", false);
            return;
        }

        // Test if the input exists and if it is a directory
        File dir = new File(newDirectory);
        File newDir = new File(JTerm.currentDirectory + newDirectory);

        if (newDirectory.equals("/")) {
            newDirectory = "/";
        } else if (newDirectory.equals(".")) {
            return;
        } else if (newDirectory.equals("..")) {
            if (JTerm.currentDirectory.equals("/")) {
                return;
            } else {
                //TODO: Fix this to actually remove a directory level
                newDirectory = JTerm.currentDirectory.substring(0, JTerm.currentDirectory.length() - 2);
                newDirectory = newDirectory.substring(0, newDirectory.lastIndexOf('/'));
            }
        } else if (newDir.exists() && newDir.isDirectory()) {
            newDirectory = JTerm.currentDirectory + newDirectory;
        } else if ((!dir.exists() || !dir.isDirectory()) && (!newDir.exists() || !newDir.isDirectory())) {
            logln("ERROR: Directory \"" + newDirectory + "\" either does not exist or is not a valid directory.", false);
            return;
        }

        if (!newDirectory.endsWith("/")) {
            newDirectory += "/";
        }

        // It does exist, and it is a directory, so just change the global working directory variable to the input
        JTerm.currentDirectory = newDirectory;
    }

    public static void pwd(List<String> options) {
        if (options.contains("-h")) {
            logln("Command syntax:\n\tpwd\n\nPrints the current working directory.", false);
            return;
        }
        logln(JTerm.currentDirectory, true);
    }


    public static void md(List<String> options) {
        if (options.contains("-h")) {
            logln("Command syntax:\n\tmd [-h] name", false);
            return;
        }

        StringBuilder nameBuilder = new StringBuilder(Util.getAsString(options));
        nameBuilder
                .deleteCharAt(nameBuilder.length() - 1)
                .insert(0, JTerm.currentDirectory);
        new File(nameBuilder.toString()).mkdir();
    }

    /*
    * rm() void
    *
    * Removes a file or directory
    *
    * ArrayList<String> options - command options
    *
    * -h
    *   Prints help information
    *
    * -r
    *   Recursively remove directories
    *
    * name
    *   Names of files or directories to be removed
     */
    public static void rm(List<String> options) {
        List<String> filesToBeRemoved = new ArrayList<>();
        boolean recursivelyDeleteFlag = false;
        for (String option : options) {
            switch (option) {
                case "-h":
                    logln("Command syntax:\n\t rm [-h] [-r] name... Remove files or directories", false);
                    return;
                case "-r":
                    recursivelyDeleteFlag = true;
                    break;
                default:
                    filesToBeRemoved.add(option);
                    break;
            }
        }

        for (String fileName : filesToBeRemoved) {
            File file = new File(JTerm.currentDirectory, fileName);

            if (!file.isFile() && !file.isDirectory()) {
                logln(fileName + " is not a file or directory", false);
            } else if (file.isDirectory()) {
                if (recursivelyDeleteFlag) {
                    try {
                        FileUtils.deleteDirectory(file);
                    } catch (IOException e) {
                        logln("Error when deleting " + fileName, false);
                    }
                } else {
                    logln("Attempting to delete a directory. Run the command again with -r.", false);
                    return;
                }
            }

            file.delete();
        }
    }
}
