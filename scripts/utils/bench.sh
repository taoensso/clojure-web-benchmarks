#!/bin/bash

source scripts/utils/common-utils.sh

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

        for CONN_NUM in $CONN_SET
        do
            wrk2 -t16 -c${CONN_NUM} -R$REQS -d30s -H "$ADD_HEADER" "http://127.0.0.1:$1/" # warmup
            sleep_for 10
            wrk2 -t16 -c${CONN_NUM} -R$REQS -d60s -H "$ADD_HEADER" \
                 --latency "http://127.0.0.1:$1/" | tee -a $OUT
            echo "-----${CONN_NUM}--DONE-----" | tee -a $OUT
        done

        echo "===============================================" | tee -a $OUT
        echo | tee -a $OUT
        echo | tee -a $OUT
    fi
}

case $1 in
    1k-keepalive)
        CONN_SET="32 64 128 256 512 1024"
        REQS=400000
        ADD_HEADER="Connection: keep-alive"
        IGNORED_PORTS="NONE"
        ;;
    1k-nonkeepalive)
        CONN_SET="32 64 128 256 512 1024"
        ## Use fewer connections for non-keepalive benchmark:
        REQS=30000
        ADD_HEADER="Connection: close"
        ## Skip http-kit (Ref. http://goo.gl/2XlGKm):
        IGNORED_PORTS="8087"
        ;;
    60k-keepalive)
        CONN_SET="10000 20000 30000 40000 60000"
        REQS=400000
        ADD_HEADER="Connection: keep-alive"
        ## Skip Immutant v1:
        IGNORED_PORTS="8095"
        ;;
    60k-nonkeepalive)
        CONN_SET="10000 20000 30000 40000 60000"
        ## Use fewer connections for non-keepalive benchmark:
        REQS=30000
        ADD_HEADER="Connection: close"
        ## Skip http-kit, Immutant v1
        IGNORED_PORTS="8087|8095"
        ;;
    *)
        echo "Usage: $0 <benching-profile> [single-port]"
        echo_profiles
        exit 1
	;;
esac

mkdir -p results/$1
OUT="results/$1/$(date +%Y-%m-%d_%H-%M.txt)"
echo "Logging bench results to $OUT..."
echo

if [ -n "$2" ]; then
    bench_port $2 # Bench only a single, given port
else
    for PORT in {8081..8099}
    do
        if [[ $PORT =~ $IGNORED_PORTS ]]; then
            echo "Skipping port $PORT due to benching profile config"
        else
            bench_port $PORT
        fi
    done
fi
echo "Done benching! Results logged to $OUT."
exit 0
