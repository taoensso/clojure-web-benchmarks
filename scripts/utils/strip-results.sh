#!/bin/bash

if [ $# -ne 1 ]; then
    echo "Usage: $0 <file>"
    exit 1
else
    egrep "(Running|Latency Distribution|requests|Socket errors|connections|%|\-+$|#|Requests|Transfer|=+$)" $1 > "$1-stripped"
fi

exit 0
