package main.java.com.jterm;

import java.io.*;
import java.util.ArrayList;

public class Write {

	/*
	* WriteFile() void
	* 
	* Get input and write it to a file.
	* 
	* ArrayList<String> options - command options
	*/
	public static void WriteFile(ArrayList<String> options) {
		
		try {
			System.out.println("Enter file contents:\n");
			BufferedReader inputStream = new BufferedReader(new InputStreamReader(System.in), 1);
			String output = inputStream.readLine();
			
			FileWriter fileWriter = new FileWriter(options.get( options.size() ) );
			fileWriter.write(output);
			fileWriter.close();
			
		}
		catch (IOException ioe)
		{
			System.out.println(ioe);
			
		}
		
	}

}
