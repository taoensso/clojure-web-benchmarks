#!/bin/bash

mkdir -p logs/$1

case $1 in
1k-keepalive)
  echo "Starting  benchmark with keepalive connections ranging  from 32 ~ 1024"
  ;;
1k-nonkeepalive)
  echo "Starting  benchmark with non-keepalive connections ranging  from 32 ~ 1024"
  ;;
60k-keepalive)
  echo "Starting  benchmark with keepalive connections ranging  from 10000 ~ 60000"
  ;;
60k-nonkeepalive)
  echo "Starting  benchmark with non-keepalive connections ranging  from 10000 ~ 60000"
  ;;
*)
  echo "Usage: scripts/run-benchmarks.sh command"
  echo "commands:"
  echo "  1k-keepalive         Start  benchmark with keepalive connections ranging  from 32 ~ 1024"
  echo "  1k-nonkeepalive      Start  benchmark with non-keepalive connections ranging  from 32 ~ 1024"
  echo "  60k-keepalive        Start  benchmark with keepalive connections ranging  from 10000 ~ 60000"
  echo "  60k-nonkeepalive     Start  benchmark with non-keepalive connections ranging  from 10000 ~ 60000"
  exit 1
  ;;
esac


(nohup scripts/bench.sh 1>>logs/$1/run-benchmarks $@ 2>&1 &)
tail -fn 0 logs/$1/run-benchmarks
