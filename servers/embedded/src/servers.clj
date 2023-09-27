(ns servers
  "Embedded servers (run via Clojure)"
  {:author "Peter Taoussanis and contributors"}
  (:require
   [clojure.string         :as str]
   [compojure.core         :as compojure]
   [taoensso.encore        :as enc]
   ;;
   [ring.adapter.jetty     :as jetty]
   [ring.adapter.undertow  :as undertow]
   [immutant.web           :as immutant]
   [vertx.http             :as vhttp]
   [vertx.embed            :as vembed]
   ;; [aleph.http          :as aleph] ; Failing?
   [org.httpkit.server     :as http-kit]))

;;;;

(def properties (into {} (System/getProperties)))
(def env        (into {} (System/getenv)))

(defn props [& keys] (str/join " " (map properties keys)))
(comment (props "os.name" "os.version"))

(def num-cores (.availableProcessors (Runtime/getRuntime)))

;;;; Handler

(def response
  {:status  200
   :headers {"content-type" "text/html"}
   :body    (slurp "../index.html")})

(defn handler [request]
  (Thread/sleep (+ 10 (long (* (Math/random) 100)))) ; Mean 60 msecs work
  response)

;;;; Servers

(defonce servers (atom {}))
(defn start-server! [server-id port constructor-fn]
  (or (get @servers server-id)
    (let [server (constructor-fn port)]
      (swap! servers assoc server-id server)
      (println (format "%s is running on port %s" server-id port))
      server)))

(defn -main [& args]

  (println "Starting embedded servers...")
  (println (str "     Instant: " (enc/now-inst)))
  (println (str "     Clojure: " (clojure-version)))
  (println (str "          OS: " (props "os.name" "os.version")))
  (println (str "        Java: " (props "java.version")))
  (println (str "          VM: " (props "java.vm.name" "java.vm.version")))
  (println (str "  Max memory: " (.maxMemory (Runtime/getRuntime))))
  (println (str "   Num cores: " num-cores))
  (println)

  (start-server! :ring-jetty 8082
    (fn [port]
      ;; Any special config necessary for many-core systems?
      (jetty/run-jetty handler
        {:port port :join? false :max-threads (* num-cores 16)})))

  #_ ; Failing?
  (start-server! :aleph 8084
    (fn [port]
      ;; No special config necessary for many-core systems
      (aleph.netty/leak-detector-level! :disabled)
      (aleph/start-server handler {:port port :executor :none})))

  (start-server! :http-kit 8087
    (fn [port]
      (let [worker
            (http-kit/new-worker
              {:n-threads (* num-cores 16)
               :queue-size 204800
               :allow-virtual? true})]

        (http-kit/run-server handler
          {:port port :worker-pool (:pool worker)}))))

  ;; :tomcat7-servlet 8090
  ;; :jetty-7-servlet 8091
  ;; :jetty-8-servlet 8092
  ;; :jetty-9-servlet 8093
  ;; :nginx-clojure   8094

  (start-server! :ring-undertow 8096
    (fn [port]
      ;; Any special config necessary for many-core systems?
      (let [dispatch? (read-string (get env "UNDERTOW_DISPATCH" "false"))]
        (undertow/run-undertow handler
          {:port port :dispatch? dispatch?}))))

  (start-server! :vertx 8097
    (fn [port]
      ;; TODO Any special config necessary for many-core systems?
      (vembed/set-vertx! (vembed/vertx))
      (-> (vhttp/server)
          (vhttp/on-request #(vhttp/end (vhttp/server-response %) (:body response)))
          (vhttp/listen port))))

  ;; :tomcat8-servlet 8098

  (start-server! :immutant 8099
    (fn [port]
      (let [dispatch? (read-string (get env "UNDERTOW_DISPATCH" "false"))]
        (immutant/run handler
          :port      port
          :dispatch? dispatch?
          :io-threads        num-cores
          :worker-threads (* num-cores 16))))))
