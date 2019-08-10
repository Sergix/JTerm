#!/bin/bash
clear

rm -r target/

mvn -e clean package

./run.sh