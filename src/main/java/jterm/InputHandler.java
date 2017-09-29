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

/*
* Special thanks to @nanoandrew4 for this feature!
* https://github.com/Sergix/JTerm/issues/31
*/

package main.java.jterm;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

public class InputHandler
{

	// List of files for tab rotation and printing options
	private static LinkedList<String> fileNames = new LinkedList<>();

	// Stores JTerm.command while rotating through above list
	private static String command = "";

	// Length of original input to be completed
	private static int startComplete = 0;

	/*
	* Process() void
	*
	* Calls appropriate method for handling
	* input read from the Input class, using
	* booleans in JTerm class to determine
	* what OS the program is running on.
	*/
	static void Process()
	{

		char input = 0;

		try
		{
			input = (char)Input.read(true);

		}
		catch (IOException e)
		{
			e.printStackTrace();

		}

		if (JTerm.isWin)
			ProcessWin(input);

		else if (JTerm.isUnix)
			ProcessUnix(input);

	}

	/*
	* ProcessUnix() void
	*
	* Processes input provided by Input class,
	* and operates based on the input it receives,
	* using the characters values used in Unix 
	* systems.
	*
	* char input - last character input by user
	*/
	private static void ProcessUnix(char input)
	{

		boolean clearFilesList = true;

		// Do not output tabs, caps lock and backspace chars
		if (input != 20 && input != 127 && input != 9) 
			System.out.print(input);
		
		// Caps Lock
		if (input == 20)
			JTerm.capsOn = !JTerm.capsOn;

		// Back Space
		else if (input == 127)
		{ 
			if (JTerm.command.length() > 0)
			{
				JTerm.command = JTerm.command.substring(0, JTerm.command.length() - 1);

				// Delete char, add white space and move back again
				System.out.print("\b \b");

			}

		}

		// Special chars, more can be added
		else if (",./\\-_+=~".contains(String.valueOf(input)))
			JTerm.command += input;

		// Tab
		else if (input == '\t' && JTerm.command.length() > 0)
		{
			clearFilesList = false;

			// Split into sections
			String[] commandArr = JTerm.command.split(" ");

			// Get last element
			String currText = commandArr[commandArr.length - 1];

			// If more than one element, autocomplete file
			if (commandArr.length > 1 && !JTerm.command.endsWith(" "))
				FileAutocomplete(currText);

			// If one element, autocomplete command (to be implemented)
			else if (commandArr.length == 1 && !JTerm.command.endsWith(" ")) 
				CommandAutocomplete(currText);
			
			// If last input was space, print all options
			else if (JTerm.command.endsWith(" "))
				FileAutocomplete(" ");

		}

		// Enter, or new line
		else if (input == '\n')
		{ 
			if (JTerm.command.length() > 0)
				JTerm.Parse(JTerm.command);
			
			JTerm.command = "";
			System.out.println();

		}

		// It's a letter
		else if (Character.isLetter(input))
		{
			if (!JTerm.capsOn)
				JTerm.command += input;

			else
				JTerm.command += Character.toUpperCase(input);

		}

		// Just print it if it is defined
		else
			JTerm.command += input;

		if (fileNames.size() > 0 && clearFilesList)
			fileNames.clear();

	}

	/*
	* ProcessWin() void
	*
	* Processes input provided by Input class,
	* and operates based on the input it receives,
	* using the characters values used in Windows systems.
	*
	* char input - last character input by user
	*/
	private static void ProcessWin(char input)
	{

		boolean clearFilesList = true;

		if (input != 20 && input != 8 && input != 9)
			System.out.print(input);

		// Caps Lock
		if (input == 20)
			JTerm.capsOn = !JTerm.capsOn;

		// Backspace
		else if (input == 8)
		{
			if (JTerm.command.length() > 0)
			{
				JTerm.command = JTerm.command.substring(0, JTerm.command.length() - 1);

				// Delete char, add white space and move back again
				System.out.print("\b \b");
			}

		}

		// Tab
		else if (input == 9 && JTerm.command.length() > 0)
		{
			clearFilesList = false;

			// Split into sections
			String[] commandArr = JTerm.command.split(" ");

			// Get last element
			String currText = commandArr[commandArr.length - 1]; 

			// If more than one element, autocomplete file
			if (commandArr.length > 1 && !JTerm.command.endsWith(" ")) 
				FileAutocomplete(currText);

			// If one element, autocomplete command (to be implemented)
			else if (commandArr.length == 1 && !JTerm.command.endsWith(" ")) 
				CommandAutocomplete(currText);

			// If last input was space, print all options
			else if (JTerm.command.endsWith(" "))
				FileAutocomplete(" ");

		}

		// New line
		else if (input == 13)
		{
			System.out.println("\r\n");
			if (JTerm.command.length() > 0)
				JTerm.Parse(JTerm.command);

			JTerm.command = "";
			System.out.println("\r\n");

		}

		// It's a letter
		else if (Character.isLetter(input))
		{
			if (!JTerm.capsOn)
				JTerm.command += input;
			else
				JTerm.command += Character.toUpperCase(input);

		}

		// just print it if it is defined
		else if (Character.isDefined(input))
			JTerm.command += input;

		if (fileNames.size() > 0 && clearFilesList)
			fileNames.clear();

	}

	/*
	* FileAutocomplete()
	*
	* Using a string of text representing what has been typed presently,
	* displays all files that match the current input.
	*
	* String currText - file that is to be completed
	*/
	private static void FileAutocomplete(String currText)
	{

		boolean newList = false;
		File currFolder = new File(JTerm.currentDirectory);
		File[] files = currFolder.listFiles();

		// get all file names for comparison
		if (fileNames.size() == 0)
		{
			// For tab rotation, true means no tab rotation, false means rotate through list
			newList = true;

			// Stores original command so that JTerm.command does not keep adding to itself
			command = JTerm.command;

			// For autocomplete in tab rotation
			startComplete = currText.length();

			for (File f : files)
				if (f.getName().startsWith(currText))
					fileNames.add(f.getName());

		}

		if (fileNames.size() != 1)
		{
			// Clear line
			if (fileNames.size() > 0 || currText.equals(" "))
				ClearLine(JTerm.command);

			// Print matching file names
			if (newList)
				for (String s : fileNames)
					System.out.print(s + "\t");

			else
			{
				ClearLine(JTerm.command);

				// Get first file or dir name
				String currFile = fileNames.pollFirst();

				// Autocomplete with first file or dir name
				JTerm.command = command + currFile.substring(startComplete, currFile.length());

				// Print to screen
				System.out.print(JTerm.command);

				// Add file or dir name at end of list
				fileNames.add(currFile);

			}

			if (fileNames.size() > 0 && newList)
			{
				System.out.println();

				// Re-output command after clearing lines
				System.out.print(JTerm.command);

			}

			// If no input, just output all files and folders
			if (currText.equals(" "))
			{
				if (newList)
				{
					for (File f : files)
					{
						System.out.print(f.getName() + " \t");
						fileNames.add(f.getName());

					}

					// Improve readability
					System.out.println("\n");

					// Re-output command after clearing lines
					System.out.print(JTerm.command);

				}
				else
				{
					ClearLine(JTerm.command);

					// Get first file or dir name
					String currFile = fileNames.pollFirst();

					// Autocomplete with first file or dir name
					JTerm.command = command + currFile.substring(startComplete, currFile.length());

					// Print to screen
					System.out.print(JTerm.command);

					// Add file or dir name at end of list
					fileNames.add(currFile);

				}

			}

		}
		else
		{

			String fileName = fileNames.getFirst();
			JTerm.command += fileName.substring(currText.length(), fileName.length()) + " ";
			System.out.print(fileName.substring(currText.length(), fileName.length()) + " ");

			// Improve readability
			System.out.println();
			
		}

	}

	/*
	* ClearLine() void
	*
	* Clears a line in the console of size line.length().
	*
	* String line - line to be cleared
	*/
	private static void ClearLine(String line) {

		for (int i = 0; i < line.length(); i++)
			System.out.print("\b");

		for (int i = 0; i < line.length(); i++)
			System.out.print(" ");

		for (int i = 0; i < line.length(); i++)
			System.out.print("\b");

	}

	/*
	* CommandAutocomplete()
	*
	* String currText - command that is to be completed
	*/
	private static void CommandAutocomplete(String currText)
	{
		


	}
}
