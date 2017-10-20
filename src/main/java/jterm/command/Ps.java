package jterm.command;

import org.apache.commons.lang3.SystemUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

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
            System.out.println("Command syntax:\n\tps [-h]\n\nDisplays all current processes running on the host system.");
            return;
        }

        // FIXME: use SystemUtils or JTerm.IS_UNIX constant?
        if (SystemUtils.IS_OS_LINUX) {
            try {
                Process process = Runtime.getRuntime().exec("ps -e");
                BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));

                // TODO: can be replaced with IOUtils.toString()  (from apache commons)
                String line;
                while ((line = input.readLine()) != null) {
                    System.out.println(line);
                }
                input.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (SystemUtils.IS_OS_WINDOWS) {
            try {
                String line;
                Process p = Runtime.getRuntime().exec(System.getenv("windir") + "\\system32\\" + "tasklist.exe");
                BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
                while ((line = input.readLine()) != null) {
                    // Parse data here.
                    System.out.println(line);
                }
                input.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}