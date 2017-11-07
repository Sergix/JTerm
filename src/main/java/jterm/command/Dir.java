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
import java.io.IOException;
import java.util.ArrayList;
import java.lang.String;

import org.apache.commons.io.FileUtils;

import static jterm.JTerm.logln;

// not doing it now, because they are not invoked directly
public class Dir {
    public Dir(ArrayList<String> options) {
    }

    public static void process(ArrayList<String> options) {
        logln("Directory Commands\n\nls\tcd\nchdir\tpwd\nmd\trm", false);
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
        String path = JTerm.currentDirectory;
        boolean printFull = true;

        for (String option : options) {
            switch (option) {
                case "-f":
                    printFull = false;
                    break;
                case "-h":
                    logln("Command syntax:\n\tdir [-f] [-h] [directory]"
                            + "\n\nPrints a detailed table of the current working directory's subfolders and files.", false);
                    return;
                default:
                    path = option;
                    break;
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
        logln("[Contents of \"" + path + "\"]", true);
        for (File file : files) {
            if (printFull) {
               logln("\t" + (file.isFile() ? "F " : "D ")
                        + (file.canRead() ? "R" : "")
                        + (file.canWrite() ? "W" : "")
                        + (file.isHidden() ? "H" : "")
                        + "\t" + file.getName()
                        + (file.getName().length() < 8 ? "\t\t\t" : (file.getName().length() > 15 ? "\t" : "\t\t"))
                        + (file.length() / 1024) + " KB", true);
            } else {
                logln("\t" + file.getName(), true);
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
        StringBuilder newDirectoryBuilder = new StringBuilder();
        for (String option : options) {
            if (option.equals("-h")) {
                logln("Command syntax:\n\tcd [-h] directory\n\nChanges the working directory to the path specified.", false);
                return;
            } else {
                newDirectoryBuilder.append(option);
            }
        }
        String newDirectory = newDirectoryBuilder.toString();

        newDirectory = newDirectory.trim();
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
            if(JTerm.currentDirectory.equals("/")) {
                return;
            } else {
                //TODO: Fix this to actually remove a directory level
                newDirectory = JTerm.currentDirectory.substring(0, JTerm.currentDirectory.length() - 2);
                newDirectory = newDirectory.substring(0, newDirectory.lastIndexOf('/'));
            }
        } else if (newDir.exists() && newDir.isDirectory()) {
            newDirectory = JTerm.currentDirectory + newDirectory;
        } else if ((!dir.exists() || !dir.isDirectory()) && (!newDir.exists() || !newDir.isDirectory())) {
            logln("ERROR: Directory \"" + newDirectory + "\" is either does not exist or is not a valid directory.", false);
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
                logln("Command syntax:\n\tpwd\n\nPrints the current Working Directory.", false);
                return;
            }
        }
        logln(JTerm.currentDirectory, true);
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
                logln("Command syntax:\n\tmd [-h] name\n\nCreates a new directory.", false);
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
    public static void rm(ArrayList<String> options) {
        ArrayList<String> filesToBeRemoved = new ArrayList<>();
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
