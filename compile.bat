@echo off
echo Compiling JTerm class---version %1
javac -d src/classpath src/JTerm.java
cls
cd src/classpath
java JTerm
cd ../..