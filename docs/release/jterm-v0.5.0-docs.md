# JTerm Documentation v0.5.0

## Table of Contents
```
I. Table of Contents  
II. Overview    
III. Build Targets  
IV. Changelog    
V. New Features  
    A. Commands  
    B. Wrapfile  
VI. Active Command List
```

## Overview
This document provides information on changes included in release version "0.5.0". The JTerm project is still far from its official release, but the project has had enough changes to include it as a new pre-release version. The Changelog section records changes made from the previous patch (0.4.1). This document was written by @Sergix and reviewed by @NCSGeek.

## Build Targets
```
[VERSION]    [FILE]              [STATE]
0.1.0        jterm-v0.1.0.jar    OK
0.2.0        jterm-v0.2.0.jar    OK
0.2.1	     jterm-v0.2.1.jar	 OK
0.3.0	     jterm-v0.3.0.jar	 DEPRECATED
0.3.1        jterm-v0.3.1.jar	 OK
0.4.0        jterm-v0.4.0.jar	 OK
0.4.1        jterm-v0.4.1.jar    OK
0.5.0	     jterm-v0.5.0.jar    OK
```

## Changelog
- Command formatting has been changed (see Commands section for details)
- Added `exit` command
- Added (dir) `ls` command (@pmorgan3)
- Added (dir) `rm` command (@pmorgan3)
- Added CodeTriage badge to README
- `quit` command is now `exit`
- Fixed [Issue #23](https://github.com/Sergix/JTerm/issues/23)
- Opened [Feature Requests](https://github.com/Sergix/JTerm/issues/22)
- Created multiple classes
	- Exit.java (`exit`)
	- Help.java (`help`)

## New Features

### Commands
The `ls` (@pmorgan3), `cd`, `chdir`, `pwd`, and `md` commands are now called by putting the `dir` command in front of them. The same goes for `files`, which now contains `read`, `delete`, `del`, `rm` (@pmorgan3), and `write`. View the command list below for more details.

The commands `set`, `exit`, `pause` have been added to JTerm. The syntax for these commands are as follows:
```
set name = value
pause [message]
```
NOTE: the `quit` command is no longer active and has been replaced by `exit`.

## Active Command List
- `help` - displays information about JTerm
- `exit` - Exits the JTerm application
- `dir [help] [ls [-f] [-h]] [cd, chdir [-h] directory] [pwd [-h]] [md [-h] name]`
	- `ls` - Dispays extensive directory information, including files and subdirectories (@pmorgan3)
		- `-f` - toggles minimal output
	- `cd` - Changes the directory to the location specified
	- `pwd` - prints the current working directory
	- `md` - creates a new directory
- `files [help] [read [-h] file1 [file2 file3...]] [delete, del, rm [-h] file/directory] [write [-h] filename]`
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
- `set name = value` - creates a new variable in the current session (previously in Exec)
- `pause [message]` - pauses the interpreter until the user hits the "Enter" key

> JTerm 0.5.0  
> `jterm-v0.5.0.jar`  
> This project and its source are held under the GNU General Public License, located in the LICENSE file in the project's directory.  
> (c) 2017  
