package main.java.jterm;

import java.util.ArrayList;
import java.io.IOException;

public class Pause
{

	/*
	* Pause() void
	* 
	* Pauses the interpreter until the user
	* hits the "Enter" key.
	*
	* ArrayList<String> options - command options
	*
	* message
	* 	Pause message to be printed
	*/
	public Pause(ArrayList<String> options) { 

		if (options.size() == 0)
			System.out.print("Press enter to continue...");
		
		else
			System.out.print(Exec.GetRest(options, 0));

		try
		{
			JTerm.userInput.read();
			JTerm.userInput.skip(1);
			
		}
		catch (IOException ioe)
		{
			System.out.println(ioe);

		}

	}

}