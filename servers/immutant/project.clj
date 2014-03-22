(defproject immutant "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]]
  :plugins [[lein-immutant "1.2.0"]]
  :profiles {:benchmark {:immutant {:context-path "/", :nrepl-port nil}}
             :servlet   {:aot [immutant.servlet]
                         :dependencies [[javax.servlet/javax.servlet-api "3.0.1"]]
                         :immutant {:init immutant.core/init}}})
