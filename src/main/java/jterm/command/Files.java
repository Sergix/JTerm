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

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import jterm.JTerm;
import jterm.util.Util;

public class Files {

    /**
     * Files() void
     * <br></br><br></br>
     * Constructor for calling methods.
     *
     * @param options command options
     */
    public Files(ArrayList<String> options) {
    }

    /**
     * Process() void
     * <br></br><br></br>
     * Process the input.
     * <br></br>
     * @param options command options
     */
    public static void process(String options) {

        System.out.println("File Commands\n\nwrite\tdelete\ndel\trm\nread\thelp");

    }

    /**
     * Write() void
     * <br></br><br></br>
     * Get input and write it to a file.
     * <br></br>
     * @param options command options
     */
    /*
     * Options:
	 * -h: Prints help information
	 * filename [...]: File to write to
	 */
    public static void write(ArrayList<String> options) {

        StringBuilder filenameBuilder = new StringBuilder();
        for (String option : options) {
            if (option.equals("-h")) {
                System.out.println("Command syntax:\n\twrite [-h] filename\n\nOpens an input prompt in which to write text to a new file.");
                return;

            } else
                filenameBuilder.append(option);

        }
        String filename = filenameBuilder.toString();

        filename = filename.trim();
        filename = JTerm.currentDirectory + filename;

        if (filename.equals("")) {
            System.out.println("Error: missing filename; type \"write -h\" for more information.");
            return;

        }

        try {
            System.out.println("Enter file contents (press enter after a blank line to quit):");
            String line = JTerm.userInput.readLine();
            StringBuilder output = new StringBuilder(line);

            for (; ; ) {
                line = JTerm.userInput.readLine();
                if (line.equals(""))
                    break;

                else if (line.equals(" "))
                    output.append("\n");

                output.append("\n").append(line);

            }

            FileWriter fileWriter = new FileWriter(filename);
            fileWriter.write(output.toString());
            fileWriter.close();

        } catch (IOException ioe) {
            System.out.println(ioe);

        }

    }

    /**
     * Delete() void
     * <br></br><br></br>
     * Delete the specified file or directory.
     * <br></br>
     * @param options command options
     */
    /*
	 * Options:
	 * -h: Prints help information
	 * file [...]: File to delete
	 */
    public static void delete(ArrayList<String> options) {

        StringBuilder filenameBuilder = new StringBuilder();
        for (String option : options) {
            if (option.equals("-h")) {
                System.out.println("Command syntax:\n\tdel [-h] file/directory\n\nDeletes the specified file or directory.");
                return;

            } else
                filenameBuilder.append(option);

        }
        String filename = filenameBuilder.toString();

        filename = filename.trim();
        filename = JTerm.currentDirectory + filename;

        File dir = new File(filename);
        if (!dir.exists()) {
            System.out.println("ERROR: File/directory \"" + options.get(options.size() - 1) + "\" does not exist.");
            return;

        }

        dir.delete();
    }

    /**
     * Rm() void
     * <br></br><br></br>
     * Identical to 'delete'; calls Delete().
     * <br></br><br></br>
     * Credit to: @pmorgan3
     * <br></br>
     * @param options command options
     */
    public static void rm(ArrayList<String> options) {
        delete(options);
    }

    /**
     * Del() void
     * <br></br><br></br>
     * Identical to 'delete'; calls Delete().
     * <br></br><br></br>
     * Credit to: @pmorgan3
     * <br></br>
     * @param options command options
     */
    public static void del(ArrayList<String> options) {
        delete(options);
    }

    /**
     * Read() void
     * <br></br><br></br>
     * Reads the specified files and outputs the contents
     * to the console.
     * <br></br><br></br>
     * Credit to @d4nntheman
     * <br></br>
     * @param options command options
     */
	/*
	 * Options:
	 * -h: Prints help information
	 * filename [...]: Prints the contents of the specified files
	 */
    public static void read(ArrayList<String> options) {

        String filename;
        for (String option : options) {
            if (option.equals("-h")) {
                System.out.println("Command syntax:\n\t read [-h] [file1 file2 ...]\n\nReads and outputs the contents of the specified files.");
                return;

            }

            filename = JTerm.currentDirectory + option;
            File file = new File(filename);
            if (!file.exists()) {
                System.out.println("ERROR: File/directory \"" + option + "\" does not exist.");
                break;

            }

            try (BufferedReader reader = new BufferedReader(new FileReader(file.getAbsolutePath()))) {
                System.out.println("\n[JTerm - Contents of " + option + "]\n");
                String line;
                while ((line = reader.readLine()) != null)
                    System.out.println(line);

            } catch (IOException e) {
                e.printStackTrace();
                return;

            }

        }

    }

    /**
     * Download() void
     * <br></br><br></br>
     * Downloads a file to the working directory, given a valid URL.
     * <br></br>
     * @param options command options, must include URL
     */
    public static void download(ArrayList<String> options) {
        String url;
        if (options.size() > 0)
            url = options.get(options.size() - 1);
        else {
            System.out.println("A URL to a file must be provided as an option");
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
            if (conn != null)
                conn.disconnect();
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
                    Util.ClearLine(update, false);
                    System.out.print(update = ("Download is: " + (((double) downloadedBytes / (double) fileSize) * 100d) + "% complete"));
                }
            }
        } catch (IOException e) {
            System.out.println("Error when downloading file.");
        }

        // clear line and notify user of download success
        Util.ClearLine(update, false);
        System.out.println("\nFile downloaded successfully in: " + Util.GetRunTime(System.currentTimeMillis() - start));
    }
}