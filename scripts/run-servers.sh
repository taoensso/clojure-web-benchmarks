#!/bin/bash
mkdir -p logs

start_server() {
    cd servers/$1
    echo "Starting server in $(pwd)..."
    (nohup lein with-profile 1.5 trampoline run 1>>../../logs/run-servers 2>&1 &)
    cd ../../
}

start_servlet() {
    cd servers/$1
    echo "Starting servlet in $(pwd)..."
    (nohup lein with-profile 1.5 trampoline servlet run 1>>../../logs/run-servers 2>&1 &)
    cd ../../
}

start_server  "embedded"
start_servlet "tomcat7"
start_servlet "tomcat8"
start_servlet "jetty7"
start_servlet "jetty8"
start_servlet "jetty9"
echo "Please start reference (nginx) server manually."

tail -fn 0 logs/run-servers

# killall java
# ps aux | grep java
