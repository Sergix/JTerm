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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Exec {
    /*
    * Exec() void
    *
    * Constructor for calling run() function.
    */
    public Exec(ArrayList<String> options) {
        run(options);
    }

    /*
    * run() boolean
    *
    * Runs the executable file.
    *
    * ArrayList<String> options - command options
    *
    * -h
    * 	Prints help information
    * file
    * 	File to execute
    */
    public static boolean run(ArrayList<String> options) {
        String file = "";
        for (String option : options) {
            if (option.equals("-h")) {
                System.out.println("Command syntax:\n\texec [-h] file\n\nExecutes a batch script.");
                return false;
            } else {
                file = option;
            }
        }

        File script = new File(file);
        if (!script.exists() || !script.isFile()) {
            file = JTerm.currentDirectory + file;
            script = new File(file);
            if (!script.exists() || !script.isFile()) {
                System.out.println("ERROR: File \"" + file + "\" either does not exist or is not an executable file.");
                return true;
            }
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(script))) {
            String directive = reader.readLine();
            if (directive != null) {
                do {
                    // Store the command as an ArrayList
                    ArrayList<String> commandOptions = new ArrayList<>();
                    try (Scanner tokenizer = new Scanner(directive)) {
                        while (tokenizer.hasNext()) {
                            options.add(tokenizer.next());
                        }
                    }
                    // Where the magic happens!
                    JTerm.parse(JTerm.getAsString(commandOptions));
                } while ((directive = reader.readLine()) != null);
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        return false;
    }

    public static String getRest(ArrayList<String> options, int index) {
        StringBuilder outputBuilder = new StringBuilder();
        for (int i = index; i < options.size(); i++) {
            if (i != options.size() - 1) {
                outputBuilder
                        .append(options.get(i))
                        .append(" ");
            } else {
                outputBuilder.append(options.get(i));
            }
        }
        return outputBuilder.toString();
    }
}