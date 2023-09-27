(defproject results-parser "1.0.0-SNAPSHOT"
  :author "Zhang,Yuexiang (@xfeep)"
  :dependencies
  [[org.clojure/clojure "1.11.1"]
   [table    "0.4.0"]
   [incanter "1.5.6"]]
  :jvm-opts ["-server" "-XX:+UseConcMarkSweepGC"]
  :main result-parser)
