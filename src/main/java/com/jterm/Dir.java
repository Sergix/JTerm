package main.java.com.jterm;

import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Dir {

  public static void PrintDir() {
  
    // Copied this snippet of code off of the java docs :P
    try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
    
      for (Path file: stream) {
        System.out.println(file.getFileName());
      }
       
    } catch (IOException | DirectoryIteratorException x) {

      System.err.println(x);
      
    }
  }
}
