package main.java.jterm;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.commons.lang3.SystemUtils;

/*
* Original code credit to @chromechris
* 
* (edits for release done by @Sergix)
*/
public class Ps 
{

	/*
	* Ps() void
	* 
	* Constructor for calling Process() function.
	*/
	public Ps(ArrayList<String> options)
	{

		for (String option: options)
		{
			if (option.equals("-h"))
			{
				System.out.println("Command syntax:\n\tps [-h]\n\nDisplays all current processes running on the host system.");
				return;
				
			}
			
		}

		if (SystemUtils.IS_OS_LINUX)
		{
			try
			{
				String line;
				Process p = Runtime.getRuntime().exec("ps -e");
				BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
				while ((line = input.readLine()) != null)
				{
					// Parse data here.
					System.out.println(line);
					
				}
				input.close();
				
			}
			catch (Exception err)
			{
				err.printStackTrace();
				
			}

		}
		else if (SystemUtils.IS_OS_WINDOWS)
		{
			try
			{
				String line;
				Process p = Runtime.getRuntime().exec(System.getenv("windir") + "\\system32\\" + "tasklist.exe");
				BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
				while ((line = input.readLine()) != null)
				{
					// Parse data here.
					System.out.println(line);
					
				}
				
				input.close();
				
			}
			catch (Exception err)
			{
				err.printStackTrace();
				
			}
			
		}

	}

}