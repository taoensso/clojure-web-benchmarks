(ns servers
  (:gen-class)
  (:require [compojure.core         :as compojure]
            [ring.adapter.jetty     :as jetty]
            [ring.adapter.simpleweb :as simpleweb]
            [aleph.http             :as aleph]
            [lamina.core            :as lamina]
            [taoensso.timbre :as timbre :refer (trace debug info warn error report)]
            [taoensso.timbre.profiling :as profiling :refer (p profile)]))

;;;; TODO Jetty 8, Netty, java.nio, Webbit, Aloha, ...?

(defonce servers (atom {}))
(def response {:status 200 :headers {"content-type" "text/html"} :body "echo"})

;;;; Handlers

(defn handler             [request] response)
(defn aleph-handler-async [channel request] (lamina/enqueue channel response))
(def  aleph-handler-sync  (aleph/wrap-ring-handler handler))

(compojure/defroutes compojure-handler (compojure/GET "*" [] response))

;;;; Servers

(defn server
  [name & [port create-server-fn]]
  (if-let [server (get @servers name)]
    server
    (when (and port create-server-fn)
      (let [server (create-server-fn port)]
        (swap! servers assoc name server)
        (info (str name " is running on port " port))
        server))))

(defn -main [& args]
  (server :jetty       8081 #(jetty/run-jetty handler {:join? false :port %}))
  (server :simple      8082 #(simpleweb/run-simpleweb handler {:port %}))
  (server :aleph-sync  8083 #(aleph/start-http-server aleph-handler-sync  {:port %}))
  (server :aleph-async 8084 #(aleph/start-http-server aleph-handler-async {:port %})))

;;;; Results post-warmup OpenJDK7 -server, 1.7GHz i5
;;;; ab -n 5000 -c4 http://localhost:[port]/
;; nginx 1.2.5  ; ~10,000 reqs/sec
;; :jetty       ;  ~6,100 reqs/sec
;; :simple      ;  ~4,600 reqs/sec
;; :aleph-sync  ;  ~2,800 reqs/sec
;; :aleph-async ;  ~4,200 reqs/seq