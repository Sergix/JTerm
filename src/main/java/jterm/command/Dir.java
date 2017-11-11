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

public class Dir implements Command {
    @FunctionalInterface
    private interface FilePrinter {
        void print(File file);
    }

    private static final Map<String, Consumer<List<String>>> FUNCTIONS = new HashMap<>(5);

    private static final FilePrinter SIMPLE_PRINTER = (file) -> System.out.println("\t" + file.getName());

    private static final FilePrinter FULL_PRINTER = (file) -> System.out.println("\t"
            + (file.isFile() ? "F" : "D") + " "
            + (file.canRead() ? "R" : "")
            + (file.canWrite() ? "W" : "")
            + (file.isHidden() ? "H" : "")
            + "\t" + file.getName()
            + (file.getName().length() < 8 ? "\t\t\t" : (file.getName().length() > 15 ? "\t" : "\t\t"))
            + (file.length() / 1024) + " KB");

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
            System.out.println("Available commands:");
            for (String command : FUNCTIONS.keySet()) {
                System.out.println("\t" + command);
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
            System.out.println("Command syntax:\n\tdir [-f] [-h] [directory]\n\n");
            return;
        }

        File[] files = new File(JTerm.currentDirectory).listFiles();

        if (files == null) {
            return;
        }

        FilePrinter printer = options.contains("-f") ? FULL_PRINTER : SIMPLE_PRINTER;

        System.out.println("[Contents of \"" + JTerm.currentDirectory + "\"]");
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
            System.out.println("Command syntax:\n\tcd [-h] directory\n\nChanges the working directory to the path specified.");
            return;
        }

        String newDirectory = Util.getAsString(options).trim();
        if (newDirectory.startsWith("\"") && newDirectory.endsWith("\"")) {
            newDirectory = newDirectory.substring(1, newDirectory.length() - 1);
        }

        if (newDirectory.equals("")) {
            System.out.println("Path not specified. Type \"cd -h\" for more information.");
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
            if(JTerm.currentDirectory.equals("/")) {
                return;
            } else {
                newDirectory = JTerm.currentDirectory.substring(0, JTerm.currentDirectory.length() - 2);
                newDirectory = newDirectory.substring(0, newDirectory.lastIndexOf('/'));
            }
        } else if (newDir.exists() && newDir.isDirectory()) {
            newDirectory = JTerm.currentDirectory + newDirectory;
        } else if ((!dir.exists() || !dir.isDirectory()) && (!newDir.exists() || !newDir.isDirectory())) {
            System.out.println("ERROR: Directory \"" + newDirectory + "\" either does not exist or is not a valid directory.");
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
            System.out.println("Command syntax:\n\tpwd\n\nPrints the current working directory.");
            return;
        }
        System.out.println(JTerm.currentDirectory);
    }

    public static void md(List<String> options) {
        if (options.contains("-h")) {
            System.out.println("Command syntax:\n\tmd [-h] name");
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
            if (option.equals("-h")) {
                System.out.println("Command syntax:\n\t rm [-h] [-r] name... Remove files or directories");
                return;
            } else if (option.equals("-r")) {
                recursivelyDeleteFlag = true;
            } else {
                filesToBeRemoved.add(option);
            }
        }

        for (String fileName : filesToBeRemoved) {
            File file = new File(JTerm.currentDirectory, fileName);

            if (!file.isFile() && !file.isDirectory()) {
                System.out.println(fileName + " is not a file or directory");
            } else if (file.isDirectory()) {
                if (recursivelyDeleteFlag) {
                    try {
                        FileUtils.deleteDirectory(file);
                    } catch (IOException e) {
                        System.out.println("Error when deleting " + fileName);
                    }
                } else {
                    System.out.println("Attempting to delete a directory. Run the command again with -r.");
                    return;
                }
            }

            file.delete();
        }
    }
}
