#!/bin/bash

source scripts/run-server-util.sh

mkdir -p logs



start_server  "embedded"
start_servlet "tomcat7"
start_servlet "tomcat8"
start_servlet "jetty7"
start_servlet "jetty8"
start_servlet "jetty9"


start_nginx_clojure "65000"
echo "If you cannot start nginx-clojure, please check jvm configuration in the file ../servers/nginx-clojure/conf/nginx.conf"

## immutant 1 has too many error under large number of connections so we just remove it now
#start_immutant

tail -fn 0 logs/run-servers

# killall java
# killall nginx
# ps aux | grep java
