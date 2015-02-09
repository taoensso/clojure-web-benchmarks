(defproject results-parser "0.1.0-SNAPSHOT"
  :author "Zhang,Yuexiang (@xfeep)"
  :dependencies
  [[org.clojure/clojure "1.7.0-alpha3"]
   [table    "0.4.0"]
   [incanter "1.5.6"]]
  :jvm-opts ["-server" "-XX:+UseConcMarkSweepGC"]
  :main wrk2-result-parser)
