#!/bin/bash
mkdir -p logs
(nohup lein with-profile 1.5 run 1>>logs/run-servers 2>&1 &)
tail -fn 0 logs/run-servers
