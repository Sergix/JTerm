package main.java.com.jterm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Files {

	/*
	* WriteFile() void
	* 
	* Get input and write it to a file.
	* 
	* ArrayList<String> options - command options
	* 
	* -h
	*     Prints help information
	*/
	public static void WriteFile(ArrayList<String> options) {
		
		String filename = "";
		
		for (String option: options)
		{
			if (option.equals("-h"))
			{
				System.out.println("Command syntax:\n\twrite [-h] filename\n\nOpens an input prompt in which to write text to a new file.");
				return;
				
			}
			else
			{
				filename = JTerm.currentDirectory + option;
				
			}
			
		}
		
		try {
			System.out.println("Enter file contents:\n");
			BufferedReader inputStream = new BufferedReader(new InputStreamReader(System.in), 1);
			String output = inputStream.readLine();
			
			FileWriter fileWriter = new FileWriter(filename);
			fileWriter.write(output);
			fileWriter.close();
			
		}
		catch (IOException ioe)
		{
			System.out.println(ioe);
			
		}
		
	}
	
	/*
	* Delete() void
	* 
	* Delete the specified file or directory.
	* 
	* ArrayList<String> options - command options
	* 
	* -h
	*     Prints help information
	*/
	public static void Delete(ArrayList<String> options) {
		
		String filename = "";
		
		for (String option: options)
		{
			if (option.equals("-h"))
			{
				System.out.println("Command syntax:\n\tdel [-h] file/directory\n\nDeletes the specified file or directory.");
				return;
				
			}
			else
			{
				filename = JTerm.currentDirectory + option;
				
			}
			
		}
		
		File dir = new File(filename);
		if (!dir.exists()) {
			System.out.println("ERROR: File/directory \"" + options.get(options.size() - 1) + "\" does not exist.");
			return;
			  
		}
		
		dir.delete();
		
	}
	
}
