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

        for CONN_NUM in 32 64 128 256 512 1024
        do
            wrk2 -t16 -c${CONN_NUM} -R400000 -d30s "http://127.0.0.1:$1/" # warmup
            sleep_for 10
            wrk2 -t16 -c${CONN_NUM} -R400000 -d60s \
                 --latency "http://127.0.0.1:$1/" | tee -a $OUT
            echo "-----${CONN_NUM}--DONE-----" | tee -a $OUT
        done

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
    for PORT in {8081..8099}
    do
        bench_port $PORT
    done
fi
echo "Done benching! Results logged to $OUT."
exit 0
