(ns servers
  (:gen-class)
  (:require [compojure.core          :as compojure]
            [ring.adapter.jetty      :as jetty]
            [ring.adapter.simpleweb  :as simpleweb]
            [aleph.http              :as aleph]
            [aloha.core              :as aloha]
            [me.shenfeng.http.server :as http-kit]
            [lamina.core             :as lamina]
            [taoensso.timbre :as timbre :refer (trace debug info warn error report)]
            [taoensso.timbre.profiling :as profiling :refer (p profile)]))

(defonce servers (atom {}))
(def response {:status 200 :headers {"content-type" "text/html"} :body "echo"})

;;;; Handlers

(defn handler             [request] response)
(defn aleph-handler-async [channel request] (lamina/enqueue channel response))
(def  aleph-handler-sync  (aleph/wrap-ring-handler handler))
(http-kit/defasync http-kit-async [request] cb (cb response))

;;;; Servers

(compojure/defroutes compojure-handler (compojure/GET "*" [] response))

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
  (server :jetty          8081 #(jetty/run-jetty handler {:join? false :port %}))
  (server :simple         8082 #(simpleweb/run-simpleweb handler {:port %}))
  (server :aleph          8083 #(aleph/start-http-server aleph-handler-sync  {:port %}))
  (server :aleph-async    8084 #(aleph/start-http-server aleph-handler-async {:port %}))
  (server :aloha          8085 #(aloha/start-http-server handler {:port %}))
  (server :http-kit       8086 #(http-kit/run-server handler {:port %}))
  (server :http-kit-async 8087 #(http-kit/run-server http-kit-async {:port %})))

;;;; Results post-warmup OpenJDK7 -server, 1.7GHz i5
;;;; ab -n 5000 -c4 http://localhost:[port]/
;; nginx 1.2.5  ; ~10,000 reqs/sec
;; :aloha       ;  ~7,800 reqs/sec
;; :jetty       ;  ~6,100 reqs/sec
;; :simple      ;  ~4,600 reqs/sec
;; :aleph       ;  ~2,800 reqs/sec
;; :aleph-async ;  ~4,500 reqs/sec