(defproject nginx-clojure "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
;  :plugins [[lein-shell "0.4.0"]]
  :dependencies 
  [[org.clojure/clojure    "1.7.0-alpha3"]
    ]
  :jvm-opts ^:replace ["-server" "-XX:+UseConcMarkSweepGC"]
  :profiles {:1.7.0-alpha3 {:dependencies [[org.clojure/clojure "1.7.0-alpha3"]]}}
  :main nginx-clojure
)
