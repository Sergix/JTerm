# JTerm Patch Notes Documentation v0.5.1

## Table of Contents
```
I. Table of Contents  
II. Overview  
III. Patch Notes  
IV. Changelog  
```

## Overview
This document provides information on version 0.5.1 of the JTerm project. Developer notes and the full changelog is included.

## Patch Notes
Patch version "0.5.1" implements an optimized command parsing algorithm, which greatly increases efficiency and command parsing speed, and is designed to make creating new commands easier. This update also changes a lot of class function names that should be taken note of. See the full changelog below for details.

## Changelog
- The second part of a command is now the function name (i.e. `dir ls`) (except for single word commands, i.e. `echo` and `help`)
- Removed switch statement blocks in `Process()` functions
- Arguments for `Process()` functions are now `ArrayList<>`'s
- The method caller in `JTerm.Parse()` now passes an `ArrayList<>` rather than a string
- Changed multple function names.
	- `Dir`
		- `PrintDir()` -> `Ls()`
		- `ChangeDir()` -> `Cd()`, `Chdir()`
		- `PrintWorkingDir()` -> `Pwd()`
		- `NewDir()` -> `Md()`
	- `Files`
		- `WriteFile()` -> `Write()`
		- `ReadFile()` -> `Read()`
- Created `Files.Del()` and `Files.Rm()` function that are identical to `Files.Delete()`
- Removed the following functions, and moved their contents to their respective class constructors
		- `Echo.EchoInput()`
		- `Exit.ExitApp()`
		- `Help.PrintHelp()`
		- `Pause.EnterPause()`
		- `Ping.PingHost()`
		- `Ps.View()`
		- `Set.NewVar()`, `Set.PrintVar()`
- Minor fixes

> `JTerm 0.5.1`  
> `jterm-v0.5.1.jar`  
> This project and its source are held under the GNU General Public License, located in the LICENSE file in the project's directory.  
> (c) 2017  
