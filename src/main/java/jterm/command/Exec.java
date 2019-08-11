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

import java.io.*;
import java.util.List;

public class Exec {

    @Command(name = "exec", minOptions = 1, syntax = "exec executable")
    public static void execute(final List<String> options) {
        String command = options.get(0);
        if (!command.startsWith("java") && command.endsWith(".jar")) {
            command = "java -jar " + command;
        }

        run(command);
    }

    public static void run(final String command) {
        final ProcessBuilder pb;
        final Process p;
        try {
            pb = new ProcessBuilder(command.split(" "));
            pb.redirectInput(ProcessBuilder.Redirect.PIPE);
            pb.redirectOutput(ProcessBuilder.Redirect.PIPE);
            pb.redirectError(ProcessBuilder.Redirect.PIPE);
            pb.directory(new File(JTerm.currentDirectory)); // Set working directory for command
            p = pb.start();
            p.waitFor();

            if (p.exitValue() == 0)
                JTerm.out.println(TextColor.INFO, readInputStream(p.getInputStream()));
            else
                JTerm.out.println(TextColor.ERROR, readInputStream(p.getErrorStream()));
            p.destroy();
        } catch (IOException | IllegalArgumentException | InterruptedException e) {
            System.err.println("Parsing command \"" + command + "\" failed, enter \"help\" for help using JTerm.");
        }
    }

    private static String readInputStream(final InputStream inputStream) throws IOException {
        final BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        final StringBuilder responseBuffer = new StringBuilder();

        String line;
        while ((line = in.readLine()) != null)
            responseBuffer.append(line).append("\n");
        return responseBuffer.toString();
    }
}