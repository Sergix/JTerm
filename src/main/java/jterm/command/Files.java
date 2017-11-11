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

import com.google.common.collect.ImmutableMap;
import jterm.JTerm;
import jterm.util.Util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class Files implements Command {
    private static class FilesCommand implements Consumer<List<String>> {
        private Consumer<List<String>> command;
        private String helpInfo;
        private int minOptionsSize;

        public FilesCommand(Consumer<List<String>> command, int minOptionsSize, String helpInfo) {
            this.command = command;
            this.minOptionsSize = minOptionsSize;
            this.helpInfo = helpInfo;
        }

        @Override
        public void accept(List<String> options) {
            if (options.contains("-h")) {
                System.out.println(helpInfo);
                return;
            }
            if (options.size() < minOptionsSize) {
                throw new CommandException("To few argumens for command");
            }
            command.accept(options);
        }
    }

    private static final Map<String, FilesCommand> SUB_COMMAND;

    static {
        SUB_COMMAND = ImmutableMap.<String, FilesCommand>builder()
            .put("write", new FilesCommand(Files::write, 1, ""))
            .put("delete", new FilesCommand(Files::delete, 1, ""))
            .put("rm", new FilesCommand(Files::delete, 1, ""))
            .put("del", new FilesCommand(Files::delete, 1, ""))
            .put("read", new FilesCommand(Files::read, 1, "Command syntax:\n\t read [-h] [file1 file2 ...]"))
            .put("download", new FilesCommand(Files::download, 1, ""))
            .put("mv", new FilesCommand(Files::move, 2, ""))
            .put("rn", new FilesCommand(Files::rename, 2, "")).build();
    }

    @Override
    public void execute(List<String> options) {
        if (options.size() == 0 || options.get(0).equals("-h")) {
            System.out.println("File Commands:");
            SUB_COMMAND
                    .keySet()
                    .forEach(k -> System.out.println("\t" + k));
            return;
        }

        String command = options.remove(0);
        if (SUB_COMMAND.containsKey(command)) {
            SUB_COMMAND.get(command).accept(options);
        } else {
            throw new CommandException("Invalid command name \"" + command + "\"");
        }
    }

    public static void move(List<String> options) {
        String sourceName = getFullName(options.get(0));
        String destinationName = getFullName(options.get(1));

        Path source = Paths.get(sourceName);
        Path destination = Paths.get(destinationName);

        try {
            java.nio.file.Files.move(source, destination);
        } catch (IOException e) {
            throw new CommandException("Failed to move \'" + sourceName + "\' to \'" + destinationName + '\'', e);
        }
    }

    public static void rename(List<String> options) {
        String fileName = getFullName(options.get(0));
        String newName = options.get(1);

        Path filePath = Paths.get(fileName);
        try {
            java.nio.file.Files.move(filePath, filePath.resolveSibling(newName), REPLACE_EXISTING);
        } catch (IOException e) {
            throw new CommandException("Failed to rename file", e);
        }
    }

    public static void write(List<String> options) {
        String filename = "";
        for (String option : options) {
            if (option.equals("-h")) {
                System.out.println("Command syntax:\n\twrite [-h] filename\n\nOpens an input PROMPT in which to write text to a new file.");
                return;
            } else {
                filename += option;
            }
        }

        filename = filename.trim();
        filename = JTerm.currentDirectory + filename;

        if (filename.equals("")) {
            System.out.println("Error: missing filename; type \"write -h\" for more information.");
            return;
        }

        try {
            System.out.println("Enter file contents (press enter after a blank line to quit):");
            String line = JTerm.userInput.readLine();
            String output = line;

            for (; ; ) {
                line = JTerm.userInput.readLine();
                if (line.equals("")) {
                    break;
                } else if (line.equals(" ")) {
                    output += "\n";
                }
                output += "\n" + line;
            }

            FileWriter fileWriter = new FileWriter(filename);
            fileWriter.write(output);
            fileWriter.close();
        } catch (IOException ioe) {
            System.out.println(ioe);
        }

    }

    public static void delete(List<String> options) {
        String fileName = getFullName(options.get(0));
        try {
            java.nio.file.Files.delete(Paths.get(fileName));
        } catch (NoSuchFileException e) {
            throw new CommandException("File does not exist", e);
        } catch (IOException e) {
            throw new CommandException("Failed to delete \'" + fileName + "\'", e);
        }
    }

    public static void read(List<String> options) {
        String fileName = getFullName(options.get(0));
        try {
            byte[] data = java.nio.file.Files.readAllBytes(Paths.get(fileName));
            System.out.println(new String(data));
        } catch (IOException e) {
            throw new CommandException("Failed to read \'" + fileName + "\' content");
        }
    }

    public static void download(List<String> options) {
        String url;
        if (options.size() > 0) {
            url = options.get(options.size() - 1);
        } else {
            System.out.println("A URL to a file must be provided as an option");
            return;
        }
        long start = System.currentTimeMillis();
        long fileSize = -1;
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

        System.out.println("Starting download of file -> " + fileName);

        // request file size from server (does not work with HTML files, unimportant because they download so fast)
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("GET");
            conn.getInputStream();
            fileSize = conn.getContentLength();
        } catch (IOException e) {
            System.out.println("Error when getting file information. Download cancelled.");
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
            while ((count = in.read(data, 0, buffer)) != -1) {
                out.write(data, 0, count);
                downloadedBytes += count;
                steps++;
                if (steps % 10 == 0) { // print every 10 download steps
                    Util.clearLine(update, false);
                    System.out.print(update = ("Download is: " + (((double) downloadedBytes / (double) fileSize) * 100d) + "% complete"));
                }
            }
        } catch (IOException e) {
            System.out.println("Error when downloading file.");
        }

        // clear line and notify user of download success
        Util.clearLine(update, false);
        System.out.println("\nFile downloaded successfully in: " + Util.getRunTime(System.currentTimeMillis() - start));
    }

    private static String getFullName(String fileName) {
        if (!fileName.startsWith("/")) {
            fileName = JTerm.currentDirectory + "/" + fileName;
        }
        return fileName;
    }
}