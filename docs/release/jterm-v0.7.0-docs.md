# JTerm Documentation v0.7.0

## Table of Contents
```
I. Table of Contents  
II. Overview    
III. Build Targets  
IV. Changelog    
V. New Features  
    A. GUI  
    B. Commands  
VI. Active Command List
```

## Overview
This document provides information on changes included in release version "0.7.0". The JTerm project is close to its official release, but the project has had enough changes to include it as a new pre-release version. The Changelog section records changes made from the previous patch (0.6.1). This document was written by @Sergix and reviewed by @NCSGeek.

## Build Targets
```
[VERSION]	 [FILE]				 [STATE]
0.1.0		 jterm-v0.1.0.jar	 OK
0.2.0		 jterm-v0.2.0.jar	 OK
0.2.1		 jterm-v0.2.1.jar	 OK
0.3.0		 jterm-v0.3.0.jar	 DEPRECATED
0.3.1		 jterm-v0.3.1.jar	 OK
0.4.0		 jterm-v0.4.0.jar	 OK
0.4.1		 jterm-v0.4.1.jar    OK
0.5.0		 jterm-v0.5.0.jar    OK
0.5.1		 jterm-v0.5.1.jar	 OK
0.6.0		 jterm-v0.6.0.jar	 OK
0.6.1		 jterm-v0.6.1.jar	 DEPRECATED
0.7.0		 jterm-v0.7.0.jar	 OK
```

## Changelog
- `cd ..` no longer adds the `.`'s to current directory
- Added `rm` command to delete files/directories
- Added `commons.io` dependency to POM
- Program starts with prompt instead of `./`
- Fixed some formatting in `JTerm.java`
- Added `setOS()` function to `Util.java`
	- Replaces code previously used to determine OS in `JTerm.java`
- Re-added the build process batch files to the root directory
- Removed `*.bat` from `.gitignore`
- Removed `lang3` dependency from POM and all imports
- Fixed issue where `InputHandler` was outputting an extra newline
- Fixed issue where autocomplete was attempting a non existent directory and outputting `NullPointerException`
- Added `Command` interface with `execute()` method (@ojles)
- Added `CommandException` for package `jterm.command.*` (@ojles)
- Implemented `Command` interface in all command classes (@ojles)
- Added dynamic loading of command instances using reflection, saving them in a `Map<String, Command>` (@ojles)
- Moved helper methods from `JTerm.java` to `Util.java` (@ojles)
- Fixed ping command (@ojles)
- Fixed `Exec.java`, which was not printing all the data got from the executed process
- Removed `org.reflections` POM import
- Updated `ROADMAP.md`
- Fixed issue with Echo cutting off extra character
- Fixed v0.6.0 documentation table
- v0.6.1 is now DEPRECATED
- Output now routes through JTerm.out
- Created "I/O", "GUI", "bug fix", "ROADMAP", "cleanup", and "command" Issue labels
- `Help.java` now automates commands output
- Added `JTerm.getCommands()` function
- `JTerm.COMMANDS` is now public
- Added a fully functioning JavaX GUI (@lbenedetto)
	- Runs commands without recreating anything
	- Added `ProtectedDocument` class
	- Added `ProtectedTextComponent` class
	- Added `Terminal` class (GUI)
	- Added `com.intellij.forms_rt` import to POM for `Terminal.form` GUI layout
- New autocompltete classes (@nanoandrew4)
	- Added `KeyHandler` class
	- Added `Keys` enum
	- Added `FileAutocomplete` class
- Added `Util` functions
	- `removeSpaces()`
	- `containsOnlySpaces()`
	- `getRunTime()`
	- `clearLine()`
	- `getAsArray()`
	- `getAsString()`
	- `getRest()`
	- `getFullPath()`
- Added a command interface (@ojles)
	- Command methods now have a `@Command` annotation
	- All commands are automatically updated and stored in a global hashmap
	- Added `CommandException` class
- Added `target/` to `.gitignore`
- Created `Files.move()`
- Simplified UNIX Arrow Key input
- Moved UNIX and Windows input functions to `RawConsoleInput` class from `UnixInput` and `WinInput` classes
- Added "regex" command (@lbenedetto)
	- Added `Regex.java`
- Added basic unit tests
	- Created `src/test` directory
	- Added `UtilTest.java`
- Added `org.junit.jupiter.junit-jupiter-api` dependency to the POM
- Added `JTerm.setPrompt()`
- `cd`/`chdir` command now changes input prompt
- Major refactor of code to prepare for text color in headless mode
	- Moved `RawConsoleInput.java`, `InputHandler.java`, and `Keys.java` classes to own package `jterm.io.input`
- Added `jterm.io.output` package
	- Added `CollectorPrinter.java`
	- Added `GUIPrinter.java`
	- Added `HeadlessPrinter.java`
	- Added `Printer.java`
	- Added `TextColor.java`
- Added `InputHander.java` test to tests
- Fixed autocomplete bugs
- Fixed Windows-based bugs
- Fixed GUI tab parser
- Closed [#45](https://github.com/Sergix/JTerm/issues/45)
- Closed [#49](https://github.com/Sergix/JTerm/issues/49)
- Closed [#59](https://github.com/Sergix/JTerm/issues/59)
- Closed [#60](https://github.com/Sergix/JTerm/issues/60)
- Closed [#68](https://github.com/Sergix/JTerm/issues/68)
- Closed [#40](https://github.com/Sergix/JTerm/issues/40)
- Closed [#47](https://github.com/Sergix/JTerm/issues/47) (PR #78)
- Closed [#48](https://github.com/Sergix/JTerm/issues/48)
- Closed [#52](https://github.com/Sergix/JTerm/issues/52) (wontfix)
- Closed [#53](https://github.com/Sergix/JTerm/issues/53)
- Closed [#71](https://github.com/Sergix/JTerm/issues/71) (PR #74)
- Closed [#73](https://github.com/Sergix/JTerm/issues/73)
- Closed [#75](https://github.com/Sergix/JTerm/issues/75)
- Closed PR [#77](https://github.com/Sergix/JTerm/pull/77) (PR #79)
- Closed [#80](https://github.com/Sergix/JTerm/issues/80)
- Closed [#81](https://github.com/Sergix/JTerm/issues/81)
- Closed [#94](https://github.com/Sergix/JTerm/issues/94)
- Closed PR [#82](https://github.com/Sergix/JTerm/pull/82) (PR #83)
- Merged [#58](https://github.com/Sergix/JTerm/pull/58) (@ojles)
- Merged [#61](https://github.com/Sergix/JTerm/pull/61) (@nanoandrew4)
- Merged [#67](https://github.com/Sergix/JTerm/pull/67) (@ojles)
- Merged [#74](https://github.com/Sergix/JTerm/pull/74) (@lbenedetto)
- Merged [#78](https://github.com/Sergix/JTerm/pull/78) (@nanoandrew4)
- Merged [#79](https://github.com/Sergix/JTerm/pull/79) (@ojles)
- Merged [#83](https://github.com/Sergix/JTerm/pull/83) (@Kaperskyguru)
- Merged [#85](https://github.com/Sergix/JTerm/pull/85) (@lbenedetto)
- Merged [#86](https://github.com/Sergix/JTerm/pull/86) (@nanoandrew4)
- Merged [#87](https://github.com/Sergix/JTerm/pull/87) (@lbenedetto)
- Merged [#88](https://github.com/Sergix/JTerm/pull/88) (@lbenedetto)
- Merged [#90](https://github.com/Sergix/JTerm/pull/90) (@nanoandrew4)
- Merged [#91](https://github.com/Sergix/JTerm/pull/91) (@nanoandrew4)
- Merged [#93](https://github.com/Sergix/JTerm/pull/93) (@lbenedetto)
- Merged [#95](https://github.com/Sergix/JTerm/pull/95) (@lbenedetto)
- Merged [#96](https://github.com/Sergix/JTerm/pull/96) (@DataSecs)
- Merged [#97](https://github.com/Sergix/JTerm/pull/97) (@lbenedetto)

## New Features

### GUI
A new GUI interface has been added by @lbenedetto! It can run on any graphical interface and replicates the looks of a terminal, and functions in almost perfect unison with its terminal alternative. The GUI runs by default; if you would like to run without it, just specifify by running JTerm with the `headless` option.

### Commands
The `download`, `rmdir`, `mv`/`move`, `rn`, and 'regex' commands have been added by @nanoandrew4, @lbenedetto, and @ojles. These command specifications have been listed in the "Active Command List" section.

## Active Command List
- `help [-h]` - displays information about JTerm
- `exit [-h]` - Exits the JTerm application
- `ls [-h] [-f]` - Dispays extensive directory information, including files and subdirectories (@pmorgan3)
	- `-f` - toggles minimal output
- `cd`, `chdir` `[-h] directory` - Changes the directory to the location specified
- `pwd [-h]` - prints the current working directory
- `md [-h] name` - creates a new directory
- `read [-h] file1 [file2 file3...]` - prints the contents of the specified files
- `delete`, `del`, `rm` `[-h] file/directory` - deletes the specified file or directory (@pmorgan3)
- `write [-h] filename` - Opens an input prompt that will write the data to the new file named `filename` 
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
- `rmdir [-h] [-r] directory` - removes the specified directory
	- `-r` - recursively deletes the directory and all its contents
- `exec executable` - executes the specified JAR or native executable
- `move, mv [-h] source destination` - moves the source file to the destination specified
- `rn [-h] file newName` - renames the specified file to the new name specified
- `download [-h] url` - downloads the file at the specified URL into the current working directory
- `regex [-m] "expression" command [commandOptions ...]`
	- `-m` - "multiline mode"; the output from the command is treated a single string with newline characters

> JTerm 0.7.0  
> `jterm-v0.7.0.jar`  
> This project and its source are held under the GNU General Public License, located in the LICENSE file in the project's directory.  
> (c) 2017  