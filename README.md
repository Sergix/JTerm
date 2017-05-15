# JTerm

### Definition
A root-like terminal written in Java for cross-platform usage.

### Implementation
This project is written in Java using Eclipse Neon JRE.

### Builds
Once you build a file from the source code to an archive (.jar), push it to the /build/jar folder, along with the associated changelog (see Changelog section), and the source code (see Source Code section).
The format of the archive's filename is as follows: "jterm-v(build version).jar"

### Source Code
Keep all updated and current source code in the /src directory, which should be updated with every build.
When pushing the source code with a build, keep the file names the same. If creating a new file, simply name the file as the topic of its contents. Keep the name clear and concise, and the file's contents in line with its name.
As mentioned in the "Builds" section, package the source code with every build into a ".zip" file that should be placed in the /build/src directory when pushing a build.
The format for the file should be as follows: "(build version)-src.zip".

### Changelog
Every time an update is pushed, the changelog will be updated and pushed as well.
It is highly recommended that every time you build you update the changelog. Once you add something to the changelog, DO NOT remove the entry if, and only if, it is incorrect.
The global changelog file is in the root folder of the project and is named "changelog.txt"; EVERY note will go into this file. It is also required that when you push a build you push an associated changelog file, formatted "(build version)-changelog.txt". This file should be pushed to the /build/changelog directory.
The format for a changelog entry is as follows:
`
[MM/DD/YYYY-HH:MM (version)]   
(entry contents)   
(entry contents)   
(newline before next entry)   
`

Timestamps should be in 24-hour (aka military) time.

### Documentation
Documentation should be published with every major release.
A "major" release is defined as one with the version number that corresponds to the following examples: "1.x", "2.x", "3.x"
Documentation guidelines are to be written in Markdown formatting. Guidelines are as follows:

- Main Header: \#
- Subheadings: \#\#\#
- Standard text
- Code blocks use markdown code block formatting
