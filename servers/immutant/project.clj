(defproject immutant "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.7.0-alpha5"]]
  :plugins [[lein-immutant "1.2.0"]]
  :profiles {:benchmark {:immutant {:context-path "/", :nrepl-port nil}}
             :servlet   {:aot [immutant.servlet]
                         :dependencies [[javax.servlet/javax.servlet-api "3.0.1"]]
                         :immutant {:init immutant.core/init}}})
