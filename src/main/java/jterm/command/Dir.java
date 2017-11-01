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

import java.io.File;
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

    public static void ls(List<String> options) {
        if (options.contains("-h")) {
            System.out.println("Command syntax:\n\tdir [-f] [-h] [directory]\n\n");
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


        System.out.println("[Contents of \"" + JTerm.currentDirectory + "\"]");
        for (File file : files) {
            printer.print(file);
        }
    }

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
}
