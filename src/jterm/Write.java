package jterm;

import java.io.*;

public class Write {

	public static void WriteFile(String fileName) {
		try {			
			BufferedReader outputStream = new BufferedReader(new InputStreamReader(System.in), 1);
			String output = outputStream.readLine();
			outputStream.close();
			
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
