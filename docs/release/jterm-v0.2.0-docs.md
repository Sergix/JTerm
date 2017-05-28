# JTerm Documentation v0.2.0

## Table of Contents
```
I. Table of Contents   
II. Overview    
III. Build Targets  
IV. Changelog    
V. New Features  
    A. Commands  
    B. Styling
```

## Overview
This document provides information on changes included in release version "0.2.0". The JTerm project is far from its official release, but the project has had enough changes to include it as a new pre-release version. This document was written by @Sergix and reviewed by @NCSGeek.

## Build Targets
```
[VERSION]    [FILE]              [STATE]
0.1.0        jterm-v0.1.0.jar    OK
0.2.0        jterm-v0.2.0.jar    OK
```

## Changelog
- Created "Dir" class
  - Added "cd" command; called with Dir.ChangeDir() function
  - Added "pwd" command; called with Dir.PrintWorkingDir() function
  - Added "dir" command; called with Dir.PrintDir() function
- Reformatted code to comply with Source Style Guide
- Changed input prompt to simply be the working directory and a space (e.g. `/etc `)
- ALL functions that run a command take the "options" variable as the only method of input
- Created a "currentDirectory" global static variable in the JTerm class that is used to store the current working directory of the program.
- Commands now have command-line options (stored in the "options" variable passed to the function by Standby()) that are read from the console
- Other minor fixes

## New Features

## Commands
The commands `cd`, `pwd`, and `dir` have been added to JTerm. The syntax for these commands are as follows:
```
cd [-h] directory
pwd [-h]
dir [-f] [-h] [directory]
```
`-h` is ALWAYS displays the command's help information in the console. `-f`, used for the `dir` command, displays a simplified format of its output table.

### Styling
With the publishing of the Source Code Style Guide specifications, the source code as been mostly reformatted to comply with these guidelines. More information and reading can be found [here](https://github.com/Sergix/JTerm/blob/master/docs/SourceStyleGuide.md).

> JTerm 0.2.0  
> `jterm-v0.2.0.jar`  
> This project and its source are held under the GNU General Public License, located in the LICENSE file in the project's directory.  
> (c) 2017  
