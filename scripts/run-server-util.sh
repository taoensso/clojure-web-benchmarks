#!/bin/bash

start_server() {
    cd servers/$1
    echo "Starting server in $(pwd)..."
    (nohup lein with-profile 1.5 trampoline run 1>>../../logs/$2/run-servers 2>&1 &)
    cd ../../
}

start_servlet() {
    cd servers/$1
    echo "Starting servlet in $(pwd)..."
    (nohup lein with-profile 1.5 trampoline servlet  run 1>>../../logs/$2/run-servers 2>&1 &)
    cd ../../
}

start_nginx_clojure() {
    cd servers/nginx-clojure
    echo "Prepare nginx-clojure in $(pwd)..."
    (nohup lein with-profile 1.7.0-alpha3 trampoline  run $1 1>>../../logs/$2/run-servers 2>&1)
    echo "Starting nginx-clojure in $(pwd)..."
    ./nginx
    cd ../..
}

start_immutant() {
    cd servers/immutant
    export LEIN_IMMUTANT_BASE_DIR=.install
    echo "Starting Immutant in $(pwd)..."
    if [[ "$IMMUTANT" == "servlet" ]]; then
        (nohup lein with-profiles benchmark,servlet do compile, immutant server 8095 1>>../../logs/$1/run-servers 2>&1 &)
    else
        (nohup lein with-profile benchmark immutant server 8095 1>>../../logs/$1/run-servers 2>&1 &)
    fi
    cd ../..
}
