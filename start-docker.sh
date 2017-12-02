#!/bin/sh
SCRIPT=$(find . -type f -name tapi-api-scope)
exec $SCRIPT -Dhttp.port=7010
