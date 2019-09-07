#!/bin/bash

cd target/ || exit

if [ -f jterm-"$JTERM_VERSION"-jar-with-dependencies.jar ]
then
	mv "jterm-$JTERM_VERSION.jar" "jterm-$JTERM_VERSION-no-deps.jar"
	mv "jterm-$JTERM_VERSION-jar-with-dependencies.jar" "jterm-$JTERM_VERSION.jar"
fi

java -jar "jterm-$JTERM_VERSION.jar" headless

cd ..