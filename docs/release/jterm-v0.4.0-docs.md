# JTerm Documentation v0.4.0

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
This document provides information on changes included in release version "0.4.0". The JTerm project is still far from its official release, but the project has had enough changes to include it as a new pre-release version. This document was written by @Sergix and @chromechris and reviewed by @NCSGeek.

## Build Targets
```
[VERSION]    [FILE]              [STATE]
0.1.0        jterm-v0.1.0.jar    OK
0.2.0        jterm-v0.2.0.jar    OK
0.2.1	     jterm-v0.2.1.jar	 OK
0.3.0	     jterm-v0.3.0.jar	 DEPRECATED
0.3.1		 jterm-v0.3.1.jar	 OK
0.4.0		 jterm-v0.4.0.jar	 OK
```

## Changelog
- Created "Ps" class (@chromechris)
  - Added a ps (process) command to view current processes in system
- Created "Ping" class (@chromechris)
  - Added a ping (ICMP Request) command in order to ping internet hosts
- Added "-h" option to "exec" command
- User input stream variable moved to global static variable "JTerm.userInput"
- Project now builds with Maven for testing and Travis CI
- Other minor fixes and technical changes

## New Features

### Commands
The commands `ps` and `ping` have been added to JTerm. The syntax for these commands are as follows:
```
ps [-h]
ping [-h] [-p port] host
```
`-h` is ALWAYS displays help information for the command. `-p` stands for port in the `ping` command.
NOTE: `ps` is OS-dependent and is NOT functionable on macOS/OSX systems.

### Wrapfile
From this release forward, a Wrapfile will be included in the build/src/ directory to encourage the use of my other project, [Wrapper](https://sergix.github.io/projects/wrapper). The format of the name will simply be "jterm-src-v(version).wrap".

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
- `ps` - Lists all processes running on the host system
- `ping` - Enables ICMP pinging to internet Hosts.
	- `-p` - specifies the port to connect with

> JTerm 0.4.0  
> `jterm-v0.4.0.jar`  
> This project and its source are held under the GNU General Public License, located in the LICENSE file in the project's directory.  
> (c) 2017  