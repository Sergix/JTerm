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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static jterm.JTerm.log;
import static jterm.JTerm.logln;


public class Files implements Command {
    private static final Map<String, Consumer<List<String>>> FUNCTIONS = new HashMap<>(6);

    static {
        FUNCTIONS.put("write", Files::write);
        FUNCTIONS.put("delete", Files::delete);
        FUNCTIONS.put("rm", Files::delete);
        FUNCTIONS.put("del", Files::delete);
        FUNCTIONS.put("read", Files::read);
        FUNCTIONS.put("download", Files::download);
    }


    @Override
    public void execute(List<String> options) {
        if (options.contains("-h") || options.size() == 0) {
            logln("File Commands\n\nwrite\tdelete\ndel\trm\nread\thelp", false);
            return;
        }

        String command = options.get(0);
        options.remove(0);
        if (FUNCTIONS.containsKey(command)) {
            FUNCTIONS.get(command).accept(options);
        } else {
            throw new CommandException("Invalid command name \"" + command + "\"");
        }
    }


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


    public static void delete(List<String> options) {
        StringBuilder filenameBuilder = new StringBuilder();
        for (String option : options) {
            if (option.equals("-h")) {
                logln("Command syntax:\n\tdel [-h] file/directory\n\nDeletes the specified file or directory.", false);
                return;
            } else {
                filenameBuilder.append(option);
            }
        }
        String filename = filenameBuilder.toString();

        filename = filename.trim();
        filename = JTerm.currentDirectory + filename;

        File dir = new File(filename);
        if (!dir.exists()) {
            logln("ERROR: File/directory \"" + options.get(options.size() - 1) + "\" does not exist.", false);
            return;
        }
        //TODO: Maybe use the result of .delete() to indicate delete succeeded
        dir.delete();
    }


    public static void read(List<String> options) {
        String filename;
        for (String option : options) {
            if (option.equals("-h")) {
                logln("Command syntax:\n\t read [-h] [file1 file2 ...]\n\nReads and outputs the contents of the specified files.", false);
                return;
            }

            filename = JTerm.currentDirectory + option;
            File file = new File(filename);
            if (!file.exists()) {
                logln("ERROR: File/directory \"" + option + "\" does not exist.", false);
                break;
            }

            try (BufferedReader reader = new BufferedReader(new FileReader(file.getAbsolutePath()))) {
                logln("\n[JTerm - Contents of " + option + "]\n", true);
                String line;
                while ((line = reader.readLine()) != null) {
                    logln(line, true);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }

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
}