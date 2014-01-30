(defproject com.taoensso/clojure-web-server-benchmarks "1.0.0-SNAPSHOT"
  :description "Clojure web server benchmarks"
  :url         "https://github.com/ptaoussanis/clojure-web-server-benchmarks"
  :license     {:name "Eclipse Public License"
                :url  "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies
  [[org.clojure/clojure                   "1.5.1"]
   [compojure                             "1.1.6"]
   [ring                                  "1.2.1"]
   ;; [org.eclipse.jetty/jetty-server     "7.6.7.v20120910"]
   [ring-simpleweb-adapter                "0.2.0"]
   [aleph                                 "0.3.0"]
   ;; [com.taoensso.forks/aleph           "0.3.0-beta9"]
   ;; [aleph                              "0.3.0-SNAPSHOT"]
   [aloha                                 "1.0.2"]
   ;; [me.shenfeng/http-kit               "1.2"]
   [http-kit "2.1.16"]
   ;; [me.shenfeng/async-ring-adapter     "1.0.2"]
   [netty-ring-adapter                    "0.4.6"]
   [ring-undertow-adapter                 "0.1.2"]
   [io.vertx/clojure-api                  "1.0.0.Beta2"]
;   [ring-netty-adapter "0.0.3"]
   ;[com.taoensso.forks/async-ring-adapter "1.1.0-alpha1"]
   [com.taoensso/timbre                   "1.2.0"]]
  :profiles   {:1.5 {:dependencies [[org.clojure/clojure "1.5.1"]]}}
  :exclusions [org.clojure/clojure]
  :jvm-opts   ["-server" "-XX:MaxGCPauseMillis=25" "-XX:+UseG1GC"]
  :main               servers
  :min-lein-version   "2.0.0"
  :warn-on-reflection true)
