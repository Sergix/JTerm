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

package main.java.jterm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Files
{

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
	public static void WriteFile(ArrayList<String> options)
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
				filename = JTerm.currentDirectory + option;
			
		}
		
		try
		{
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
			{
				filename = JTerm.currentDirectory + option;
				
			}
			
		}
		
		File dir = new File(filename);
		if (!dir.exists())
		{
			System.out.println("ERROR: File/directory \"" + options.get(options.size() - 1) + "\" does not exist.");
			return;
			  
		}
		
		dir.delete();
		
	}

	/*
	* ReadFile() void
	* 
    * Reads the specified files
	* 
	* ArrayList<String> options - command options
	* 
	* -h
	*     Prints help information
	*/
    public static void ReadFile(ArrayList<String> options)
    {
        String filename = "";
        for (String option: options)
        {
            if (option.equals("-h"))
            {
                System.out.println("Command syntax:\n\t read [-h] [FILE]...");
                return;
            }

			filename = JTerm.currentDirectory + option;
            File file = new File(filename);
		    if (!file.exists())
		    {
			    System.out.println("ERROR: File/directory \"" + option + "\" does not exist.");
                break;
		    }

			try (BufferedReader reader = new BufferedReader(new FileReader(file.getAbsolutePath())))
            {
                String line = null;
                while((line = reader.readLine()) != null)
                    System.out.println(line);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }
	
}
