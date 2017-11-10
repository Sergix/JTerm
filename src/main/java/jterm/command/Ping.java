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
import java.util.List;

import static jterm.JTerm.logln;


public class Ping implements Command {
    @Override
    public void execute(List<String> options) {
        if (options.size() == 0 || options.contains("-h")) {
            logln("Command syntax:\n\tping [-h] [-p port] host", false);
            return;
        }

        String port = "80";
        int portIndex = options.indexOf("-p");
        if (portIndex != -1) {
            if ((options.size() != 3) || (portIndex + 1 == options.size())) {
                throw new CommandException("Invalid ping usage");
            } else {
                port = options.get(portIndex + 1);
                options.remove("-p");
                options.remove(port);
            }
        }

        String host = options.get(options.size() - 1);
        try (Socket socket = new Socket()) {
            logln("Pinging " + host + "...", true);
            socket.connect(new InetSocketAddress(host, Integer.parseInt(port)), 3000);
            logln("Ping Successful", true);
        } catch (IOException e) {
            throw new CommandException("Ping failed", e);
        } catch (NumberFormatException e) {
            throw new CommandException("Invalid port value", e);
        }
    }
}
