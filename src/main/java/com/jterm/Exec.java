package main.java.com.jterm;

import java.io.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;

public class Exec {
	
	private static Hashtable<String, String> vars = new Hashtable<>();
	
	public static void Run(ArrayList<String> options)
	{	
		
		String file = "";
	
		for (String option: options) {
			if (option.equals(""))
			{
				
				
			}
			else
			{
				file = option;
				
			}
			
		}
		
		File script = new File(file);
		if (!script.exists() || !script.isFile())
		{
			file = JTerm.currentDirectory + file;
			script = new File(file);
			if (!script.exists() || !script.isFile())
			{
				System.out.println("ERROR: File \"" + file + "\" either does not exist or is not a file.");
				return;
				
			}
			
		}
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(script));
			try {
				String directive = reader.readLine();
				do {
					Exec.Parse(directive);
					
				} while ((directive = reader.readLine()) != null);

				reader.close();
				
			} catch (IOException ioe) {
				System.out.println(ioe);
				
			}
			
			
		} catch (FileNotFoundException ioe) {
			System.out.println(ioe);
			
		}
		
	}
	
	public static void Parse(String directive)
	{
		
		Scanner tokenizer = new Scanner(directive);
		ArrayList<String> options = new ArrayList<String>();
		while (tokenizer.hasNext())
			options.add(tokenizer.next());
		
		String command = options.get(0).toLowerCase();
		
		switch (command)
		{
		case "set":
			String key = options.get(0), value = "";
			if ( !options.get(1).equals("=") )
			{
				tokenizer.close();
				return;
				
			}
			
			for (int i = 2; i < options.size(); i++)
			{
				value += options.get(i);
				
			}

			vars.put(key, value);
			break;
			
		default:
			JTerm.Parse(options);
			tokenizer.close();
			return;
		
		}
		
		tokenizer.close();
		
	}
	
}
