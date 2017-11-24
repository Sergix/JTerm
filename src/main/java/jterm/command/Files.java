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
import jterm.io.output.TextColor;
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
    @Command(name = {"mv", "move"}, minOptions = 2, syntax = "move, mv [-h] source destination")
    public static void move(List<String> options) {
        String sourceName = Util.getFullPath(options.get(0));
        String destinationName = Util.getFullPath(options.get(1));

        Path source = Paths.get(sourceName);
        Path destination = Paths.get(destinationName);

        try {
            java.nio.file.Files.move(source, destination);
        } catch (IOException e) {
            throw new CommandException(String.format("Failed to move '%s' to '%s'", sourceName, destinationName), e);
        }
    }

    @Command(name = "rn", minOptions = 2, syntax = "rn [-h] file newName")
    public static void rename(List<String> options) {
        Path filePath = Paths.get(Util.getFullPath(options.get(0)));

        try {
            java.nio.file.Files.move(filePath, filePath.resolveSibling(options.get(1)), REPLACE_EXISTING);
        } catch (IOException e) {
            throw new CommandException("Failed to rename file", e);
        }
    }

    @Command(name = "write", minOptions = 1, syntax = "write [-h] filename")
    public static void write(List<String> options) {
        StringBuilder filenameBuilder = new StringBuilder();
        for (String option : options) {
            if (option.equals("-h")) {
                JTerm.out.println(TextColor.INFO, "Command syntax:\n\twrite [-h] filename\n\nOpens an input PROMPT in which to write text to a new file.");
                return;
            } else {
                filenameBuilder.append(option);
            }
        }

        String fileName = String.format("%s%s", JTerm.currentDirectory, filenameBuilder.toString().trim());
        if (fileName.equals("")) {
            JTerm.out.println(TextColor.ERROR, "Error: missing filename; type \"write -h\" for more information.");
            return;
        }

        try (FileWriter fileWriter = new FileWriter(fileName)) {
            JTerm.out.println(TextColor.INFO, "Enter file contents (press enter after a blank line to quit):");
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

            fileWriter.write(output.toString());
        } catch (IOException ioe) {
            JTerm.out.println(TextColor.ERROR, ioe.toString());
        }

    }

    @Command(name = {"rm", "del", "delete"}, minOptions = 1, syntax = "rm, del, delete [-h] file")
    public static void delete(List<String> options) {
        String fileName = Util.getFullPath(options.get(0));

        try {
            java.nio.file.Files.delete(Paths.get(fileName));
        } catch (NoSuchFileException e) {
            throw new CommandException("File does not exist", e);
        } catch (IOException e) {
            throw new CommandException(String.format("Failed to delete '%s'", fileName), e);
        }
    }

    @Command(name = "read", minOptions = 1, syntax = "read [-h] [file1 file2 ...]")
    public static void read(List<String> options) {
        String fileName = Util.getFullPath(options.get(0));

        try {
            JTerm.out.println(TextColor.INFO, new String(java.nio.file.Files.readAllBytes(Paths.get(fileName))));
        } catch (IOException e) {
            throw new CommandException(String.format("Failed to read '%s' content", fileName));
        }
    }

    @Command(name = "download", minOptions = 1, syntax = "download [-h] url")
    public static void download(List<String> options) {
        String url;
        if (options.size() > 0) {
            url = options.get(options.size() - 1);
        } else {
            JTerm.out.println(TextColor.ERROR, "A URL to a file must be provided as an option");
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

        JTerm.out.printf(TextColor.INFO, "Starting download of file -> %s%n", fileName);

        // request file size from server (does not work with HTML files, unimportant because they download so fast)
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("GET");
            conn.getInputStream();
            fileSize = conn.getContentLength();
        } catch (IOException e) {
            JTerm.out.println(TextColor.ERROR, "Error when getting file information. Download cancelled.");
            return;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(JTerm.currentDirectory + "/" + fileName));
             BufferedInputStream in = new BufferedInputStream(new URL(url).openStream())) {

            int buffer = 1024;
            byte data[] = new byte[buffer];
            int count, steps = 0;
            // download file, and output information about progress
            JTerm.out.print(TextColor.INFO, update = (String.format("Download is: %s%% complete", ((double) downloadedBytes / (double) fileSize) * 100d)));
            while ((count = in.read(data, 0, buffer)) != -1) {
                out.write(data, 0, count);
                downloadedBytes += count;
                steps++;
                //TODO: Progress bar instead maybe?
                //Also, this causes flickering in the GUI, a lower update rate might be good
                if (steps % 10 == 0) { // print every 10 download steps
                    JTerm.out.clearLine(update, update.length(), false);
                    JTerm.out.print(TextColor.INFO, update = (String.format("Download is: %s%% complete", ((double) downloadedBytes / (double) fileSize) * 100d)));
                }
            }
        } catch (IOException e) {
            JTerm.out.println(TextColor.ERROR, "Error when downloading file.");
        }

        // clear line and notify user of download success
        JTerm.out.clearLine(update, update.length(), false);
        JTerm.out.printf(TextColor.INFO, "\nFile downloaded successfully in: %s%n", Util.getRunTime(System.currentTimeMillis() - start));
    }
}