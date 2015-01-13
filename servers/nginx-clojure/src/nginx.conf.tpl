# user www-data;
pid logs/nginx.pid;
worker_processes auto;
worker_rlimit_nofile 10240;

events {
        multi_accept on;
        accept_mutex_delay 5ms;
}



error_log logs/error.log;


http {
       max_balanced_tcp_connections #{max_balanced_tcp_connections};
       sendfile  on;
        tcp_nopush  on;
        tcp_nodelay on;
        #keepalive_timeout   65;
        keepalive_requests  10000000;
        server_tokens off;
        reset_timedout_connection on;
        send_timeout 5;

        default_type text/html;

        access_log off;
        #error_log /dev/null crit;

        gzip       off;

        jvm_options "-Djava.class.path=#{class-path}";


        jvm_path "#{jvm_shared_library_path}";

  server {
         listen       8094 deferred;
         server_name  .localhost;
         location / {
           content_handler_name 'nginx-clojure/handler ';
         }
         
        location /nginx_status {
                stub_status on;
        }
         
  }

}
