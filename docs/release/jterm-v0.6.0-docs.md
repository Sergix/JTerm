# JTerm Documentation v0.6.0

## Table of Contents
```
I. Table of Contents  
II. Overview    
III. Build Targets  
IV. Changelog    
V. New Features  
    A. Tab Autocompletion  
	B. Application Execution  
    C. Commands  
	D. Command Package  
VI. Active Command List
```

## Overview
This document provides information on changes included in release version "0.6.0". The JTerm project is still far from its official release, but the project has had enough changes to include it as a new pre-release version. The Changelog section records changes made from the previous patch (0.5.1). This document was written by @Sergix and reviewed by @NCSGeek.

## Build Targets
```
[VERSION]    [FILE]              [STATE]
0.1.0        jterm-v0.1.0.jar    OK
0.2.0        jterm-v0.2.0.jar    OK
0.2.1	     jterm-v0.2.1.jar	 OK
0.3.0	     jterm-v0.3.0.jar	 DEPRECATED
0.3.1		 jterm-v0.3.1.jar	 OK
0.4.0		 jterm-v0.4.0.jar	 OK
0.4.1        jterm-v0.4.1.jar    OK
0.5.0        jterm-v0.5.0.jar    OK
0.5.1		 jterm-v0.5.1.jar	 OK
0.6.0		 jterm-v0.6.0.jar	 OK
```

## Changelog
- Removed the Process() function from Window and Exec
- Fixed issue where if "dir" or "files" was entered it crashed the application due to an out of bounds exception
- Added Waffle.io throughput graph to the README
- JTerm.java changes and additions for tab completion:
	- `os`: gets the OS name
	- `isWin`: boolean for if OS is Windows-based
	- `isUnix`: boolean for if OS is UNIX-based
	- `capsOn`: used for determining whether Caps Lock is pressed
	- `command`: command string currently stored
	- Removed `Standby()`; characters are read individually
- [Input.java](http://www.source-code.biz/snippets/java/RawConsoleInput)
- InputHander.java
	- `fileNames`: list of files for tab rotation and printing options
	- `command`: stores JTerm.command while rotating through above list
	- `startComplete`: length of original input to be completed
	- `ProcessUnix()`: processes input provided by Input class (UNIX)
	- `ProcessWin()`: processes input provided by Input class (Windows)
	- `FileAutocomplete()`: displays all files that match the current input
	- `ClearLine()`: clears a line in the console of size line.length()
	- Works with `exec`; completes filenames
	- If there are multiple possible results, it prints out a list of possible completions, then the user can cycle through options by repeatedly hitting TAB.
- Added Issue template to repo (borrowed from "angular-translate")
- Created ROADMAP.md file, which contains information about the needs to reach "v1.0.0" of JTerm
- Created a GitHub milestone to track the ROADMAP ([v1.0.0 Roadmap](https://github.com/Sergix/JTerm/issues?q=is%3Aopen+is%3Aissue+milestone%3A%22v1.0.0+Roadmap%22))
- Fixed issues with running on Mac and other UNIX distros
- Moved command classes into their own package (main.java.jterm.command)
- Made the JTerm class variables public
- Added help information to Date, Clear, Exit, Help, Pause, Set, and Time
- Closed [#31](https://github.com/Sergix/JTerm/issues/31)
- Closed [#32](https://github.com/Sergix/JTerm/issues/32)
- Closed [#33](https://github.com/Sergix/JTerm/issues/33)
- Closed [#35](https://github.com/Sergix/JTerm/issues/35)
- Closed [#36](https://github.com/Sergix/JTerm/issues/36)
- Closed [#37](https://github.com/Sergix/JTerm/issues/37)
- Closed [#40](https://github.com/Sergix/JTerm/issues/40)
- Merged [#34](https://github.com/Sergix/JTerm/pull/34)
- Merged [#38](https://github.com/Sergix/JTerm/pull/38)
- Merged [#39](https://github.com/Sergix/JTerm/pull/39)
- Merged [#43](https://github.com/Sergix/JTerm/pull/43)
- Merged [#44](https://github.com/Sergix/JTerm/pull/44)
- Merged [#46](https://github.com/Sergix/JTerm/pull/46)
- Closed [#50](https://github.com/Sergix/JTerm/pull/50)
- Minor bug fixes

## New Features

### Tab Autocompletion
Striking the `TAB` key now autocompletes names of files. Hitting it repeatedly cycles through options to autocomplete if there are multiple results. It can currently be used with `exec`. Huge thanks to @nanoandrew4!

### Application Execution
`exec` now runs system native executables and JARs. Thanks to @nanoandrew4!

### Commands
The `time` and `date` commands have been added (@NCSGeek), as well as the `clear` command (@ccmetz).

### Command Package


## Active Command List
- `help [-h]` - displays information about JTerm
- `exit [-h]` - Exits the JTerm application
- `dir [-h] [ls [-f] [-h]] [cd, chdir [-h] directory] [pwd [-h]] [md [-h] name]`
	- `ls` - Dispays extensive directory information, including files and subdirectories (@pmorgan3)
		- `-f` - toggles minimal output
	- `cd` - Changes the directory to the location specified
	- `pwd` - prints the current working directory
	- `md` - creates a new directory
- `files [-h] [read [-h] file1 [file2 file3...]] [delete, del, rm [-h] file/directory] [write [-h] filename]`
	- `read` - prints the contents of the specified files
	- `delete`, `del`, `rm` - deletes the specified file or directory (@pmorgan3)
	- `write` - Opens an input prompt that will write the data to the new file named `filename` 
- `echo [-h] input` - outputs the input on a new line
- `window [-h] [-r] [-w width] [-l height] [-t title]` - creates a new GUI window
	- `-r` - toggles resizability
	- `-w` - specifies window width
	- `-l` - specifies window height
	- `-t` - sets the name of the window, displayed in the title bar
- `ps [-h]` - Lists all processes running on the host system
- `ping [-h] [-p] host` - enables ICMP pinging to Internet hosts
	- `-p` - specifies the port to connect with
- `set [-h] name = value` - creates a new variable in the current session (previously in Exec)
- `pause [-h] [message]` - pauses the interpreter until the user hits the "Enter" key
- `date [-h]` - prints the system date to the console
- `time [-h]` - prints the system time to the console
- `clear [-h]` - clears the terminal

> JTerm 0.6.0  
> `jterm-v0.6.0.jar`  
> This project and its source are held under the GNU General Public License, located in the LICENSE file in the project's directory.  
> (c) 2017  