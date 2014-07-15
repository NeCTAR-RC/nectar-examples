#!/bin/bash

# create a file to record the pid in
basePath=$("pwd")
pidFile="${basePath}/pids.pid"

if [ -f ${pidFile} ];
then
  echo "$pidFile already exists. Stop the process before attempting to start."
else
    # make the project
    mvn package

    # and run it...
    mvn exec:java -Dexec.mainClass=nz.co.paulo.Mainstay &

    # save the pid
    echo -n "$! " >> ${pidFile}
fi