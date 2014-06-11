#!/bin/sh

# make the project
mvn package
# and run it...
mvn exec:java -Dexec.mainClass=nz.co.paulo.Mainstay