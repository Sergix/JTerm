# Documentation Style Guide

## Introduction
This document provides information on how to format documentation published for new releases of the JTerm project. A new "release" is defined as a build where either its version "major" or "minor" section was increased (e.g. "1.2.3"->"1.3.0"). More information on versioning [here](https://github.com/mojombo/semver/blob/master/semver.md). If the build's patch version has incremented (e.g. ""->""), please see the [Patch Note Documentation Style Guide](), and use the rules stated in that document.
The examples in this document are the literal formatting that would be used in Markdown. Therefore, if you see a `#` character in an example, it is not meaning to say that you put a `#` in the actual document (unless it does not abide by the standard Markdown rules or otherwise specified.)

> The key words "MUST", "MUST NOT", "REQUIRED", "SHALL", "SHALL
> NOT", "SHOULD", "SHOULD NOT", "RECOMMENDED",  "MAY", and
> "OPTIONAL" in this document are to be interpreted as described in
> [RFC 2119](https://tools.ietf.org/html/rfc2119).

## Formatting and Styling
### File Name and Title
Documentation for versions are written in Markdown. The file name of the document MUST be formatted as follows:
`jterm-v1.0.0-docs.md`
Here, `1.0.0` is to be replaced with the version number of the associated release that the documentation is published for. The title of the document is to be:
`JTerm Documentation v1.0.0`
Where `1.0.0` is also to be replaced with the version number. The title is to be the very first line of the document. It is styled using first-level heading style (e.g. `# JTerm Documentation v1.0.0`).

### Headings
All headings MUST abide by these criteria:
1. Document titles are first-level headings (`#`)
2. Section titles are second-level headings (`##`)
3. Section sub-headings are third-level headings (`###`)

### Sections
A "section" is a block of text with included styling that explains or discusses a specific topic. Sections MUST be seperated by a blank newline. Section titles SHOULD be short and clear, and MUST NOT be sentences. Sections should be formatted as follows:
```
## Language Specifications
This section provides various resources on the programming language and its compiler.

### Language Tokens
The following tokens are reserved by the language:
...
...
```
## Documentation Contents
Release and development documentation MUST include the following sections in the following order, with any amount of extra sections describing new features and implementations in-between. Required documentation contents are as follows, and in the following order:
1. Table of Contents - formatted as shown in the section "Table of Contents"
2. Overview - introduction describing the documentation and its contents
3. Build Targets - formatted as shown in the section "Build Targets"
4. Changelog - formatted as shown in the section "Changelog"
5. Document contents are placed here (all other sections pertaining to release information)
6. Footer - formatted as shown in the section "Footer"
All of these sections must be included in the Table of Contents of the document.

### Table of Contents
The table of contents' listing MUST be formatted as follows:
```
I. Top-level
    A. Second-level
        1. Third-level
            a. Fourth-level
            b. Fourth-level
                - Note
        2. Third-level
    B. Second-level
II. Top-level
```
An implied rule for this listing format is, except for top-level, "where there is an A, there is a B; where there is a 1, there is a 2, etc." If there is only one point on a level, and it does not have sub-levels, use the format as show for "Note" above: the line is preceded by the "-" character.
The table of contents MUST include _every_ section in the document, including the required sections. It must also include itself as the first point.

### Build Targets
The purpose of the Build Targets section is to list all versions published of the project at the time of writing. This section is formatted in 3 columns, each seperated by a couple tabs. The first column is `[VERSION]`, which is the version number of the release. The second column is `[FILE]`, which specifies the name of the associated build located in the `/build/jar` directory. The last column is `[STATE]`, and it specifies whether a build is still active and supported for use (`OK`), or if there are known issues that prevent it from being fully accessible (`DEPRECATED`). An example for this section is as follows:
```
## Build Targets
[VERSION]    [FILE]              [STATE]
0.1.0        jterm-v0.1.0.jar    DEPRECATED
0.2.0        jterm-v0.2.0.jar    OK
```
This section, except for the title, is formatted using Markdown code block formatting.

### Changelog
The Changelog section is used to specify what has been added, removed, or modified since the last build. The changelog section is simply formatted using Markdown bulleted lists. An example for this section is as follows:
```
## Changelog
- Edited file "123.txt"
- Changed some other stuff
- Moved file "321.java" to /src/test directory
...
...
```
Of course, a real changelog would be much more descriptive.

### Footer
The footer is to provide a sort of "watermark" containing copyright and license information. It should be formatted as in the following example:
```
JTerm 0.1.0
jterm-v0.1.0.jar
This project and its source are held under the GNU General Public License, located in the LICENSE file in the project's directory.
(c) 2017
```
Where here, `0.1.0` is the version number of the release.
