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
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class Files {
    // @ojles and @Kaperskyguru
    @Command(name = {"mv", "move"}, minOptions = 2)
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

    @Command(name = "write", minOptions = 1, syntax = "write [-h] filename")
    public static void write(List<String> options) {
        StringBuilder filenameBuilder = new StringBuilder();
        for (String option : options) {
            if (option.equals("-h")) {
                JTerm.out.println("Command syntax:\n\twrite [-h] filename\n\nOpens an input PROMPT in which to write text to a new file.");
                return;
            } else {
                filenameBuilder.append(option);
            }
        }
        String filename = filenameBuilder.toString();

        filename = filename.trim();
        filename = JTerm.currentDirectory + filename;

        if (filename.equals("")) {
            JTerm.out.println("Error: missing filename; type \"write -h\" for more information.");
            return;
        }

        try {
            JTerm.out.println("Enter file contents (press enter after a blank line to quit):");
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
            JTerm.out.println(String.valueOf(ioe));
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
            JTerm.out.println(new String(data));
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
            JTerm.out.println("A URL to a file must be provided as an option");
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

        JTerm.out.println("Starting download of file -> " + fileName);

        // request file size from server (does not work with HTML files, unimportant because they download so fast)
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("GET");
            conn.getInputStream();
            fileSize = conn.getContentLength();
        } catch (IOException e) {
            JTerm.out.println("Error when getting file information. Download cancelled.");
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
            JTerm.out.print(update = ("Download is: " + (((double) downloadedBytes / (double) fileSize) * 100d) + "% complete"));
            while ((count = in.read(data, 0, buffer)) != -1) {
                out.write(data, 0, count);
                downloadedBytes += count;
                steps++;
                //TODO: Progress bar instead maybe?
                //Also, this causes flickering in the GUI, a lower update rate might be good
                if (steps % 10 == 0) { // print every 10 download steps
                    Util.clearLine(update, false);
                    JTerm.out.print(update = ("Download is: " + (((double) downloadedBytes / (double) fileSize) * 100d) + "% complete"));
                }
            }
            out.close();
        } catch (IOException e) {
            JTerm.out.println("Error when downloading file.");
        }

        // clear line and notify user of download success
        Util.clearLine(update, false);
        JTerm.out.println("\nFile downloaded successfully in: " + Util.getRunTime(System.currentTimeMillis() - start));
    }
}
