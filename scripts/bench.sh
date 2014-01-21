#!/bin/bash

#if [[ $(/usr/bin/id -u) -ne 0 ]]; then
#    echo "ABORTING: Please run as sudoer (required for OS tuning)"
#    exit 1
#fi

sleep_for() {
    echo "Sleeping for $1s..."
    sleep $1
}



bench_port() {
    curl "http://127.0.0.1:$1/"
    if [ -z "$(lsof -i :$1)" ]; then
        echo "ABORTING: Is the $1 server running?" | tee -a $OUT
    else
        echo "Benching port $1..." | tee -a $OUT
        ab -n 300000  -c 16 -rk "127.0.0.1:$1/" # Warmup
        sleep_for 10
        ab -n 300000 -c 16 -rk "127.0.0.1:$1/" | tee -a $OUT
        sleep_for 10
        ab -n 300000  -c 64 -rk "127.0.0.1:$1/" # Warmup
        sleep_for 10
        ab -n 300000 -c 64 -rk "127.0.0.1:$1/" | tee -a $OUT
        sleep_for 10
        ab -n 300000  -c 96 -rk "127.0.0.1:$1/" # Warmup
        sleep_for 10
        ab -n 300000 -c 96 -rk "127.0.0.1:$1/" | tee -a $OUT
        sleep_for 10
        ab -n 300000  -c 128 -rk "127.0.0.1:$1/" # Warmup
        sleep_for 10
        ab -n 300000 -c 128 -rk "127.0.0.1:$1/" | tee -a $OUT
        echo "===============================================" | tee -a $OUT
        echo | tee -a $OUT
        echo | tee -a $OUT
    fi
}


mkdir -p results
OUT="results/$(date +%Y%m%d"-"%H-%M)"
echo "Logging bench results to $OUT..."
echo

if [ -n "$1" ]; then
    bench_port $1 # Bench only a single, given port
else
    for PORT in {8081..8097}
    do
        bench_port $PORT
    done
fi
echo "Done benching! Results logged to $OUT."
exit 0
