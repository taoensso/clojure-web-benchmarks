#!/bin/bash

case $1 in
	1k-keepalive)
	  echo "Starting  Servers for benchmark with keepalive connections ranging  from 32 ~ 1024"
	  NGINX_MB_CONNS=1400
	  ##TODO: some optimizations for other server??? 
	  ;;
	1k-nonkeepalive)
	  echo "Starting  Servers for benchmark with non-keepalive connections ranging  from 32 ~ 1024"
	  NGINX_MB_CONNS=65000
	  ##TODO: some optimizations for other server??? 
	  ;;
	60k-keepalive)
	  echo "Starting  Servers for benchmark with keepalive connections ranging  from 10000 ~ 60000"
	  NGINX_MB_CONNS=65000
	  ##TODO: some optimizations for other server??? 
	  ;;
	60k-nonkeepalive)
	  echo "Starting  Servers for benchmark with non-keepalive connections ranging  from 10000 ~ 60000"
	  NGINX_MB_CONNS=65000
	  ##TODO: some optimizations for other server??? 
	  ;;
	*)
	  echo "Usage: scripts/run-servers.sh profile"
	  echo "profiles:"
	  echo "  1k-keepalive      Start  Servers for benchmark with keepalive connections ranging  from 32 ~ 1024"
	  echo "  1k-nonkeepalive   Start  Servers for benchmark with non-keepalive connections ranging  from 32 ~ 1024"
	  echo "  60k-keepalive     Start  Servers for benchmark with keepalive connections ranging  from 10000 ~ 60000"
	  echo "  60k-nonkeepalive  Start  Servers for benchmark with non-keepalive connections ranging  from 10000 ~ 60000"
	  exit 1
	  ;;
esac

source scripts/run-server-util.sh

mkdir -p logs/$1

##TODO: some optimizations for these server??? 
start_server  "embedded" $1
start_servlet "tomcat7"  $1
start_servlet "tomcat8"  $1
start_servlet "jetty7"   $1
start_servlet "jetty8"   $1
start_servlet "jetty9"   $1


start_nginx_clojure $NGINX_MB_CONNS $1
echo "If you cannot start nginx-clojure, please check jvm configuration in the file ../servers/nginx-clojure/conf/nginx.conf"

start_immutant   $1


tail -fn 0 logs/$1/run-servers

# killall java
# killall nginx
# ps aux | grep -e java -e nginx

