@echo off

cd target
if EXIST "jterm-%JTERM_VERSION%-jar-with-dependencies.jar" (
	ren jterm-%JTERM_VERSION%.jar jterm-%JTERM_VERSION%-no-deps.jar
	ren jterm-%JTERM_VERSION%-jar-with-dependencies.jar jterm-v%JTERM_VERSION%.jar
)
cd ..

java -cp target/jterm-v%JTERM_VERSION%.jar jterm.JTerm
