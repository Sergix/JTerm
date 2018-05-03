@echo off

set option=h
cd target/

if exist "jterm-%JTERM_VERSION%-jar-with-dependencies.jar" (
	ren jterm-%JTERM_VERSION%.jar jterm-%JTERM_VERSION%-no-deps.jar
	ren jterm-%JTERM_VERSION%-jar-with-dependencies.jar jterm-%JTERM_VERSION%.jar
)

if not exist "jterm-%JTERM_VERSION%.jar" (
	exit
)

if "%1"=="%option%" (
	java -jar jterm-%JTERM_VERSION%.jar headless
) else (
	java -jar jterm-%JTERM_VERSION%.jar
)

cd ..