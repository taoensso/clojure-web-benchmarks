#!/bin/bash
mkdir -p logs
# (nohup sudo scripts/bench.sh 1>>logs/run-benchmarks $@ 2>&1 &) # Not working?
(sudo nohup scripts/bench.sh 1>>logs/run-benchmarks $@ 2>&1 &)
tail -fn 0 logs/run-benchmarks
