#!/bin/sh
SCRIPT=$(find . -type f -name scapig-scope)
rm -f scapig-scope*/RUNNING_PID
exec $SCRIPT -Dhttp.port=7010 -J-Xms128M -J-Xmx512m
