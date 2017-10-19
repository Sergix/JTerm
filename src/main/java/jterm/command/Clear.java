package jterm.command;

import jterm.JTerm;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Class for the "Clear" command
 *
 * Clears the terminal display by removing all lines
 */
public class Clear
{
  final private String ANSI_CLS = "\u001b[2J";
  final private String ANSI_HOME = "\u001b[H";

  public Clear(ArrayList<String> options)
  {
    for (String option : options)
    {
      if (option.equals("-h"))
      {
        System.out.println("Command syntax:\n\tclear [-h]\n\nClears all lines in the terminal display.");
      }
    }

    // If '-h' was not used - Clear the Screen
    if (!options.contains("-h"))
    {
      if (JTerm.isUnix)
      {
        // Use escape sequences to clear the screen for Unix OS
        System.out.print(ANSI_CLS + ANSI_HOME);
        System.out.flush();
      }
      else if (JTerm.isWin)
      {
        // Invoke the command line interpreter's own 'clear' command for Windows OS
        try
        {
          new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        }
        catch (IOException | InterruptedException e)
        {
          System.out.println(e);
        }
      }
    }
  }
}
