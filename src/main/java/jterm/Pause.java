package main.java.jterm;

import java.util.ArrayList;
import java.io.IOException;

public class Pause
{

	/*
	* EnterPause() void
	* 
	* Pauses the interpreter until the Enter
	* key is hit.
	* 
	* ArrayList<String> options - command options
	*/
	public static void EnterPause(ArrayList<String> options)
	{

		if (options.size() == 1)
			System.out.print("Press enter to continue...");
		
		else
			System.out.print(Exec.GetRest(options, 1));

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