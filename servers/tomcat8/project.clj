(defproject tomcat8 "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :warn-on-reflection true
  :plugins [[lein-servlet "0.4.0"]]
  :dependencies [[org.clojure/clojure "1.5.1"]]
  :aot [tomcat8.servlet]
  :jvm-opts ["-server" "-XX:+UseConcMarkSweepGC"]
  :profiles {:1.5 {:dependencies [[org.clojure/clojure "1.5.1"]]}}
  :servlet {:deps    [[lein-servlet/adapter-tomcat8 "0.4.0"]]
            :config  {:port 8098}
            :webapps {"/" {:servlets {"/*" 'tomcat8.servlet}
                           :public "."}}})
