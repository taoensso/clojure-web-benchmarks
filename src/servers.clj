(ns servers
  (:gen-class)
  (:require [compojure.core         :as compojure]
            [ring.adapter.jetty     :as jetty]
            [ring.adapter.simpleweb :as simpleweb]
            [aleph.http             :as aleph]
            [lamina.core            :as lamina]
            [taoensso.timbre :as timbre :refer (trace debug info warn error report)]
            [taoensso.timbre.profiling :as profiling :refer (p profile)]))

;;;; TODO Jetty 8, Netty, java.nio, Webbit, ...?

(defonce servers (atom {}))
(def response {:status 200 :headers {"content-type" "text/html"} :body "echo"})

;;;; Handlers

(defn handler             [request] response)
(defn aleph-handler-async [channel request] (lamina/enqueue channel response))
(def  aleph-handler-sync  (aleph/wrap-ring-handler handler))

(compojure/defroutes compojure-handler (compojure/GET "*" [] response))

;;;; Servers

(defn start-jetty!
  [port]
  (when-not (:jetty @servers)
    (swap! servers assoc :jetty
           (jetty/run-jetty handler {:join? false :port  port}))
    (info (str "Jetty is running on port " port))))

(defn start-simple!
  [port]
  (when-not (:simple @servers)
    (swap! servers assoc :simple
           (simpleweb/run-simpleweb handler {:port port}))
    (info (str "Simple HTTP Engine is running on port " port))))

(defn start-aleph-sync!
  [port]
  (when-not (:aleph-sync @servers)
    (swap! servers assoc :aleph-sync
           (aleph/start-http-server aleph-handler-sync {:port port}))
    (info (str "Aleph (synchronous) is running on port " port))))

(defn start-aleph-async!
  [port]
  (when-not (:aleph-async @servers)
    (swap! servers assoc :aleph-async
           (aleph/start-http-server aleph-handler-async {:port port}))
    (info (str "Aleph (asynchronous) is running on port " port))))

(defn -main
  "Results captured post-warmup on JDK7 -server, Macbook Air 1.7GHz i5:
   `ab -n 5000 -c4 http://localhost:[port]/`"
  [& args]
  ;; Reference nginx 1.2.5  ; ~10,000 reqs/sec
  (start-jetty!       8081) ;  ~6,100 reqs/sec
  (start-simple!      8082) ;  ~4,600 reqs/sec
  (start-aleph-sync!  8083) ;  ~2,800 reqs/sec
  (start-aleph-async! 8084) ;  ~4,200 reqs/seq
  )