(defproject com.taoensso/clojure-web-server-benchmarks "1.0.0-SNAPSHOT"
  :author "Peter Taoussanis and contributors"
  :description "Clojure web server benchmarks"
  :url "https://www.taoensso.com/clojure/web-benchmarks"

  :license
  {:name "Eclipse Public License - v 1.0"
   :url  "https://www.eclipse.org/legal/epl-v10.html"}

  :global-vars
  {*warn-on-reflection* true
   *assert*             true
   *unchecked-math*     false}

  :main servers
  :jvm-opts ["-server" "-Xms2048m" "-Xmx2048m"]

  :dependencies
  [[org.clojure/clojure   "1.11.1"]
   [com.taoensso/encore   "3.68.0"]
   [compojure             "1.7.0"]
   [ring                  "1.10.0"]
   ;;
   #_[aleph               "0.6.3"] ; Failing?
   [http-kit              "2.8.0-beta1"]
   [org.immutant/web      "2.1.10"]
   [ring-undertow-adapter "0.2.2" :exclusions [io.undertow/undertow-core]]
   [io.vertx/clojure-api  "1.0.5"]]

  :profiles
  {:dev
   {:dependencies [[cider/cider-nrepl "0.38.1"]]
    :plugins
    [[mx.cider/enrich-classpath "1.18.0"]
     [lein-pprint               "1.3.2"]
     [lein-ancient              "0.7.0"]]}}

  :aliases {"start-dev" ["repl" ":headless"]})
