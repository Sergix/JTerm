# Contributing to JTerm
First off, thanks for being interested in our project! We put a lot of hard work into making JTerm an accessible but powerful program that can be used by anyone.  

These guidelines should be reviewed before contributing to the JTerm project.

## About
JTerm is a cross-platform terminal. The project is written in Java so that it can be run on virtually any computer. This document provides information such as project resources, guidelines, and other information needed to start programming for JTerm.

## Implementation
This project is written in Java using [Eclipse IDE for Java EE](http://www.eclipse.org/downloads/packages/eclipse-ide-java-ee-developers/neon3).

## Code of Conduct
The Code of Conduct for any contributions and posting information can be found [here](/CODE_OF_CONDUCT.md).

## GitHub Branch
The development branch of the JTerm project is the `dev` branch. _**Make sure to work off of this branch, not the master!**_ All commits on the dev branch are tracked in the 'Dev' PR. This is the PR that is merged into master.

## Builds
Once you build a file from the source code to an archive (.jar), push it to the /build/jar folder, along with the associated changelog (see Changelog section), source code (see Source Code section), and documentation (see Documentation section.)

The format of the archive's filename is as follows: "jterm-v(build version).jar"

## Source Code
Keep all updated and current source code in the /src directory, which should be updated with every build. Please review the [Source Code Style Guide](https://github.com/Sergix/JTerm/blob/master/docs/SourceStyleGuide.md) for more information.

Subfolders are formatted for packaging and for CI integration.

When pushing the source code with a build, DO NOT change the filenames. As mentioned in the "Builds" section, package the source code with every build into a ".zip" file and a Wrapfile that should be placed in the /build/src directory when pushing a build. Subfolders in the /src directory (e.g. /src/main/java/...) should be included in packaged source. Wrapfiles should only include sources in the `main/` source directory. The format for the file should be as follows: `jterm-src-v(version).zip`, and `jterm-src-v(version).wrap`.

## Versioning
Version numbers should use [Semantic Versioning](https://github.com/mojombo/semver/blob/master/semver.md).  
Numbering versions on files (such as released builds) should take the format of "vX.Y.Z", where "X.Y.Z" is the version.

## Documentation
Documentation is published with every:
- Major and minor release ([Documentation Style Guide](https://github.com/Sergix/JTerm/blob/master/docs/DocStyleGuide.md))
- Patch ([Patch Notes Documentation Style Guide](https://github.com/Sergix/JTerm/blob/master/docs/PatchDocStyleGuide.md))

## Styling
Source code, documentation, and patch note styling can be found here:  
- [Source Code Style Guide](https://github.com/Sergix/JTerm/blob/master/docs/SourceStyleGuide.md)
- [Documentation Style Guide](https://github.com/Sergix/JTerm/blob/master/docs/DocStyleGuide.md)
- [Patch Note Documentation Style Guide](https://github.com/Sergix/JTerm/blob/master/docs/PatchDocStyleGuide.md)

Any of these types of files MUST be formatted as shown in these documents.

## Changelog
Every time an update is pushed, the changelog will be updated and pushed as well. It is highly recommended that every time you build you update the changelog. 

Once you add something to the changelog, DO NOT remove the entry, unless if, and only if, it is incorrect (such as a typo in the version number.) If you change a previous entry you must include a statement stating your change in a seperate entry.

The global changelog file is in the root folder of the project and is named "changelog.txt"; EVERY note will go into this file. It is also required that when you push a build you push an associated changelog file, formatted "jterm-changelog-(build version).txt". This file should be pushed to the /build/changelog directory. 

Changelog entry headings must be formatted as shown in the given example below. New changelog entries in the global "changelog.txt" file should be placed at the _top_ of the file, not the bottom. Simply create a few newlines before the latest entry and use the newlines to enter the new entry information. Entries must also be tab-indented.

The format for a changelog entry is as follows:
```
(#(entry))[MM/DD/YYYY-HH:MM (version) (username)]   
	(entry contents)   
	(entry contents)  
	(blank newline)  
```
Here is an example changelog:  
```
(#2)[2/2/2017-3:33 1.3 NCSGeek]     
	information uploaded.   
	added files.   
	pushed to github.   

(#1)[1/1/2017-3:33 1.0 Sergix]
	information uploaded.  
	pushed to github.  

```
Of course, a real changelog would be more descriptive in its entries.

Timestamps should be in 24-hour (aka military) time.

## Pull Requests
Pull Requests, of course, MUST be opened on a new branch. The branch and PR name must be short but descriptive of its topic. A description must be made of the PR's purpose and a short description of the added code. Every PR must request to be reviewed by @Sergix or @NCSGeek. Travis CI is also used in this project (dashboard can be found [here](https://travis-ci.org/Sergix/JTerm)), and PR's are automatically checked by Travis CI to make sure they are ready to be deployed.  

If the PR looks ready to go, it will be merged into the master branch. Documentation will then need to be written for the released code, whether it is a major, minor, or patch (view the "Styling" section above for more information.) This documentation will either be published to the `/docs/release` folder or the `/docs/patch` folder. It is requested that you write the documentation yourself (abiding by the guidelines above.)

## Issues
If a new bug or issue is found, create a new Issue! The issue name must be short but descriptive, and must contain the following sections:
1. "Description of Problem" - describe using words and/or pictures in full detail the problem you are experiencing.
2. "System Information" - include the following:
  - Version of Java you are running
  - Operating System version
  - Version of JTerm
3. "Files and Code" - If you find where the issue is or believe it to be, put the filenames and/or code in this section.

> JTerm v0.7.0  
> `jterm-v0.7.0.jar`  
> This project and its source are held under the GNU General Public License, located in the LICENSE file in the project's directory.  
> (c) 2017