#!/bin/bash

## sudo keep-alive
# sudo -v
# while true; do sudo -n true; sleep 60; kill -0 "$$" || exit; done 2>/dev/null &

if [[ $(/usr/bin/id -u) -ne 0 ]]; then
    echo "ABORTING: Please run as sudoer"
    exit
fi

bench_port() {
    if [ -z "$(sudo lsof -i :$1)" ]; then
        echo "Is the $1 server running?" | tee -a $OUT
    else
        echo
        echo "Port $1: Benching for $2x20=$(($2 * 20)) seconds..." | tee -a $OUT
        autobench --single_host --host1 localhost --uri1 / --port1 $1 --quiet \
            --low_rate 30 --high_rate 900 --rate_step 45 --num_call 48 \
            --timeout 5 --const_test_time $2 | tee -a $OUT
    fi
}

if [ -z "$1" ]; then
    echo "Usage: $0 per_port_bench_time_seconds [port]"
    echo "Recommend 10 for fast bench, >=60 for accurate bench."
    echo "Leave port unspecified to bench ALL server ports."
    exit 1
else

    ## OS Tuning
    # sudo sysctl -w kern.maxfiles=65536 kern.maxfilesperproc=32768 \
    #     net.inet.ip.portrange.first=1024 \
    #     net.inet.ip.portrange.hifirst=1024 \
    #     net.ipv4.ip_local_port_range="1025 65535" \
    #     net.inet.tcp.msl=7500
    # ulimit -n 32768
    # ulimit -S -n 32768
    # launchctl limit maxfiles 32768
    # sudo sysctl -w net.ipv4.tcp_tw_reuse=1

    sudo sysctl -w net.ipv4.tcp_tw_reuse=1
    ulimit -n 32768

    echo
    echo "$(ulimit -a)"
    echo "$(launchctl limit)"
    echo

    mkdir -p results
    OUT="results/$(date +%Y%m%d"-"%H-%M).tsv"
    echo "" > $OUT
    echo "Logging bench results to $OUT..."

    if [ -n "$2" ]; then
        bench_port $2 $1
    else

        # Bench reference server(s)
        bench_port 8080 $1

        # Bench Clojure servers
        for PORT in {8081..8095}
        do
            echo "Sleeping for 60s..."
            sleep 60
            bench_port $PORT $1
        done
    fi
    echo "Done benching! Results logged to $OUT."
fi
