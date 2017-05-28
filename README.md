![JTerm](https://sergix.github.io/img/jterm.png)

![Build Status](https://travis-ci.org/Sergix/JTerm.svg?branch=master)

## Definition
A root-like terminal written for cross-platform usge.

## Implementation
This project is written in Java using [Eclipse IDE for Java EE](http://www.eclipse.org/downloads/packages/eclipse-ide-java-ee-developers/neon3).

## Builds
Once you build a file from the source code to an archive (.jar), push it to the /build/jar folder, along with the associated changelog (see Changelog section), and the source code (see Source Code section).
The format of the archive's filename is as follows: "jterm-v(build version).jar"

## Source Code
Keep all updated and current source code in the /src directory, which should be updated with every build.  
Please review the [Source Code Style Guide](https://github.com/Sergix/JTerm/blob/master/docs/SourceStyleGuide.md) for more information.
Subfolders are formatted for packaging and for CI integration.  
When pushing the source code with a build, DO NOT change the filenames. When creating a new class, place it in a new file with the name of the class as the filename (e.g. "class WriteFile", "WriteFile.java".) Keep the name clear and concise, and the file's contents in line with its name.
As mentioned in the "Builds" section, package the source code with every build into a ".zip" file that should be placed in the /build/src directory when pushing a build. Subfolders in the /src directory (e.g. /src/main/java/...) should be included in packaged source.
The format for the file should be as follows: "jterm-src-(build version).zip".

## Changelog
Every time an update is pushed, the changelog will be updated and pushed as well.
It is highly recommended that every time you build you update the changelog. Once you add something to the changelog, DO NOT remove the entry, unless if, and only if, it is incorrect (such as a typo in the version number.)
The global changelog file is in the root folder of the project and is named "changelog.txt"; EVERY note will go into this file. It is also required that when you push a build you push an associated changelog file, formatted "jterm-changelog-(build version).txt". This file should be pushed to the /build/changelog directory.
New changelog entries in the global "changelog.txt" file should be placed at the _top_ of the file, not the bottom. Simply create a few newlines before the latest entry and use the newlines to enter the new entry information. Entries must also be tab-indented.
The format for a changelog entry is as follows:
```
[MM/DD/YYYY-HH:MM (version) (username)]   
	(entry contents)   
	(entry contents)  
	(blank newline)  
```
Here is an example changelog:  
```
[2/2/2017-3:33 1.3 NCSGeek]     
	information uploaded.   
	added files.   
	pushed to github.   

[1/1/2017-3:33 1.0 Sergix]     
	information uploaded.  
	pushed to github.  

```
Of course, a real changelog would be more descriptive in its entries.

Timestamps should be in 24-hour (aka military) time.

## Versioning
Version numbers should use [Semantic Versioning](https://github.com/mojombo/semver/blob/master/semver.md).  
Numbering versions on files (such as released builds) should take the format of "vX.Y.Z", where "X.Y.Z" is the version.

## Documentation
Documentation is published with every:
- Major and minor release ([Documentation Style Guide](https://github.com/Sergix/JTerm/blob/master/docs/DocStyleGuide.md))
- Patch ([Patch Notes Documentation Style Guide](https://github.com/Sergix/JTerm/blob/master/docs/PatchDocStyleGuide.md))
