@echo off
cls

rd /S /Q target

mvn -e clean package
run