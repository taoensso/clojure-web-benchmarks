(defproject jetty7 "0.1.0-SNAPSHOT"
  :min-lein-version "2.3.3"
  :global-vars {*warn-on-reflection* true
                *assert* false}
  :plugins [[lein-servlet "0.4.1"]]
  :dependencies [[org.clojure/clojure "1.7.0-alpha5"]]
  :aot [jetty7.servlet]
  :jvm-opts ^:replace ["-server" "-Xmx2g"]
  :servlet {:deps    [[lein-servlet/adapter-jetty7  "0.4.1"]]
            :config  {:port 8091}
            :webapps {"/" {:servlets {"/*" 'jetty7.servlet}
                           :public "."}}})
