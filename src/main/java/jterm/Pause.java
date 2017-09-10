package main.java.jterm;

import java.util.ArrayList;
import java.io.IOException;

public class Pause
{

	/*
	* Pause() void
	* 
	* Constructor for calling Process() function.
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