# JTerm Documentation v0.3.0

## Table of Contents
```
I. Table of Contents  
II. Overview    
III. Build Targets  
IV. Changelog    
V. New Features  
    A. Commands  
    B. Client/Server
VI. Active Command List
```

## Overview
This document provides information on changes included in release version "0.3.0". The JTerm project is still far from its official release, but the project has had enough changes to include it as a new pre-release version. This document was written by @Sergix and reviewed by @NCSGeek.

## Build Targets
```
[VERSION]    [FILE]              [STATE]
0.1.0        jterm-v0.1.0.jar    OK
0.2.0        jterm-v0.2.0.jar    OK
0.2.1	     jterm-v0.2.1.jar	 OK
0.3.0	     jterm-v0.3.0.jar	 OK
```

## Changelog
- Created "Client" class
  - Added "connect" command; called with `Client.Connect()` function
  - NOT FUNCTIONING in this release; source is included
- Created "Server" class
  - Added "server" command; called with `Server.Start()` function
  - NOT FUNCTIONING in this release; source is included
- Created "Window" class
  - Added "window" command; called with `new Window()` function
- Added the "md" command (Make Directory) that calls the new `Dir.NewDir()` function
- Added the "del" command (DELete) that calls the new `Files.Delete()` function
- In-development source code is now pushed to the `dev` branch on the repository, which will be merged with `master` on release
- Inputed commands convert to all lowercase
- `Write` class has been removed; member function moved to `Files` class
- The current directory will always end with a single "forward slash" (e.g. `/usr/home/`)
- Other minor fixes
-Added a PS (process) command to view current processes in system
-Added a ping (ICMP Request) command in order to ping internet hosts

## New Features

### Commands
The commands `connect`, `server`, `md`, `del`, and `window` have been added to JTerm. The syntax for these commands are as follows:
```
connect [-h] [-p port] address
server [-h] port
md [-h] name
del [-h] file/directory
window [-h] [-r] [-w width] [-l height] [-t title]
```
`connect` AND `server` ARE NOT FUNCTIONABLE IN THIS RELEASE.
`-h` ALWAYS displays the command's help information in the console. `-r`, used for the `window` command, toggles resizability for the created window.

### Client/Server
As mentioned before, a new Client/Server interface has been written for JTerm. However, this functionality has too many issues to be released yet. The source code and classes are packaged into the source build, as well as the JAR, but it is not able to be run. Any presented fixes to the issues are welcome.

## Active Command List
- `help` - displays information about JTerm
- `quit` - Exits the JTerm application
- `write [-h] filename` - Opens an input prompt that will write the data to the new file named `filename` 
- `dir [-f] [-h] [directory]` - Dispays extensive directory information, including files and subdirectories
	- `-f` - toggles minimal output
- `cd [-h] directory` - Changes the directory to the location specified
- `pwd` - prints the current working directory
- `echo [-h] input` - outputs the input on a new line
- `del [-h] file/directory` - deletes the specified file or directory
- `md [-h] name` - creates a new directory
- `window [-h] [-r] [-w width] [-l height] [-t title]` - creates a new GUI window
	- `-r` - toggles resizability
	- `-w` - specifies window width
	- `-l` - specifies window height
	- `-t` - sets the name of the window, displayed in the title bar
- `ps` - Lists all processes in System
-`ping` - Enables ICMP pinging to internet Hosts. First type ping, hit enter, then type in hostname with port to 		  		   which you wish to ping.

> JTerm 0.3.0  
> `jterm-v0.3.0.jar`  
> This project and its source are held under the GNU General Public License, located in the LICENSE file in the project's directory.  
> (c) 2017  
