(ns jetty8.servlet
  (:import (java.io            PrintWriter)
           (javax.servlet      ServletConfig)
           (javax.servlet.http HttpServletRequest HttpServletResponse))
  (:gen-class :extends javax.servlet.http.HttpServlet))


(defn -init
  ([this]
   (println "Servlet initialized with no params"))
  ([this ^ServletConfig config]
   (println "Servlet initialized with servlet config" config)))


(defn handle
  [^HttpServletRequest request ^HttpServletResponse response]
  (let [template "echo"]
    (.setContentType response "text/html")
    (doto ^PrintWriter (.getWriter response)
      (.println template))))


(defn -doGet
  [this ^HttpServletRequest request ^HttpServletResponse response]
  (handle request response))


(defn -doPost
  [this ^HttpServletRequest request ^HttpServletResponse response]
  (handle request response))


(defn -destroy
  [this]
  (println "Servlet destroyed"))

