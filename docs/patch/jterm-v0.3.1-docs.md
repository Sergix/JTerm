# JTerm Patch Notes Documentation v0.3.1

## Table of Contents
I. Table of Contents  
II. Overview  
III. Patch Notes  
IV. Changelog  

## Overview
This document provides information on version 0.3.1 of the JTerm project. Release notes, developer notes, and the full changelog is included.

## Patch Notes
Patch version "0.3.1" fixes some major crashing issues associated with the command interpreter. Additions to the Window() class were also made to help with the incoming "Scripting" update (designated to be v0.4.0). Licensing notes were also put in the source code at the head of each file, as recommended by the GNU Public License. The "Exec" class is included in the source code built in the JAR, but it is not usable until the "Scripting" update (as mentioned before.) Read the Changelog section for details on these new additions.

## Changelog
- Created JTerm.Parse() function that is the dedicated command parser (called by JTerm.Standby())
- Added checking for blank input in the Parse() function
- Entering an invalid command now outputs "Unknown Command '(command)'"
- Window() class changes
	- Window.ToggleVisible() function that toggles the visibility of the window
	- Window.visible property (private) (visibility is automatically set to false)
	- Added command option [-v] which turns on visibility of the window
	- Fixed possible issue with the Window title being null
	- Window.GetTitle() returns title of the window
	- Window.title property (private)
- GNU General Public License comments were put at the head of each source file in the project
- When opening the terminal, a quick note about JTerm's license is displayed, along with JTerm's version and authors
- Slight project folder mapping reformatting for possible Maven integration (package is now "main.java.jterm")

> `JTerm 0.3.1`  
> `jterm-v0.3.1.jar`  
> This project and its source are held under the GNU General Public License, located in the LICENSE file in the project's directory.  
> (c) 2017  
