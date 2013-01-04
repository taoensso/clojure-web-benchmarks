(defproject jetty8 "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :warn-on-reflection true
  :plugins [[lein-servlet "0.2.0"]]
  :dependencies [[org.clojure/clojure "1.4.0"]]
  :aot [jetty8.servlet]
  :servlet {;; uncomment only either of the :deps entries below
            ;; :deps    [[lein-servlet/adapter-jetty7  "0.2.0"]]
            :deps    [[lein-servlet/adapter-jetty8  "0.2.0"]]
            ;; :deps    [[lein-servlet/adapter-tomcat7 "0.2.0"]]
            :config  {:port 8208}
            :webapps {"/" {:servlets {"/*" 'jetty8.servlet}
                           :public "."}}})
