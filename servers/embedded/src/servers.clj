(ns servers "Clojure web server benchmarks"
  {:author "Various contributors"}
  (:require
   [clojure.string         :as str]
   [compojure.core         :as compojure]
   [taoensso.timbre        :as timbre]
   ;;
   [ring.adapter.jetty     :as jetty]
   [ring.adapter.undertow  :as undertow]
   [immutant.web           :as immutant]
   [immutant.web.undertow  :as immutant-undertow]
   [vertx.http             :as vhttp]
   [vertx.embed            :as vembed]
   [aleph.http             :as aleph]
   [org.httpkit.server     :as http-kit]))

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

  ;; :nginx-php 8081 (removed, not necessary to benchmark)

  (start-server! :ring-jetty 8082
    (fn [port]
      ;; No special config necessary for many-core systems?
      (jetty/run-jetty handler {:port port :join? false :max-threads 100})))

  ;; :ring-simple 8083 (server removed, no longer maintained)

  (start-server! :aleph 8084
    (fn [port]
      ;; No special config necessary for many-core systems
      (aleph.netty/leak-detector-level! :disabled)
      (aleph/start-server handler {:port port :executor :none})))

  ;; :aloha 8085 (server removed, no longer maintained)

  (start-server! :http-kit 8087
    (fn [port]
      ;; :queue-size - default 20k; should be at least as large as the number of clients
      ;; :thread     - default 4; used for application logic, network IO is
      ;;               always single-threaded
      (http-kit/run-server handler {:port port :queue-size 204800 :thread 4})))

  ;; :ring-netty      8089 (server removed, no longer maintained)
  ;; :tomcat7-servlet 8090
  ;; :jetty-7-servlet 8091
  ;; :jetty-8-servlet 8092
  ;; :jetty-9-servlet 8093
  ;; :nginx-clojure   8094
  ;; :immutant        8095

  (start-server! :ring-undertow 8096
    (fn [port]
      ;; TODO Any special config necessary for manycore systems?
      (undertow/run-undertow handler {:port port})))

  (start-server! :vertx 8097
    (fn [port]
      ;; TODO Any special config necessary for many-core systems?
      (vembed/set-vertx! (vembed/vertx))
      (-> (vhttp/server)
          (vhttp/on-request #(vhttp/end (vhttp/server-response %) (:body response)))
          (vhttp/listen port))))

  ;; :tomcat8-servlet 8098

  (start-server! :immutant2 8099
    (fn [port]
      ;; threads should be roughly 1/3 the number of available cores
      (immutant/run handler (immutant-undertow/options
                               :port port
                               :io-threads 8
                               :worker-threads 8)))))
