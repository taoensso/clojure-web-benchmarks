(defproject nginx-clojure "0.1.0-SNAPSHOT"
  ;; :plugins [[lein-shell "0.4.0"]]
  :dependencies [[org.clojure/clojure "1.7.0-alpha5"]]
  :jvm-opts ^:replace ["-server" "-XX:+UseConcMarkSweepGC"]
  :main nginx-clojure)
