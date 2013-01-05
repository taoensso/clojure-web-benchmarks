#!/bin/bash

if [[ $(/usr/bin/id -u) -ne 0 ]]; then
    echo "ABORTING: Please run as sudoer (required for OS tuning)"
    exit 1
fi

sleep_for() {
    echo "Sleeping for $1s..."
    sleep $1
}

tune_os() {
    echo "Tuning (OS X)..."
    echo
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
}

bench_port() {
    curl "http://127.0.0.1:$1/"
    if [ -z "$(sudo lsof -i :$1)" ]; then
        echo "ABORTING: Is the $1 server running?" | tee -a $OUT
    else
        echo "Benching port $1..." | tee -a $OUT
        ab -n 60000  -c 16 -rk "127.0.0.1:$1/" # Warmup
        sleep_for 10
        ab -n 120000 -c 16 -rk "127.0.0.1:$1/" | tee -a $OUT
        sleep_for 10
        ab -n 120000 -c 64 -rk "127.0.0.1:$1/" | tee -a $OUT
        sleep_for 10
        ab -n 120000 -c 96 -rk "127.0.0.1:$1/" | tee -a $OUT
        echo
        echo
    fi
}

tune_os

mkdir -p results
OUT="results/$(date +%Y%m%d"-"%H-%M)"
echo "Logging bench results to $OUT..."
echo

if [ -n "$1" ]; then
    bench_port $1 # Bench only a single, given port
else
    for PORT in {8080..8091}
    do
        bench_port $PORT
    done
fi
echo "Done benching! Results logged to $OUT."
exit 0
