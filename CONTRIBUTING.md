# Contributing to JTerm
First off, thanks for being interested in our project! We put a lot of hard work into making JTerm an accessible but powerful program that can be used by anyone.  

These guidelines should be reviewed before contributing to the JTerm project.

## About
JTerm is a cross-platform terminal. The project is written in Java so that it can be run on virtually any computer. This document provides information such as project resources, guidelines, and other information needed to start programming for JTerm.

## Code of Conduct
The Code of Conduct for any contributions and posting information can be found [here](/CODE_OF_CONDUCT.md).

## Styling
Source code, documentation, and patch note styling can be found here:  
- [Source Code Style Guide](https://github.com/Sergix/JTerm/blob/master/docs/SourceStyleGuide.md)
- [Documentation Style Guide](https://github.com/Sergix/JTerm/blob/master/docs/DocStyleGuide.md)
- [Patch Note Documentation Style Guide](https://github.com/Sergix/JTerm/blob/master/docs/PatchDocStyleGuide.md)

Any of these types of files MUST be formatted as shown in these documents.

## Changelog
Any changes, in any branch, MUST be pushed to the changelog file found on the master branch called "changelog.txt". View the "Changelog" section in the [README](/README.md) for more information. Do not push updates to changelog files on other branches; the master one is to be the only one changed and updated.

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
