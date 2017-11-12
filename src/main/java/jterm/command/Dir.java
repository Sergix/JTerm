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
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Dir {
    private static final Consumer<File> SIMPLE_PRINTER = (file) -> System.out.println("\t" + file.getName());

    private static final Consumer<File> FULL_PRINTER = (file) -> System.out.println("\t"
            + (file.isFile() ? "F" : "D") + " "
            + (file.canRead() ? "R" : "")
            + (file.canWrite() ? "W" : "")
            + (file.isHidden() ? "H" : "")
            + "\t" + file.getName()
            + (file.getName().length() < 8 ? "\t\t\t" : (file.getName().length() > 15 ? "\t" : "\t\t"))
            + (file.length() / 1024) + " KB");

    @Command(name = "ls", syntax = "ls [-f] [-h] [directory]")
    public static void ls(List<String> options) {
        // FIXME: options not used to list directory
        File[] files = new File(JTerm.currentDirectory).listFiles();
        if (files == null) {
            return;
        }

        Consumer<File> printer = options.contains("-f") ? FULL_PRINTER : SIMPLE_PRINTER;

        System.out.println("[Contents of \"" + JTerm.currentDirectory + "\"]");
        for (File file : files) {
            printer.accept(file);
        }
    }

    // FIXME: throws exception if no options specified
    @Command(name = "cd", minOptions = 1, syntax = "cd [-h] directory")
    public static void cd(List<String> options) {
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
            if (JTerm.currentDirectory.equals("/")) {
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

    @Command(name = "pwd")
    public static void pwd(List<String> options) {
        System.out.println(JTerm.currentDirectory);
    }

    @Command(name = {"md", "mkdir"}, minOptions = 1, syntax = "md [-h] dirName")
    public static void md(List<String> options) {
        String dirName = options.get(0);
        if (!dirName.startsWith("/")) {
            dirName = JTerm.currentDirectory + "/" + dirName;
        }

        try {
            java.nio.file.Files.createDirectory(Paths.get(dirName));
        } catch (IOException e) {
            throw new CommandException("Failed to create directory \'" + dirName + '\'');
        }
    }

    @Command(name = "rmdir", minOptions = 1, syntax = "rm [-h] [-r] dirName")
    public static void rm(List<String> options) {
        List<String> filesToBeRemoved = new ArrayList<>();
        boolean recursivelyDeleteFlag = false;
        for (String option : options) {
            if (option.equals("-r")) {
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
