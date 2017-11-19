/*
* JTerm - a cross-platform terminal
* Copyright (C) 2017 Sergix, NCSGeek
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

package main.java.jterm.command;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import main.java.jterm.JTerm;

public class Files
{

	/*
	* Files() void
	* 
	* Constructor for calling methods.
	*
	* ArrayList<String> options - command options
	*/
	public Files(ArrayList<String> options) { }
	
	/*
	* Process() void
	* 
	* Process the input.
	* 
	* String options - command options
	*/
	public static void Process(String options)
	{

		System.out.println("File Commands\n\nwrite\tdelete\ndel\trm\nread\thelp");

	}

	/*
	* Write() void
	* 
	* Get input and write it to a file.
	* Changelog (#65)
	* 
	* ArrayList<String> options - command options
	* 
	* -h
	*   Prints help information
	* filename [...]
	*	File to write to
	*/
	public static void Write(ArrayList<String> options)
	{
		
		String filename = "";
		
		for (String option: options)
		{
			if (option.equals("-h"))
			{
				System.out.println("Command syntax:\n\twrite [-h] filename\n\nOpens an input prompt in which to write text to a new file.");
				return;
				
			}
			else
				filename += option;
			
		}
		
		filename = filename.trim();
		filename = JTerm.currentDirectory + filename;
		
		if (filename.equals(""))
		{
			System.out.println("Error: missing filename; type \"write -h\" for more information.");
			return;
			
		}
		
		try
		{
			System.out.println("Enter file contents (press enter after a blank line to quit):");
			String line = JTerm.userInput.readLine();
			String output = line;
			
			for(;;)
			{
				line = JTerm.userInput.readLine();
				if (line.equals(""))
					break;
				
				else if (line.equals(" "))
					output += "\n";
				
				output += "\n" + line;
				
			}
			
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
	*   Prints help information
	* file [...]
	* 	File to delete
	*/
	public static void Delete(ArrayList<String> options)
	{
		
		String filename = "";
		
		for (String option: options)
		{
			if (option.equals("-h"))
			{
				System.out.println("Command syntax:\n\tdel [-h] file/directory\n\nDeletes the specified file or directory.");
				return;
				
			}
			else
				filename += option;
			
		}
		
		filename.trim();
		filename = JTerm.currentDirectory + filename;
		
		File dir = new File(filename);
		if (!dir.exists())
		{
			System.out.println("ERROR: File/directory \"" + options.get(options.size() - 1) + "\" does not exist.");
			return;
			  
		}
		
		dir.delete();
		
	}

	/*
	* Rm() void (@pmorgan3)
	* 
	* Identical to 'delete'; calls Delete().
	*
	* ArrayList<String> options - command options
	*/
	public static void Rm(ArrayList<String> options)
	{
		
		Delete(options);

	}

	/*
	* Del() void (@pmorgan3)
	* 
	* Identical to 'delete'; calls Delete().
	*
	* ArrayList<String> options - command options
	*/
	public static void Del(ArrayList<String> options)
	{
		
		Delete(options);

	}

	/*
	* Read() void
	* Changelog (#68)
	* 
	* Reads the specified files and outputs the contents
	* to the console.
	* 
	* ArrayList<String> options - command options
	* 
	* -h
	*   Prints help information
	* filename [...]
	*	Prints the contents of the specified files
	*     
	* Credit to @d4nntheman
	*/
	public static void Read(ArrayList<String> options)
	{
		
		String filename = "";
		for (String option: options)
		{
			if (option.equals("-h"))
			{
				System.out.println("Command syntax:\n\t read [-h] [file1 file2 ...]\n\nReads and outputs the contents of the specified files.");
				return;
				
			}
	
			filename = JTerm.currentDirectory + option;
			File file = new File(filename);
			if (!file.exists())
			{
			    System.out.println("ERROR: File/directory \"" + option + "\" does not exist.");
			    break;
			    
			}
		
			try ( BufferedReader reader = new BufferedReader(new FileReader(file.getAbsolutePath())) )
	        {
				System.out.println("\n[JTerm - Contents of " + option + "]\n");
	            String line = null;
	            while( (line = reader.readLine()) != null )
	                System.out.println(line);
	            
	        }
			catch (IOException e)
			{
	            e.printStackTrace();
	            return;
	            
	        }
			
	    }
		
	}
	
	@Command(name = {"move", "mv"}, minOptions = 2)
	public static void moveFile(File fileName, String newLocation){
		if(fileName.renameTo(new File(newLocation + fileName.getName()))){
			System.out.println(fileName + "Successfully Moved");
		}else{
			System.out.println(fileName + "Failed to Moved");
		}
		
	}
	
}
