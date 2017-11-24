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

package jterm;

import jterm.io.output.TextColor;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Client implements Runnable {
    private static BufferedReader input;

    public void run() {
        while (true) {
            try {
                String output = Client.input.readLine();
                if (output != null) {
                    JTerm.out.println(TextColor.INFO, output);
                }
            } catch (IOException e) {
                return;
            }
        }
    }

    public static void connect(ArrayList<String> options) {
        String address = "0.0.0.0";
        String portInput = "80";
        boolean next = false;

        for (String option : options) {
            if (option.equals("-h")) {
                JTerm.out.println(TextColor.INFO, "Command syntax:\n\tconnect [-h] [-p port] address\n\n"
                        + "connect to the specified IP address using TCP/IP. "
                        + "Default address is \"0.0.0.0\". Default port is 80.");
                return;
            } else if (option.equals("-p")) {
                next = true;
            } else if (next) {
                portInput = option;
                next = false;
            } else {
                address = option;
            }
        }

        int port = Integer.valueOf(portInput);
        try (Socket connection = new Socket(address, port); InputStream input = connection.getInputStream(); OutputStream output = connection.getOutputStream();
             BufferedReader bufferedSocketOutput = new BufferedReader(new InputStreamReader(System.in), 1)) {

            JTerm.out.printf(TextColor.INFO, "Connecting to %s:%d%n", address, port);

            Client.input = new BufferedReader(new InputStreamReader(input));

            Client client = new Client();
            Thread readThread = new Thread(client);
            readThread.start();

            JTerm.out.println(TextColor.INFO, "Connected to server. Enter a blank line to quit. Reading for input...");

            String line;
            while ((line = bufferedSocketOutput.readLine()) != null && !line.equals("")) {
                output.write(line.getBytes());
            }
        } catch (IOException e) {
            JTerm.out.println(TextColor.ERROR, "Connection severed.");
        }
    }
}
