#!/bin/sh
SCRIPT=$(find . -type f -name tapi-api-scope)
exec $SCRIPT $HMRC_CONFIG -Dhttp.port=7010
