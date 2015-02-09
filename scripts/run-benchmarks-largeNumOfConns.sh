#!/bin/bash
mkdir -p logs
# (nohup sudo scripts/bench-largeNumofConns.sh 1>>logs/run-benchmarks-largeNumofConns $@ 2>&1 &) # Not working?
(nohup scripts/bench-largeNumofConns.sh 1>>logs/run-benchmarks-largeNumofConns $@ 2>&1 &)
tail -fn 0 logs/run-benchmarks-largeNumofConns
