/*
* JTerm - a cross-platform terminal
* Copyright (C) 2017 Sergix, NCSGeek
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.

* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.

* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package main.java.jterm;

import java.util.ArrayList;
import javax.swing.JFrame;

public class Window
{

	public static int windowCount = 0;
	
	private int id;
	private JFrame window;
	private String title = null;
	private boolean visible = false;
	
	/*
	* Window() void
	* 
	* Create a new JFrame window.
	* 
	* ArrayList<String> options - command options
	* 
	* -h
	*     Prints help information
	*     
	* -t title
	* 	  Sets the window title in 
	* 	  the header
	* 
	* -w width
	* 	  Sets the width of the window
	* 
	* -l height
	* 	  Sets the height of the window
	* 
	* -r
	* 	  Sets the window to be able to
	*	  be resized  
	* 	  
	*/
	Window(ArrayList<String> options)
	{
		
		int width = 500, height = 500;
		boolean resizable = false;
		
		boolean titleNext = false, heightNext = false, widthNext = false;
		
		for (String option: options)
		{
			if (option.equals("-h"))
			{
				System.out.println("Command syntax:\t\nwindow [-h] [-r] [-v] [-w width] [-l height] [-t title]\n\nCreates a new programmable GUI window.\nDefault title is \"JTerm Window\", and the default width and height of the window is 500 x 500.");
				return;
				
			}
			else if (option.equals("-v"))
				visible = true;

			else if(option.equals("-t"))
			{
				title = "";
				titleNext = true;
				
			}
			else if(option.equals("-w"))
				widthNext = true;

			else if(option.equals("-l"))
				heightNext = true;

			else if(widthNext)
			{
				width = Integer.parseInt(option);
				widthNext = false;
				
			}
			
			else if(heightNext)
			{
				height = Integer.parseInt(option);
				heightNext = false;
				
			}
			else if (option.equals("-r"))
				resizable = true;

			else if(titleNext)
				title += option + " ";
			
		}
		
		windowCount += 1;
		id = windowCount;
		
		if (title == null)
			title = "JTerm Window";
		
		JFrame window = new JFrame(title);
		window.setSize(width, height);
		window.setResizable(resizable);
		window.setVisible(visible);
		
		this.window = window;
		
	}
	
	/*
	* ToggleVisible() void
	* 
	* Toggles the visibility of the window
	*/
	public void ToggleVisible()
	{
		window.setVisible(visible = !visible);
		
	}
	
	/*
	* GetId() int
	* 
	* Returns the process ID of the window
	*/
	public int GetId()
	{
		
		return id;
		
	}
	
	/*
	* GetId() JFrame
	* 
	* Returns the JFrame object displaying
	* the window
	*/
	public JFrame GetFrame()
	{
		
		return this.window;
		
	}
	
	/*
	* GetTitle() String
	* 
	* Returns the title of the window
	*/
	public String GetTitle()
	{
		
		return title;
		
	}
	
}
