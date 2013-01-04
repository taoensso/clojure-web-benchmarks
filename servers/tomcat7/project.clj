(defproject tomcat7 "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :warn-on-reflection true
  :plugins [[lein-servlet "0.2.0"]]
  :dependencies [[org.clojure/clojure "1.4.0"]]
  :aot [tomcat7.servlet]
  :jvm-opts ["-server" "-XX:+UseConcMarkSweepGC"]
  :profiles {:1.5 {:dependencies [[org.clojure/clojure "1.5.0-RC1"]]}}
  :servlet {;; uncomment only either of the :deps entries below
            ;; :deps    [[lein-servlet/adapter-jetty7  "0.2.0"]]
            ;; :deps    [[lein-servlet/adapter-jetty8  "0.2.0"]]
            :deps    [[lein-servlet/adapter-tomcat7 "0.2.0"]]
            :config  {:port 8089}
            :webapps {"/" {:servlets {"/*" 'tomcat7.servlet}
                           :public "."}}})
