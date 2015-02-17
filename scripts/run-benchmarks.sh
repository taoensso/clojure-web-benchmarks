#!/bin/bash

source scripts/utils/common-utils.sh

case $1 in
    1k-keepalive|1k-nonkeepalive|60k-keepalive|60k-nonkeepalive)
        echo "Starting benchmarks with profile: $1..."

        mkdir -p logs/$1
        (nohup scripts/utils/bench.sh 1>>logs/$1/run-benchmarks $@ 2>&1 &)
        tail -fn 0 logs/$1/run-benchmarks
        ;;
    *)
        echo "Usage: $0 <benching-profile>"
        echo_profiles
        exit 1
        ;;
esac

exit 0
