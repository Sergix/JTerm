# JTerm Patch Notes Documentation v0.4.1

## Table of Contents
```
I. Table of Contents  
II. Overview  
III. Patch Notes  
IV. Changelog  
```

## Overview
This document provides information on version 0.4.1 of the JTerm project. Developer notes and the full changelog is included.

## Patch Notes
Patch version "0.4.1" adds the "Set" and "Pause" classes which were previously located in Exec. Their associated commands can now be called from the interpreter. The project README and CONTRIBUTING.md has been updated with some minor changes. On the development side, batch scripts ("init", "build", and "run") have been created to assist with the build process. You can now build and run JTerm without relying on an IDE such as Eclipse, and can develop from _any_ application of your choosing. See the full changelog below for more in-depth detail.

## Changelog
- Created Pause class
	- Pause.java
	- Contains all code previously located in Exec that was associated with the `pause` command
	- `EnterPause(options)`: pauses the interpreter until the Enter key is hit.
- Created Set class
	- Set.java
	- Contains all code previously located in Exec that was associated with the `set` command
	- `set` now can be called from outside Exec
	- `vars`: global program hashtable containing all variables
	- `NewVar(options)`: creates a new variable
	- `PrintVars()`: prints all variables currently stored
- Window class changes
	- Added `Window.CloseAll()` function (JTerm automatically calls `CloseAll()` right before exiting the application)
	- `Window.windows` array contains all active windows
- Created batch scripts for build and run processes
	- `init.bat`: initializes the environment for building
	- `build.bat`: builds with Maven (`mvn clean package`)
	- `run.bat`: runs the application
- The Maven package process now builds two JAR's: one w/o dependencies and one with.
	- If you perform the `run` batch script right after a build, it will rename the files to their respective names
	- Packaged JAR, no dependencies (before name change w/ `run`): `jterm-0.5.0.jar`
	- Packaged JAR, with dependencies (before name change w/ `run`): `jterm-0.5.0-jar-with-dependencies.jar`
	- Packaged JAR, no dependencies (after name change w/ `run`): `jterm-0.5.0-no-deps.jar`
	- Packaged JAR, with dependencies (after name change w/ `run`): `jterm-v0.5.0.jar`
- `Files.WriteFile()` input stream is closed when a blank line is entered
- Moved all non-JTerm-compatible switch cases from `Exec.Parse()` into `JTerm.Parse()`
- Added documentation comments for multiple functions
- Updated README and CONTRIBUTING
- Deleted the `Dir` and `readfile` branches
- Updated the POM to include build process changes
	- Maven now runs a special plugin that packages all dependencies into the binary (maven-assembly-plugin)
- Merged [#17](https://github.com/Sergix/JTerm/pull/17). Thanks to @d4nntheman!
- Fixed [#14](https://github.com/Sergix/JTerm/issues/14)
- Fixed [#15](https://github.com/Sergix/JTerm/issues/15)
- Fixed [#16](https://github.com/Sergix/JTerm/issues/16)
- Fixed [#20](https://github.com/Sergix/JTerm/issues/20)
- Minor Bug Fixes
	- `NewDir()` was not taking arguments with multiple spaces
	- `WriteFile()` was not taking arguments with multiple spaces
	- `Delete()` was not taking arguments with multiple spaces
	- `Echo()` was outputting an extra space
	- `ChangeDir()` accepts directory paths enclosed in quotes

> `JTerm 0.4.1`  
> `jterm-v0.4.1.jar`  
> This project and its source are held under the GNU General Public License, located in the LICENSE file in the project's directory.  
> (c) 2017  
