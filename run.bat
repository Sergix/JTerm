@echo off

cd target
if EXIST "jterm-0.5.0-jar-with-dependencies.jar" (
	ren jterm-0.5.0.jar jterm-0.5.0-no-deps.jar
	ren jterm-0.5.0-jar-with-dependencies.jar jterm-v0.5.0.jar
)
cd ..

java -cp target/jterm-v0.5.0.jar main.java.jterm.JTerm