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

import jterm.JTerm;
import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static jterm.JTerm.logln;

/*
* Original code credit to @chromechris
* 
* (edits for release done by @Sergix)
*/
public class Ps {
    /*
    * Ps() void
    *
    * Prints a list of process running on
    * the system.
    *
    * ArrayList<String> options - command options
    *
    * -h
    * 	Prints help information
    */
    public Ps(ArrayList<String> options) {
        if (options.contains("-h")) {
            logln("Command syntax:\n\tps [-h]\n\nDisplays all current processes running on the host system.", false);
            return;
        }

        if (JTerm.IS_UNIX) {
            try {
                Process process = Runtime.getRuntime().exec("ps -e");
                BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
                logln(IOUtils.toString(input), true);
                input.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (JTerm.IS_WIN) {
            try {
                String line;
                Process p = Runtime.getRuntime().exec(System.getenv("windir") + "\\system32\\" + "tasklist.exe");
                BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
                //TODO: Also switch this to IOUtils.toString() from apache commons?
                while ((line = input.readLine()) != null) {
                    // Parse data here.
                    logln(line, true);
                }
                input.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}