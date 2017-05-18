package jterm;

import java.io.*;

public class Write {

	public static void WriteFile() {
		try {
			System.out.println("New File Name: ");
			
			BufferedReader inputStream = new BufferedReader(new InputStreamReader(System.in), 1);
			
			String fileName = inputStream.readLine();
			String output   = inputStream.readLine();
			
			FileWriter fileWriter = new FileWriter(fileName);
			fileWriter.write(output);
			fileWriter.close();
		}
		catch (IOException ioe)
		{
			System.out.println(ioe);
		}
	}

}
