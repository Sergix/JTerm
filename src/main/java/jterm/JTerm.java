/*
* JTerm - a cross-platform terminal
* Copyright (code) 2017 Sergix, NCSGeek
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

// package = folder :P
package main.java.jterm;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;

public class JTerm
{

	// Global version variable
	static String version = "0.5.1";

	// Global directory variable (use "cd" command to change)
	// Default value "./" is equal to the default directory set when the program starts
	static String currentDirectory = "./";

	// User input variable used among all parts of the application
	static BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

	// Boolean to determine if caps lock is on, since input system does not distinguish between character cases
	// Command string which the input system will aggregate characters to
	private static boolean capsOn = Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK);
	static String command = "";

	/*
	* main() void
	*
	* Function called when the program loads. Sets
	* up basic input streams.
	*
	*
	* String[] args - arguments passed from the
	* 				console
	*/
	public static void main(String[] args)
	{

		// Print licensing information
		System.out.println(
				"JTerm Copyright (C) 2017 Sergix, NCSGeek, chromechris\n" +
						"This program comes with ABSOLUTELY NO WARRANTY.\n" +
						"This is free software, and you are welcome to redistribute it\n" +
						"under certain conditions.\n"
		);

		/*
		 * Wait until "exit" is typed in to exit
		 * Sends last char received from Input class to Process function
		 */
		while (true) {
			try {
				Process((char)Input.read(true));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * Process()
	 *
	 * Processes input provided by Input class, and operates based on the input it receives
	 *
	 * @param input last character input by user
	 */
	private static void Process(char input) {
		if (input != 20 && input != 127 && input != 9) // do not output tabs, caps lock and backspace chars
			System.out.print(input);
		if (input == 20) { // caps lock
			capsOn = !capsOn;
		} else if (input == 127) { // back space
			if (command.length() > 0) {
				command = command.substring(0, command.length() - 1);
				System.out.print("\b \b"); // delete char, add white space and move back again
			}
		} else if (",./\\-_+=~".contains(String.valueOf(input))) { // special chars, more can be added
			command += input;
		} else if (input == '\t' && command.length() > 0) { // tab
			String[] commandArr = command.split(" "); // split into sections
			String currText = commandArr[commandArr.length - 1]; // get last element
			if (commandArr.length > 1) // if more than one element, autocomplete file
				FileAutocomplete(currText);
			else if (commandArr.length == 1) // if one element, autocomplete command (to be implemented)
				CommandAutocomplete(currText);

		} else if (input == '\n') { // enter, or new line
			if (command.length() > 0)
				Parse(command);
			command = "";
		} else if (Character.isLetter(input)) { // its a letter
			if (!capsOn)
				command += input;
			else
				command += Character.toUpperCase(input);
		} else if (Character.isDefined(input)) { // just print it if it is defined
			command += input;
		}
	}

	/**
	 * FileAutocomplete()
	 *
	 * Using a string of text representing what has been typed presently, displays all files that match the current input
	 * Currently does not complete any input due to incapability to move input cursor, would require own UI... WIP
	 *
	 * @param currText file that is to be completed
	 */
	private static void FileAutocomplete(String currText) {
		File currFolder = new File(currentDirectory);
		File[] files = currFolder.listFiles();
		LinkedList<String> fileNames = new LinkedList<>();

		// get all file names for comparison
		for (File f : files)
			if (f.getName().startsWith(currText))
				fileNames.add(f.getName());
		// print all file names that match
		if (fileNames.size() != 1) {
			for (String s : fileNames)
				System.out.print(s + "\t");
			// if no input, just output all files and folders
			if (fileNames.size() == 0)
				for (File f : files)
					System.out.print(f.getName() + " \t");
			System.out.println();
		} else {
			String fileName = fileNames.getFirst();
			command += fileName.substring(currText.length(), fileName.length()) + " ";
			System.out.print(fileName.substring(currText.length(), fileName.length()) + " ");
		}
	}

	/**
	 * CommandAutocomplete()
	 *
	 * @param currText command that is to be completed
	 */
	private static void CommandAutocomplete(String currText) {
		// will autocomplete commands
	}

	/*
	* Parse() boolean
	*
	* Checks input and passes command options to the function
	* that runs the requested command.
	*
	* ArrayList<String> options - command options
	*/
	public static boolean Parse(String options)
	{

		ArrayList<String> optionsArray = GetAsArray(options);

		// Default to process/help command if function is not found
		String method = "Process";

		// Get the first string in the options array, which is the command,
		// and capitalize the first letter of the command
		String original = optionsArray.get(0).toLowerCase(), command = original;
		String classChar = command.substring(0, 1);
		classChar = classChar.toUpperCase();
		command = command.substring(1);
		command = "main.java.jterm." + classChar + command;
		optionsArray.remove(0);

		// Get the method name
		if (optionsArray.toArray().length >= 1)
			method = optionsArray.get(0);

		else
			optionsArray.add(method);

		classChar = method.substring(0, 1);
		classChar = classChar.toUpperCase();
		method = method.substring(1);
		method = classChar + method;

		try
		{
			// Get the class of the command
			Class<?> clazz = Class.forName(command);
			Constructor<?> constructor = clazz.getConstructor(ArrayList.class);
			Object obj = constructor.newInstance(optionsArray);

			ArrayList<Method> methods = new ArrayList<Method>(Arrays.asList(obj.getClass().getDeclaredMethods()));

			// Invoke the correct method of the class to run, but only if it contains that method
			Method m = obj.getClass().getMethod(method, ArrayList.class);
			if(methods.contains(m))
			{
				optionsArray.remove(0);
				m.invoke(options.getClass(), new Object[] {optionsArray});

			}

		}

		// Exceptions
		catch (ClassNotFoundException cnfe)
		{
			ArrayList<String> execFile = new ArrayList<String>();
			execFile.add(original);
			if ( Exec.Run(execFile) )
				System.out.println("Unknown Command \"" + original + "\"");

		}
		catch (InstantiationException ie)
		{
			System.out.println(ie);

		}
		catch (IllegalAccessException iae)
		{
			System.out.println(iae);

		}
		catch (NoSuchMethodException nsme)
		{
			//System.out.println(nsme);

		}
		catch (InvocationTargetException ite)
		{
			System.out.println(ite);

		}

		return false;

		// 	// Commands to skip in batch files
		// 	case "bcdedit":
		// 	case "chkdsk":
		// 	case "chkntfs":
		// 	case "cls":
		// 	case "cmd":
		// 	case "color":
		// 	case "convert":
		// 	case "diskpart":
		// 	case "driverquery":
		// 	case "format":
		// 	case "fsutil":
		// 	case "gpresult":
		// 	case "mode":
		// 	case "sc":
		// 	case "shutdown":
		// 	case "start":
		// 	case "tasklist":
		// 	case "taskkill":
		// 	case "ver":
		// 	case "vol":
		// 	case "wmic":
		// 		break;

	}

	/*
	* GetAsArray() ArrayList<String>
	* 
	* Returns a String as an ArrayList of
	* Strings (spaces as delimiters)
	*
	* String options - String to be split
	*/
	public static ArrayList<String> GetAsArray(String options)
	{

		// Get each substring of the command entered
		Scanner tokenizer = new Scanner(options);

		// options String array will be passed to command functions
		ArrayList<String> array = new ArrayList<String>();

		// Get command arguments
		while (tokenizer.hasNext())
		{
			String next = tokenizer.next();
			array.add(next);

		}

		// Close the string stream
		tokenizer.close();

		return array;

	}

	/*
	* GetAsString() String
	* 
	* Returns an ArrayList of Strings 
	* as a String (spaced with spaces)
	*
	* ArrayList<String> options - array to be split
	*/
	public static String GetAsString(ArrayList<String> options)
	{

		// Get each substring of the command entered
		String string = "";

		// Get command arguments
		for (String option: options)
			string += option + " ";

		string.trim();

		return string;

	}

}