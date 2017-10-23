/*
* JTerm - a cross-platform terminal
* Copyright (code) 2017 Sergix, NCSGeek
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

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;

/*
* Original code credit to @chromechris
* 
* (edits for release done by @Sergix)
*/
public class Ping {
    /*
    * Ping() void
    *
    * Pings the specified host.
    *
    * ArrayList<String> options - command options

    * -h
    * 	Prints help information
    * host
    * 	Host to ping
    * -p port
    *	Port to ping the host on
    */
    // FIXME: ping is failing when no options are set
    public Ping(ArrayList<String> options) {
        String host = "google.com";
        String port = "80";
        boolean portNext = false;

        for (String option : options) {
            if (option.equals("-h")) {
                System.out.println("Command syntax:\n\tping [-h] [-p port] host\n\n"
                        + "Attempts to connect to the specified host. Default port is '80'.");
                return;
            } else if (portNext) {
                port = option;
                portNext = false;
            } else if (option.equals("-p")) {
                portNext = true;
            } else {
                host = option;
            }
        }

        // FIXME: if no options set, host = "process" !!!
        try (Socket socket = new Socket()) {
            System.out.println("Pinging " + host + "...");
            socket.connect(new InetSocketAddress(host, Integer.parseInt(port)), 10000);
            System.out.println("Ping Successful");
        } catch (IOException e) {
            // Either timeout or unreachable or failed DNS lookup
            System.out.println("Ping Failed");
        }
    }
}
