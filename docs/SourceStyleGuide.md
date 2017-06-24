# Source Code Style Guide

## Introduction
This document provides information on how to style elements in the source code, including naming, comments, and whitespace.

      The key words "MUST", "MUST NOT", "REQUIRED", "SHALL", "SHALL
      NOT", "SHOULD", "SHOULD NOT", "RECOMMENDED",  "MAY", and
      "OPTIONAL" in this document are to be interpreted as described in
      RFC 2119.

## Naming Conventions
This section describes how variables, functions, classes, and other elements are to be named within the code.

### Classes
Class names MUST meet the following criteria:
1. Clear and concise
2. Describe its contents
3. No underscores/dashes/other seperators
4. Use a capital letter for the first letter and when starting a new word (e.g. "WriteFile")  

The point of a class is to contain multiple related functions and variables, and to create objects that perform a specific function, not a myriad of different purposes.
For example, a constructor class called "File" MUST only contain functions and properties related to file reading, writing, and the like. It MUST NOT do unrelated things such as console I/O, networking functions, etc.
Therefore, the name represents the class's purpose, and abiding by these rules will increase efficiency when programming.

### Functions
Function naming rules are almost identical to that of a class, but here is the full criteria:
1. Clear and concise
2. Describe its contents
3. No underscores/dashes/other seperators
4. Use a capital letter for the first letter and when starting a new word (e.g. `PrintFile`)
5. Constructor class member functions use a lowercase on the first letter (e.g. `File.readChar`)

The only addition is the last rule, rule five. This rule ONLY applies to constructor classes. A "namespace" class's functions use rule four.

### Variables and Constants
Variable and constant names are also related to function and class naming. The full criteria is:
1. Clear and concise
2. Describe its contents, while trying to also include the type of variable (e.g. a variable containing a File object: `outputFile`)
3. No seperators except for underscores, but they MUST NOT be used as the first or last character (e.g. `output_file`)
5. If an underscore is used between words, the first letter of the latter word is not capitalized
6. Use a lowercase letter for the first letter, when starting a new word use a capital (e.g. `outputFile`)
7. Constants MUST use all capital letters (e.g. `PI`)
8. Both MUST NOT include numbers

The purpose of a variable is to store a value. These rules make sure that the word we use to refer to it is self-explanatory while concise and neat.

## Whitespace
Whitespace is used to seperate statements and to provide more readable code while looking professional.

### Functions, Conditionals, Variables
Whitespace MUST be used in the following ways:
1. There MUST NOT be space between the function name and the argument list (e.g. `public static void Test()`)
2. If/Else/For/While, and any conditional statement MUST use a space between the operation token and the parentheses; there MUST NOT be spaces in between the contents of the parentheses (e.g. `if (this)`)
3. A space MUST be used between variable names and operators (e.g. `int value = 1 + 1;`)
4. If using multi-level parenthetical statements, spaces MUST be used to seperate the statements and provide padding (e.g. `if ( (this) && (that) )`)
5. A newline MUST be used before the bracket of the method/conditional:
```
if (this)
{
```

### Newlines
Blank newlines MUST abide by the following criteria:
1. They are used to seperate different sections of code; keep related sections together. e.g.:
```
int value = 1 + 1;
System.out.println(value);

int second_value = 2 + 2;
System.out.println(second_value);
```
2. They are used after a function name, and after the last line of code inside the function. e.g.:
```
public static void Test()
{

    int value = 1;
    
}
```
3. They MUST NOT be used after comments. e.g.:
```
// Inserts the value "1" into the variable "input"
int input = 1;
```
4. They MUST NOT be used before sections of code in conditionals and methods other than functions, but a newline MUST be used after the section of code. e.g.:
```
while (this)
{
    DoThis();
    
}
```

### Tabs and Indents
Tabs and indents are used to define blocks of code, and code that is contained by a method or conditional. Tabs are interpreted differently by different programs; therefore it is dependent on how many times you hit the "TAB" key when indenting.  
As an overall rule, a single tab MUST be used to indent code one lever further in every case it is needed. e.g.:
```
class Test
{

  public static void main()
  {
    
    int value = 1;
    // Check if the value equals "1"
    if (value == 1)
    {
      return;
      
    }
    else
    {
      System.out.println("Value is not equal to 1!");
      
    }
    
  }
  
}
```

## Comments
Comments are used to describe sections of code to other developers to help better understand the code, or to make notes about the code, such as a function or object.
Comments MUST abide by the following criteria:  
1. A comment describing a section of code MUST be used before the section of code, and not be placed anywhere else. e.g.:
```
// Print "Hello!" to the console
System.out.println("Hello!");
```
2. In multi-line comments, every line must be preceded by a "*" character, and the first statement MUST be included on the second line, and the last statement MUST be on the next-to-last line of the comment. e.g.:
```
/*
* The following code inserts a value into the variable and then
* checks to see if the value equals the second statement.
*/
```

### Documentation Comments
Documentation comments are used to describe function definitions and other important code elements. They MUST abide by the following criteria:
1. They MUST be a multi-level code comment
2. It includes the name of the function as the first statement, with blank parenthesis after it. On the same line the return type is specified (e.g. `PrintFile() void`)
3. A comment newline is used, and the next line includes a short summary of the function's purpose; multiple lines of the comment MAY be used (e.g. `Prints the contents of a specified file to the console.`)
4. A comment newline is used after the before rule, and then a list of the function's arguments with their details MUST be included (e.g. `String filename - the name of the file to be read`)
5. If the function interprets command options, they MUST be included and formatted as in the example below
6. An example MAY be provided to display how the function could be used (e.g. `PrintFile("myFile.txt");`)
7. In the example section, multiple examples MAY be included, and each example is indented by two spaces. A line showing its return contents MAY also be included directly after the example line (without newlines between), indented as well by two spaces (e.g. `=> This is my file.`)
Here is an example of the correct usage of these rules:
```
/*
* PrintFile() void
* 
* Prints the contents of a specified file
* to the console.
*
* String filename - the name of the file to be read
* Boolean info    - whether the function should output
*                   extra information about the file
*
* -f
*     Changes the output format to only file and directory
*     names
* -h
*     Prints help information
*
* Examples
*
*   PrintFile("myFile.txt");
*     => This is my file.
*
*   PrintFile("myFile.txt", true);
*     => myFile.txt
*     => This is my file.
*/
```
These commenting rules are based on the [TomDoc](http://tomdoc.org/) specification.
