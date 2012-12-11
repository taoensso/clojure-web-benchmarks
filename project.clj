(defproject com.taoensso/clojure-web-server-benchmarks "0.1.0"
  :description "Clojure web server benchmarks"
  :url         "https://github.com/ptaoussanis/clojure-web-server-benchmarks"
  :license     {:name "Eclipse Public License"
                :url  "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies
  [[org.clojure/clojure               "1.4.0"]
   [compojure                         "1.1.3"]
   [ring                              "1.1.6"]
   ;; [org.eclipse.jetty/jetty-server "7.6.7.v20120910"]
   [ring-simpleweb-adapter            "0.2.0"]
   [aleph                             "0.3.0-beta8"]
   [aloha                             "1.0.1"]
   [me.shenfeng/http-kit              "1.2"]
   [me.shenfeng/async-ring-adapter    "1.0.2"]
   [com.taoensso/timbre               "1.0.0"]]
  :profiles {:1.5 {:dependencies [[org.clojure/clojure "1.5.0-alpha3"]]}}
  :exclusions [org.clojure/clojure]
  :jvm-opts   ["-server" "-XX:+UseConcMarkSweepGC"]
  :main               servers
  :min-lein-version   "2.0.0"
  :warn-on-reflection true)
