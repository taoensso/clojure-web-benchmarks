(ns servers "Clojure web server benchmarks"
  {:author "Various contributors"}
  (:require
   [clojure.string         :as str]
   [compojure.core         :as compojure]
   [ring.adapter.jetty     :as jetty]
   [ring.adapter.simpleweb :as simpleweb]
   [ring.adapter.undertow  :as undertow]
   [immutant.web           :as immutant]
   [vertx.http             :as http]
   [vertx.embed            :as embed]
   ;; [ring.adapter.netty  :as netty]
   [netty.ring.adapter     :as netty]
   [aleph.http             :as aleph]
   [org.httpkit.server     :as http-kit]
   [taoensso.timbre        :as timbre]))

(timbre/refer-timbre)

(def properties (into {} (System/getProperties)))
(defn system [& keys] (str/join " " (map properties keys)))

(comment (system "os.name" "os.version"))

;;;; Handler

(def response
  {:status  200
   :headers {"content-type" "text/html"}
   :body    (slurp "../index.html")})

(defn handler [request] response)

;;;; Servers

(defonce servers (atom {}))
(defn start-server! [server-id port constructor-fn]
  (or (get @servers server-id)
    (let [server (constructor-fn port)]
      (swap! servers assoc server-id server)
      (infof "%s is running on port %s" server-id port)
      server)))

(defn -main [& args]
  (info "Starting up servers..."
    {:clojure (clojure-version)
     :os      (system "os.name" "os.version")
     :java    (system "java.version")
     :vm      (system "java.vm.name" "java.vm.version")})

  ;; :nginx-php 8081

  (start-server! :ring-jetty 8082
    (fn [port]
      (jetty/run-jetty handler {:port port :join? false :max-threads 200})))

  (start-server! :ring-simple 8083
    (fn [port]
      (simpleweb/run-simpleweb handler {:port port})))

  (start-server! :aleph 8084
    (fn [port]
      (aleph.netty/leak-detector-level! :disabled)
      (aleph/start-server handler {:port port :executor :none})))

  (start-server! :http-kit 8087
    (fn [port]
      (http-kit/run-server handler {:port port :queue-size 204800 :thread 8})))

  (start-server! :ring-netty 8089
    (fn [port]
      (netty/start-server handler
        {:port port :zero-copy true
         :channel-options { "child.tcpNoDelay" true}
         :max-http-chunk-length 1048576
         :number-of-handler-threads 16
         :max-channel-memory-size 1048576
         :max-total-memory-size 1048576})))

  ;; :tomcat7-servlet 8090
  ;; :jetty-7-servlet 8091
  ;; :jetty-8-servlet 8092
  ;; :jetty-9-servlet 8093
  ;; :nginx-clojure   8094
  ;; :immutant        8095

  (start-server! :ring-undertow 8096
    (fn [port]
      (undertow/run-undertow handler {:port port})))

  (start-server! :vertx 8097
    (fn [port]
      (embed/set-vertx! (embed/vertx))
      (-> (http/server)
          (http/on-request #(http/end (http/server-response %) (:body response)))
          (http/listen port))))

  ;; :tomcat8-servlet 8098

  (start-server! :immutant2 8099
    (fn [port]
      (immutant/run handler {:port port}))))
