#!/bin/bash

if [ $# -ne 1 ]; then
    echo "Usage: $0 <file>"
    exit 1
else
    egrep "(Server Port|Requests per second|=+$)" $1 > "$1.stripped"
fi
