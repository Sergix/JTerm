package main.java.com.jterm;

import java.io.*;

public class Write {

	public static void WriteFile(String filename) {
		try {
			
			System.out.println("Enter file contents:\n");
			BufferedReader inputStream = new BufferedReader(new InputStreamReader(System.in), 1);
			
			String output   = inputStream.readLine();
			
			FileWriter fileWriter = new FileWriter(filename);
			fileWriter.write(output);
			fileWriter.close();
			
		}
		catch (IOException ioe)
		{
			
			System.out.println(ioe);
			
		}
	}

}
