(defproject results-parser "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
;  :plugins [[lein-shell "0.4.0"]]
  :dependencies 
  [[org.clojure/clojure    "1.7.0-alpha3"]
    [table "0.4.0"] 
    [incanter "1.5.6"]
    ]
  :jvm-opts ["-server" "-XX:+UseConcMarkSweepGC"]
  :profiles {:1.7.0-alpha3 {:dependencies [[org.clojure/clojure "1.7.0-alpha3"]]}}
  :main wrk2-result-parser
)
