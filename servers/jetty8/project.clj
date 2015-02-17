(defproject jetty8 "0.1.0-SNAPSHOT"
  :min-lein-version "2.3.3"
  :global-vars {*warn-on-reflection* true
                *assert* false}
  :plugins [[lein-servlet "0.4.1"]]
  :dependencies [[org.clojure/clojure "1.7.0-alpha5"]]
  :aot [jetty8.servlet]
  :jvm-opts ^:replace ["-server" "-Xmx2g"]
  :servlet {:deps    [[lein-servlet/adapter-jetty8  "0.4.1"]]
            :config  {:port 8092}
            :webapps {"/" {:servlets {"/*" 'jetty8.servlet}
                           :public "."}}})
