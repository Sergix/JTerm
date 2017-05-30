# JTerm Patch Notes Documentation v0.2.1

## Table of Contents
I. Table of Contents  
II. Overview  
III. Patch Notes  
IV. Changelog  

## Overview
This document provides information on version 0.2.1 of the JTerm project. Release notes, developer notes, and the full changelog is included.

## Patch Notes
Update 0.2.1 of JTerm adds the "echo" command, which takes the input specified and outputs it to the console. It is a simplistic but useful command, as the creation of programmable scripts will soon come as an update into the project.

## Changelog
- Added "Echo" class file
  - Includes "echo" command; called with Echo.EchoInput()
  - Takes options variable as only input
  - Loops through and adds each output "word" from `options` into the local output variable, then prints that variable
  - Help output included
- Added "echo" command to JTerm command switch

> `JTerm 0.1.0`  
> `jterm-v0.1.0.jar`  
> This project and its source are held under the GNU General Public License, located in the LICENSE file in the project's directory.  
> (c) 2017  
