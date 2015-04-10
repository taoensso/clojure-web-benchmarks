#!/bin/bash

source scripts/utils/common-utils.sh

start_server() {
    cd servers/$1
    echo "Starting server in $(pwd)..."
    (nohup lein with-profile default trampoline run 1>>../../logs/$2/run-servers 2>&1 &)
    cd ../../
}

start_servlet() {
    cd servers/$1
    echo "Starting servlet in $(pwd)..."
    (nohup lein with-profile default trampoline servlet  run 1>>../../logs/$2/run-servers 2>&1 &)
    cd ../../
}

start_nginx_clojure() {
    cd servers/nginx-clojure
    echo "Prepare nginx-clojure in $(pwd)..."
    (nohup lein with-profile default trampoline  run $1 1>>../../logs/$2/run-servers 2>&1)
    echo "Starting nginx-clojure in $(pwd)..."
    ./nginx
    cd ../..
}

case $1 in
    ## 1k-keepalive|1k-non-keepalive|60k-keepalive|60k-non-keepalive)
    ## echo "Starting servers with profile: $1..."
    ## ;;
    1k-keepalive)
        echo "Starting servers with profile: $1..."
        NGINX_MB_CONNS=1400
        ## TODO Any optimizations for other servers?
        ;;
    1k-non-keepalive)
        echo "Starting servers with profile: $1..."
        NGINX_MB_CONNS=65000
        export UNDERTOW_DISPATCH=true
        ## TODO Any optimizations for other servers?
        ;;
    60k-keepalive)
        echo "Starting servers with profile: $1..."
        NGINX_MB_CONNS=65000
        ## TODO Any optimizations for other servers?
        ;;
    60k-non-keepalive)
        echo "Starting servers with profile: $1..."
        NGINX_MB_CONNS=65000
        export UNDERTOW_DISPATCH=true
        ## TODO Any optimizations for other servers?
        ;;
    *)
        echo "Usage: $0 <benching-profile>"
        echo_profiles
        exit 1
        ;;
esac

mkdir -p logs/$1

## TODO Any optimizations for these servers?
start_server  "embedded" $1
start_servlet "tomcat7"  $1
start_servlet "tomcat8"  $1
start_servlet "jetty7"   $1
start_servlet "jetty8"   $1
start_servlet "jetty9"   $1

start_nginx_clojure $NGINX_MB_CONNS $1
echo "If you cannot start nginx-clojure, please check jvm configuration in the file ../servers/nginx-clojure/conf/nginx.conf"

tail -fn 0 logs/$1/run-servers

# killall java
# killall nginx
# ps aux | grep -e java -e nginx
