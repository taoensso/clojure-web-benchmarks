#!/bin/bash
mkdir -p logs
# (nohup sudo scripts/bench-nonkeepalive.sh 1>>logs/run-benchmarks-nonkeepalive $@ 2>&1 &) # Not working?
(nohup scripts/bench-nonkeepalive.sh 1>>logs/run-benchmarks-nonkeepalive $@ 2>&1 &)
tail -fn 0 logs/run-benchmarks-nonkeepalive
