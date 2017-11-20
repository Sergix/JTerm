@echo off

set option="nogui"

cd target
if exist "jterm-%JTERM_VERSION%-jar-with-dependencies.jar" (
	ren jterm-%JTERM_VERSION%.jar jterm-%JTERM_VERSION%-no-deps.jar
	ren jterm-%JTERM_VERSION%-jar-with-dependencies.jar jterm-v%JTERM_VERSION%.jar
)

if not exist "jterm-v%JTERM_VERSION%.jar" (
	exit
)

if "%1" EQU %option% (
	java -jar jterm-v%JTERM_VERSION%.jar headless
) else (
	java -jar jterm-v%JTERM_VERSION%.jar
)

cd ..