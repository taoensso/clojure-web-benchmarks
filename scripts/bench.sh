#!/bin/bash

if [[ $(/usr/bin/id -u) -ne 0 ]]; then
    echo "ABORTING: Please run as sudoer (required for OS tuning)"
    exit 1
fi

tune_os() {
    echo "Tuning OS..."
    echo
    sudo sysctl -w \
        kern.maxfiles=65536 \
        kern.maxfilesperproc=32768 \
        net.inet.ip.portrange.first=1024 \
        net.inet.ip.portrange.hifirst=1024 \
        net.ipv4.ip_local_port_range="1025 65535" \
        net.ipv4.tcp_keepalive_time=1800 \
        net.inet.tcp.msl=7500 \
        net.ipv4.tcp_max_syn_backlog=3240000 \
        net.core.somaxconn=3240000 \
        net.ipv4.tcp_max_tw_buckets=1440000 \
        net.ipv4.tcp_tw_recycle=1
        # net.ipv4.tcp_tw_reuse=1
    ulimit -n 32768
    ulimit -S -n 32768
    launchctl limit maxfiles 32768

    echo
    echo "$(ulimit -a)"
    echo "$(launchctl limit)"
    echo
}

bench_port() {
    curl "http://127.0.0.1:$1/" | tee -a $OUT
    if [ -z "$(sudo lsof -i :$1)" ]; then
        echo "ABORTING: Is the $1 server running?" | tee -a $OUT
    else
        echo "Benching port $1..." | tee -a $OUT
        ab -n 50000  -c 16 -k "127.0.0.1:$1/" # Warmup
        ab -n 100000 -c 16 -k "127.0.0.1:$1/" | tee -a $OUT
        ab -n 100000 -c 64 -k "127.0.0.1:$1/" | tee -a $OUT
        echo
        echo
    fi
}

tune_os

mkdir -p results
OUT="results/$(date +%Y%m%d"-"%H-%M)"
echo "Logging bench results to $OUT..."
echo
echo

if [ -n "$1" ]; then
    bench_port $1 # Bench only a single, given port
else
    for PORT in {8080..8091}
    do
        echo "Sleeping for 10s..."
        sleep 10
        bench_port $PORT
    done
fi
echo "Done benching! Results logged to $OUT."
exit 0
