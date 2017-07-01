package main.java.jterm;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.commons.lang3.SystemUtils;

public class Ps {

	public static void main(String[] args) {
		process();
	}

	public static void process() {

		if (SystemUtils.IS_OS_LINUX) {
			try {
				String line;
				Process p = Runtime.getRuntime().exec("ps -e");
				BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
				while ((line = input.readLine()) != null) {
					System.out.println(line); // <-- Parse data here.
				}
				input.close();
			} catch (Exception err) {
				err.printStackTrace();
			}

		} else if (SystemUtils.IS_OS_WINDOWS) {
			try {
				String line;
				Process p = Runtime.getRuntime().exec(System.getenv("windir") + "\\system32\\" + "tasklist.exe");
				BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
				while ((line = input.readLine()) != null) {
					System.out.println(line); // <-- Parse data here.
				}
				input.close();
			} catch (Exception err) {
				err.printStackTrace();
			}
		}

	}

}
