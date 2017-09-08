@echo off

cd target
if EXIST "jterm-0.4.1-jar-with-dependencies.jar" (
	ren jterm-0.4.1.jar jterm-0.4.1-no-deps.jar
	ren jterm-0.4.1-jar-with-dependencies.jar jterm-v0.4.1.jar
)
cd ..

java -cp target/jterm-v0.4.1.jar main.java.jterm.JTerm