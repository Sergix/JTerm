package main.java.com.jterm;

import java.io.*;

public class Dir {

  public static void PrintDir(String path) throws NullPointerException {
	  
	  File dir = new File(path);
	  File[] files = dir.listFiles();
	  
	  for (File file: files)
	  {
		  System.out.println(file.getName());
	  }
	  
  }
}
