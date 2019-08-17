# JTerm Roadmap to v1.0
#### by Sergix

## Table of Contents
```
I. Introduction
	A. Definitions
	B. Disclaimer
II. Fundamentals
III. Tracking
```

## Introduction
This document provides a general outline for what needs to be accomplished before the official "1.0.0" release of the JTerm project.

### Definitions
The key words "MUST", "MUST NOT", "REQUIRED", "SHALL", "SHALL NOT", "SHOULD", "SHOULD NOT", "RECOMMENDED", "MAY", and "OPTIONAL" in this document are to be interpreted as described in RFC 2119.

In the "Fundamentals" section of this document:

- "(I #_n_)" is interpreted as "Refer to Issue #_n_"
- "(PR #_n_)" is interpreted as "Refer to Pull Request #_n_"
- "(CL #_n_)" is interpreted as "Refer to ChangeLog entry #_n_"

### Disclaimer
Due to the ever-growing list of ideas of what to implement, this document MAY slightly change over time. The main aspects will stay the same, but smaller features may be added/removed based on the consent of the project maintainers.

## Fundamentals
This section provides a list of "foundational" elements that MUST be finished before anything else.

- [x] Create own package for command classes (CL #90)
- [x] Exit/quit (PR #21) (CL #81)
- [x] Echo (PR #4) (CL #37)
- [x] Pause (PR #21) (CL #75)
- [ ] Files
	- [x] Reading (I #16) (PR #17) (CL #68)
	- [x] Writing (PR #1) (CL #9)
	- [x] Moving (I #81) (PR #79) (CL #101)
	- [x] Deleting (PR #5) (CL #40)
	- [ ] Editing
	- [x] Renaming (PR #67) (CL #101)
- [x] Directories
	- [x] Listing (PR #2) (CL #17)
	- [x] Moving (PR #79) (CL #101)
	- [x] Change working directory (PR #3) (CL #28)
	- [x] Deleting (PR #67) (CL #101)
	- [x] Creating (PR #5) (CL #41)
	- [x] Renaming (PR #67) (CL #101)
- [x] Clear screen (I #33) (PR #44) (CL #91)
- [x] Full help information (I #40) (PR #79) (CL #101)
	- [x] Command help (`-h`/`help`) (I #40) (PR #79) (CL #101)
- [x] Run external executable programs (I #32) (PR #46) (CL #93)
- [x] Tab completion (I #31) (PR #34) (CL #88)
- [x] System process listing (PR #9) (CL #60)
- [ ] Edit prompt text
- [x] Date/time printing (I #49) (PR #54) (CL #96)
- [ ] Environment Variables (CL #74)
- [ ] Create new terminal window instance
- [x] Internet file downloader (I #45) (PR #61) (CL #96)

## Tracking
To track this list, it is placed in the [v1.0.0 Roadmap](https://github.com/Sergix/JTerm/milestone/1) milestone. All Issues and Pull Requests that are designated to complete a task on the milestone tracker should be marked as so.

> JTerm v0.7.0  
> `jterm-v0.7.0.jar`  
> This project and its source are held under the GNU General Public License, located in the LICENSE file in the project's directory.  
> (c) 2017
