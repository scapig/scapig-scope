#!/bin/sh
SCRIPT=$(find . -type f -name scapig-api-scope)
rm -f scapig-api-scope*/RUNNING_PID
exec $SCRIPT -Dhttp.port=7010
