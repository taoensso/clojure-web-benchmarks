#!/bin/bash

sudo sysctl -w \
    kern.maxfiles=100000 \
    kern.maxfilesperproc=50000 \
    net.inet.ip.portrange.first=1024 \
    net.inet.ip.portrange.hifirst=1024 \
    net.inet.tcp.blackhole=2 \
    net.inet.udp.blackhole=1 \
    kern.ipc.somaxconn=30000 \
    kern.ipc.maxsockbuf=800000 \
    net.inet.tcp.msl=1000
    # net.inet.icmp.icmplim=250

ulimit -n 50000
ulimit -S -n 50000
launchctl limit maxfiles 50000

echo | tee -a $OUT
echo "$(ulimit -a)" | tee -a $OUT
echo "$(launchctl limit)" | tee -a $OUT
echo | tee -a $OUT

ab -n 50000 -c 2000 -kr 127.0.0.1:8080/
