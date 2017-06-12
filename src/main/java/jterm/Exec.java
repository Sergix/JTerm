package main.java.jterm;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Scanner;

public class Exec {
	
	private static Hashtable<String, String> vars = new Hashtable<>();
	
	public static void Run(ArrayList<String> options)
	{	
		
		String file = "";
	
		for (String option: options) {
			if (option.equals("-h"))
			{
				System.out.println("Command syntax:\n\texec file\n\nExecutes a batch script.");
				
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
				if (directive != null)
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
			if (options.size() == 1)
			{
				String element = "";
				for (Enumeration<String> e = vars.keys(); e.hasMoreElements();)
					
				       System.out.println((element = e.nextElement()) + "=" + vars.get(element));
				       
				break;
			}
			
			String key = options.get(1), value = "";
			if ( !options.get(2).equals("=") )
			{
				tokenizer.close();
				return;
				
			}
			
			for (int i = 3; i < options.size(); i++)
			{
				value += options.get(i) + " ";
				
			}

			vars.put(key, value);
			break;
			
		case "cls":
		case "color":
			break;
			
		default:
			JTerm.Parse(options);
			tokenizer.close();
			return;
		
		}
		
		tokenizer.close();
		
	}
	
}
