package main.java.com.jterm;

import java.io.*;
import java.util.ArrayList;

public class Dir {

  public static void PrintDir(ArrayList<String> options) throws NullPointerException {

	  String path = ".";
	  boolean printFull = true, printFile = false, printHelp = false;
	  
	  for (String option: options) {
		  if (option.equals("-f")) {
			  printFull = false;
			  printFile = true;
			  
		  }
		  else if (option.equals("-h"))
		  {
			  printHelp = true;
			  
		  }
		  else
		  {
			  path = option;
			  
		  }
		  
	  }
	  
	  File dir = new File(path);
	  File[] files = dir.listFiles();
	  
	  /*
	   * Format of output:
	   * [FD] [RWHE] [filename] [size in KB]
	   * 
	   * Prefix definitions:
	   * 	F -- File
	   * 	D -- Directory
	   * 	R -- Readable
	   * 	W -- Writable
	   * 	H -- Hidden
	   * 
	   * Example:
	   * 	F RW	myfile.txt	   5 KB
	   */
	  
	  if (printHelp) {
		  System.out.println("Command syntax:\n\tdir [-f] [-h] [directory]");
		  return;
		  
	  }
	  
	  System.out.println("[Contents of \"" + path + "\"]");
	  for (File file: files)
	  {
		  if (printFull) {
			  System.out.println("\t" + (file.isFile() ? "F " : "D ") + (file.canRead() ? "R" : "") + (file.canWrite() ? "W" : "") + (file.isHidden() ? "H" : "") + "\t" + file.getName() + (file.getName().length() < 8 ? "\t\t\t" : (file.getName().length() > 15 ? "\t" : "\t\t")) + (file.length() / 1024) + " KB");
			  
		  } else if (printFile) {
			  System.out.println("\t" + file.getName());
			  
		  }
		  
	  }
	  
  }
}
