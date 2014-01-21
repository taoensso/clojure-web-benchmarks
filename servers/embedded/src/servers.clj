(ns servers "Clojure web server benchmarks"
  {:author "Peter Taoussanis"}
  (:require [clojure.string          :as str]
            [compojure.core          :as compojure]
            [ring.adapter.jetty      :as jetty]
            [ring.adapter.simpleweb  :as simpleweb]
            [ring.adapter.undertow   :as undertow]
            [vertx.http              :as http]
            [vertx.embed             :as embed]
;            [ring.adapter.netty      :as netty]
            [netty.ring.adapter      :as netty]
            [lamina.core             :as lamina]
            [aleph.http              :as aleph]
            [aloha.core              :as aloha]
            [org.httpkit.server :as http-kit]
            [taoensso.timbre :as timbre :refer (trace debug info warn error report)]
            [taoensso.timbre.profiling :as profiling :refer (p profile)]))

(defonce servers (atom {}))
(def response {:status 200 :headers {"content-type" "text/html"}
               :body (slurp "../index.html")})

;;;; Handlers

(defn handler             [request] response)
(defn aleph-handler-async [channel request] (lamina/enqueue channel response))
(def  aleph-handler-sync  (aleph/wrap-ring-handler handler))

;(http-kit/defasync http-kit-async [request] cb (cb response))

;;;; Servers

(compojure/defroutes compojure-handler (compojure/GET "*" [] response))

(defn server
  [name & [port create-server!-fn]]
  (if-let [server (get @servers name)]
    server
    (when (and port create-server!-fn)
      (let [server (create-server!-fn port)]
        (swap! servers assoc name server)
        (info name "is running on port" port)
        server))))

(def system
  (let [properties (into {} (System/getProperties))]
    (fn [& keys] (str/join " " (map properties keys)))))

;;(lamina.trace/defexecutor aleph-executor {:min-thread-count 128})

(defn -main [& args]
  (info "Starting up servers..."
        {:clojure (clojure-version)
         :os      (system "os.name" "os.version")
         :java    (system "java.version")
         :vm      (system "java.vm.name" "java.vm.version")})

  (let [;; TODO Double check Aleph config opts
        cfg-aleph    (fn [port] {:port  port
                                :netty {:options {"client.soLinger" 0}}
                                ;;:executor aleph-executor
                                })
        cfg-http-kit (fn [port] {:port port :queue-size 20000})]

    ;;      :nginx-php           8081
    (server :ring-jetty      8082 #(jetty/run-jetty handler {:join? false :port %
                                                             :max-threads 100}))
    (server :ring-simple     8083 #(simpleweb/run-simpleweb handler {:port %}))
    (server :aleph           8084 #(aleph/start-http-server aleph-handler-sync  (cfg-aleph %)))
    (server :aleph-async     8085 #(aleph/start-http-server aleph-handler-async (cfg-aleph %)))
    (server :aloha           8086 #(aloha/start-http-server handler {:port %}))
    (server :http-kit        8087 #(http-kit/run-server handler        (cfg-http-kit %)))
;    (server :http-kit-async  8088 #(http-kit/run-server http-kit-async (cfg-http-kit %)))
    (server :ring-netty      8089 #(netty/start-server handler {:port %  :zero-copy true 
                                                             :channel-options { "child.tcpNoDelay" true}
                                                             :max-http-chunk-length 1048576
                                                             :number-of-handler-threads 16
                                                             :max-channel-memory-size 1048576
                                                             :max-total-memory-size 1048576}))
    ;;      :tomcat7-servlet 8090
    ;;      :jetty-7-servlet 8091
    ;;      :jetty-8-servlet 8092
    ;;      :jetty-9-servlet 8093
    ;;      :nginx-clojure   8094
    ;;      :immutant        8095
    (server :ring-undertow   8096 #(undertow/run-undertow handler {:port %}))
    (server :vertx           8097 (fn [port]
                                    (embed/set-vertx! (embed/vertx))
                                    (-> (http/server)
                                      (http/on-request #(-> (http/server-response %)
                                                          (http/end (:body response))))
                                      (http/listen port))))))
