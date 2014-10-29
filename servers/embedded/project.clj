(defproject com.taoensso/clojure-web-server-benchmarks "1.0.0-SNAPSHOT"
  :description "Clojure web server benchmarks"
  :url         "https://github.com/ptaoussanis/clojure-web-server-benchmarks"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"
            :distribution :repo
            :comments "Same as Clojure"}
  :min-lein-version "2.3.3"
  :global-vars {*warn-on-reflection* true
                *assert*             false}
  :main servers
  :dependencies
  [[org.clojure/clojure    "1.7.0-alpha3"]
   [compojure              "1.2.1"]
   [ring                   "1.3.1"]
   [com.taoensso/timbre    "3.3.1"]
   [aleph                  "0.4.0-alpha7"]
   [http-kit               "2.1.19"]
   [org.immutant/web       "2.x.incremental.157"] ; 2.0.0-alpha2?
   [ring-undertow-adapter  "0.1.7"
    :exclusions [io.undertow/undertow-core]]
   [io.vertx/clojure-api   "1.0.4"]]
  :exclusions [org.clojure/clojure]
  :profiles   {:1.5 {:dependencies [[org.clojure/clojure "1.5.1"]]}
               :dev {:dependencies [[cider/cider-nrepl   "0.7.0"]]
                     :plugins
                     [[lein-pprint  "1.1.2"]
                      [lein-ancient "0.5.5"]]}}
  :jvm-opts   ["-server" "-XX:MaxGCPauseMillis=25" "-XX:+UseG1GC" "-Xmx2g"]
  :repositories
  {"sonatype-oss-public"             "https://oss.sonatype.org/content/groups/public/"
   "Immutant 2.x incremental builds" "http://downloads.immutant.org/incremental/"}

  :aliases
  {"start-dev" ["repl" ":headless"]})
