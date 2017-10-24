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

import java.io.File;
import java.util.ArrayList;

// not doing it now, because they are not invoked directly
public class Dir {
    public Dir(ArrayList<String> options) {
    }

    public static void process(ArrayList<String> options) {
        System.out.println("Directory Commands\n\nls\tcd\nchdir\tpwd\nmd");
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
    public static void ls(ArrayList<String> options) {
        System.out.println(options);

        String path = JTerm.currentDirectory;
        boolean printFull = true;

        for (String option : options) {
            if (option.equals("-f")) {
                printFull = false;
            } else if (option.equals("-h")) {
                System.out.println("Command syntax:\n\tdir [-f] [-h] [directory]"
                        + "\n\nPrints a detailed table of the current working directory's subfolders and files.");
                return;
            } else {
                path = option;
            }
        }

        File[] files = new File(path).listFiles();

		/*
        * Format of output:
		* [FD] [RWHE] [filename] [size in KB]
		* 
		* Prefix definitions:
		* 	F -- File
		* 	D -- Directory
		* 	R -- Readable
		* 	W -- Writable
		* 	H -- Hidden
		* 
		* Example:
		* 	F RW	myfile.txt	   5 KB
		*/
        System.out.println("[Contents of \"" + path + "\"]");
        for (File file : files) {
            if (printFull) {
                System.out.println("\t" + (file.isFile() ? "F " : "D ")
                        + (file.canRead() ? "R" : "")
                        + (file.canWrite() ? "W" : "")
                        + (file.isHidden() ? "H" : "")
                        + "\t" + file.getName()
                        + (file.getName().length() < 8 ? "\t\t\t" : (file.getName().length() > 15 ? "\t" : "\t\t"))
                        + (file.length() / 1024) + " KB");
            } else {
                System.out.println("\t" + file.getName());
            }
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
    public static void cd(ArrayList<String> options) {
        String newDirectory = "";
        for (String option : options) {
            if (option.equals("-h")) {
                System.out.println("Command syntax:\n\tcd [-h] directory\n\nChanges the working directory to the path specified.");
                return;
            } else {
                newDirectory += option + " ";
            }
        }

        newDirectory = newDirectory.trim();
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
            System.out.println("ERROR: Directory \"" + newDirectory + "\" is either does not exist or is not a valid directory.");
            return;
        }

        if (!newDirectory.endsWith("/")) {
            newDirectory += "/";
        }

        // It does exist, and it is a directory, so just change the global working directory variable to the input
        JTerm.currentDirectory = newDirectory;
    }

    /*
    * chdir() void
    *
    * Identical to 'cd'; calls cd().
    *
    * ArrayList<String> options - command options
    */
    public static void chdir(ArrayList<String> options) {
        cd(options);
    }

    /*
    * pwd() void
    *
    * Prints the working directory to the console.
    *
    * ArrayList<String> options - command options
    *
    * -h
    * 	Prints help information
    */
    public static void pwd(ArrayList<String> options) {
        for (String option : options) {
            if (option.equals("-h")) {
                System.out.println("Command syntax:\n\tpwd\n\nPrints the current Working Directory.");
                return;
            }
        }
        System.out.println(JTerm.currentDirectory);
    }

    /*
    * md() void
    *
    * Creates a new directory.
    *
    * ArrayList<String> options - command options
    *
    * -h
    * 	Prints help information
    * name [...]
    *	Name of the new directory
    */
    public static void md(ArrayList<String> options) {
        StringBuilder nameBuilder = new StringBuilder();
        for (String option : options) {
            if (option.equals("-h")) {
                System.out.println("Command syntax:\n\tmd [-h] name\n\nCreates a new directory.");
                return;
            } else {
                nameBuilder
                        .append(option)
                        .append(" ");
            }
        }

        nameBuilder
                .deleteCharAt(nameBuilder.length() - 1)
                .insert(0, JTerm.currentDirectory);
        new File(nameBuilder.toString()).mkdir();
    }
}
