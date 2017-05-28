# Patch Notes Documentation Style Guide

## Introduction
This document provides information on how to style documentation written for patch notes. A "patch" is defined as when a released build's version's patch number increments (e.g. `0.1.0`->`0.1.1`). See more information on versioning [here](https://github.com/mojombo/semver/blob/master/semver.md). The examples in this document are the literal formatting that would be used in Markdown. Therefore, if you see a # character in an example, it is not meaning to say that you put a # in the actual document (unless it does not abide by the standard Markdown rules or otherwise specified.)  

Styling specifications for headings and other elements are already included in the [Documentation Style Guide](https://github.com/Sergix/JTerm/blob/master/docs/DocStyleGuide.md), so there is no need to reiterate them. See the section titled "Formatting and Styling" in it for information.

The term "section", when used as a noun to describe a paragraph of information being written for the purpose of documenting, is defined as is in the "Formatting and Styling"->"Sections" paragrapth in the [Documentation Style Guide](https://github.com/Sergix/JTerm/blob/master/docs/DocStyleGuide.md).

> The key words "MUST", "MUST NOT", "REQUIRED", "SHALL", "SHALL NOT", "SHOULD", "SHOULD NOT", "RECOMMENDED", "MAY", and "OPTIONAL" in this  >document are to be interpreted as described in RFC 2119.  

## Documentation Contents
Patch note documentation MUST include the following sections as ordered:  
1. Table of Contents - see "Table of Contents" section in the [Documentation Style Guide](https://github.com/Sergix/JTerm/blob/master/docs/DocStyleGuide.md)
2. Overview - introduction describing documentation and its contents
3. Patch Notes - paragraph describing what was fixed; does not have to be detailed, just an overview
4. Changelog - formatted as shown in "Changelog" section below
5. Footer - see "Footer" section in the [Documentation Style Guide](https://github.com/Sergix/JTerm/blob/master/docs/DocStyleGuide.md)

### Changelog
The Changelog section is used to provide a list of changes made in the patch. The changelog is to be formatted as follows:
```
## Changelog
- Fixed issue #1234
  - Edited line number 123 in `file.java` to be `String filename` rather than `int filename`
  - Removed `fileChange()` function in `file.java`
...
...
```
Patch changelogs must be as descriptive as possible. _Every_ change, big or small, MUST be noted in a patch note documentation.
