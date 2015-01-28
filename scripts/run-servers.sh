#!/bin/bash

source run-server-util.sh

mkdir -p logs



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
