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

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.*;
import java.util.List;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import static jterm.JTerm.log;
import static jterm.JTerm.logln;

public class Files {
    private static final String renameHelp = "rename [-h] [-f] path name\n\tpath\tpath to file\n\tname\tnew name\n\t-h\tshow this message and exit\n\t-f\treplace existing";

    @Command(name = "mv", minOptions = 2)
    public static void move(List<String> options) {
        String sourceName = Util.getFullPath(options.get(0));
        String destinationName = Util.getFullPath(options.get(1));

        Path source = Paths.get(sourceName);
        Path destination = Paths.get(destinationName);

        try {
            java.nio.file.Files.move(source, destination);
        } catch (IOException e) {
            throw new CommandException("Failed to move \'" + sourceName + "\' to \'" + destinationName + '\'', e);
        }
    }

    @Command(name = "rn", minOptions = 2)
    public static void rename(List<String> options) {
        String fileName = Util.getFullPath(options.get(0));
        String newName = options.get(1);

        Path filePath = Paths.get(fileName);
        try {
            java.nio.file.Files.move(filePath, filePath.resolveSibling(newName), REPLACE_EXISTING);
        } catch (IOException e) {
            throw new CommandException("Failed to rename file", e);
        }
    }

    @Command(name = "rn2", minOptions = 2)
    public static void rename2(List<String> options) {
        boolean overwrite = false;
        int numArgs = 2, i = 0;
        String[] args = new String[numArgs];

        for (String option : options) {
            switch (option) {
                case "-f":
                    overwrite = true;
                    break;
                case "-h":
                    logln(renameHelp, true);
                    break;
                default:
                    if (i < numArgs) {
                        args[i++] = option;
                    } else {
                        logln(renameHelp, true);
                        return;
                    }
            }
        }

        if (i == numArgs) {
            try {
                String path = args[0];
                String newName = args[1];

                Path source = Paths.get(Util.getFullPath(path));
                Path target = source.resolveSibling(newName);

                if (overwrite) {
                    java.nio.file.Files.move(source, target, REPLACE_EXISTING);
                } else {
                    java.nio.file.Files.move(source, target);
                }
            } catch (FileAlreadyExistsException e) {
                logln("File with this name already exists. Use -f to replace existing.", true);
                throw new CommandException("File with this name already exists. Use -f to replace existing.", e);
            } catch (NoSuchFileException e) {
                logln("File with this path does not exist.", true);
                throw new CommandException("File with this path does not exist.", e);
            } catch (IOException | SecurityException | InvalidPathException e) {
                logln("Cannot rename file.", true);
                throw new CommandException("Cannot rename file.", e);
            }
        } else {
            System.out.println(renameHelp);
        }
    }

    @Command(name = "write", minOptions = 1, syntax = "write [-h] filename")
    public static void write(List<String> options) {
        StringBuilder filenameBuilder = new StringBuilder();
        for (String option : options) {
            if (option.equals("-h")) {
                logln("Command syntax:\n\twrite [-h] filename\n\nOpens an input PROMPT in which to write text to a new file.", true);
                return;
            } else {
                filenameBuilder.append(option);
            }
        }
        String filename = filenameBuilder.toString();

        filename = filename.trim();
        filename = JTerm.currentDirectory + filename;

        if (filename.equals("")) {
            logln("Error: missing filename; type \"write -h\" for more information.", false);
            return;
        }

        try {
            logln("Enter file contents (press enter after a blank line to quit):", false);
            String line = JTerm.userInput.readLine();
            StringBuilder output = new StringBuilder(line);

            for (; ; ) {
                line = JTerm.userInput.readLine();
                if (line.equals("")) {
                    break;
                } else if (line.equals(" ")) {
                    output.append("\n");
                }
                output.append("\n").append(line);
            }

            FileWriter fileWriter = new FileWriter(filename);
            fileWriter.write(output.toString());
            fileWriter.close();
        } catch (IOException ioe) {
            System.out.println(ioe);
        }

    }

    @Command(name = {"rm", "del", "delete"}, minOptions = 1)
    public static void delete(List<String> options) {
        String fileName = Util.getFullPath(options.get(0));
        try {
            java.nio.file.Files.delete(Paths.get(fileName));
        } catch (NoSuchFileException e) {
            throw new CommandException("File does not exist", e);
        } catch (IOException e) {
            throw new CommandException("Failed to delete \'" + fileName + "\'", e);
        }
    }

    @Command(name = "read", minOptions = 1, syntax = "read [-h] [file1 file2 ...]")
    public static void read(List<String> options) {
        String fileName = Util.getFullPath(options.get(0));
        try {
            byte[] data = java.nio.file.Files.readAllBytes(Paths.get(fileName));
            logln(new String(data), true);
        } catch (IOException e) {
            throw new CommandException("Failed to read \'" + fileName + "\' content");
        }
    }

    @Command(name = "download", minOptions = 1)
    public static void download(List<String> options) {
        String url;
        if (options.size() > 0) {
            url = options.get(options.size() - 1);
        } else {
            logln("A URL to a file must be provided as an option", false);
            return;
        }
        long start = System.currentTimeMillis();
        long fileSize;
        long downloadedBytes = 0;

        String update = "";

        // split url provided to find file name (will be last element if split by "/" char)
        String[] split = url.split("/");
        String fileName = split[split.length - 1];
        // if no file extension, assume HTML
        if (!split[split.length - 1].contains(".")) {
            url += ".html";
            split[split.length - 1] += ".html";
            fileName += ".html";
        }

        logln("Starting download of file -> " + fileName, true);

        // request file size from server (does not work with HTML files, unimportant because they download so fast)
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("GET");
            conn.getInputStream();
            fileSize = conn.getContentLength();
        } catch (IOException e) {
            logln("Error when getting file information. Download cancelled.", false);
            return;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        BufferedOutputStream out;
        BufferedInputStream in;
        try {
            // output file
            out = new BufferedOutputStream(new FileOutputStream(JTerm.currentDirectory + "/" + fileName));
            // input from server
            in = new BufferedInputStream(new URL(url).openStream());

            int buffer = 1024;
            byte data[] = new byte[buffer];
            int count, steps = 0;
            // download file, and output information about progress
            log(update = ("Download is: " + (((double) downloadedBytes / (double) fileSize) * 100d) + "% complete"), true);
            while ((count = in.read(data, 0, buffer)) != -1) {
                out.write(data, 0, count);
                downloadedBytes += count;
                steps++;
                //TODO: Progress bar instead maybe?
                //Also, this causes flickering in the GUI, a lower update rate might be good
                if (steps % 10 == 0) { // print every 10 download steps
                    Util.clearLine(update, false);
                    log(update = ("Download is: " + (((double) downloadedBytes / (double) fileSize) * 100d) + "% complete"), true);
                }
            }
            out.close();
        } catch (IOException e) {
            logln("Error when downloading file.", false);
        }

        // clear line and notify user of download success
        Util.clearLine(update, false);
        logln("\nFile downloaded successfully in: " + Util.getRunTime(System.currentTimeMillis() - start), true);
    }

    @Command(name = {"move", "mv"}, minOptions = 2)
    public static void moveFile(File fileName, String newLocation) {
        if (fileName.renameTo(new File(newLocation + fileName.getName()))) {
            System.out.println(fileName + "Successfully Moved");
        } else {
            System.out.println(fileName + "Failed to Moved");
        }

    }

}
