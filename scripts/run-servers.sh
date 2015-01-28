#!/bin/bash

source run-server-util.sh

mkdir -p logs

start_servlet() {
    cd servers/$1
    echo "Starting servlet in $(pwd)..."
    (nohup lein with-profile 1.7 trampoline servlet  run 1>>../../logs/run-servers 2>&1 &)
    cd ../../
}

start_nginx_clojure() {
    cd servers/nginx-clojure
    echo "Prepare nginx-clojure in $(pwd)..."
    (nohup lein with-profile 1.7.0-alpha3 trampoline  run $1 1>>../../logs/run-servers 2>&1)
    echo "Starting nginx-clojure in $(pwd)..."
    ./nginx
    cd ../..
}

start_immutant() {
    cd servers/immutant
    export LEIN_IMMUTANT_BASE_DIR=.install
    echo "Starting Immutant in $(pwd)..."
    if [[ "$IMMUTANT" == "servlet" ]]; then
        (nohup lein with-profiles benchmark,servlet do compile, immutant server 8095 1>>../../logs/run-servers 2>&1 &)
    else
        (nohup lein with-profile benchmark immutant server 8095 1>>../../logs/run-servers 2>&1 &)
    fi
    cd ../..
}

start_server  "embedded"
start_servlet "tomcat7"
start_servlet "tomcat8"
start_servlet "jetty7"
start_servlet "jetty8"
start_servlet "jetty9"


start_nginx_clojure "1400"
echo "If you cannot start nginx-clojure, please check jvm configuration in the file ../servers/nginx-clojure/conf/nginx.conf"

start_immutant

tail -fn 0 logs/run-servers

# killall java
# killall nginx
# ps aux | grep java
