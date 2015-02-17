(ns wrk2-result-parser
  (:require
   [clojure.string    :as str]
   [clojure.java.io   :as io]
   [table.core        :as tb]
   [incanter.core     :as ic]
   [incanter.charts   :as ics]
   [incanter.io       :as iio]
   [incanter.datasets :as ids]))

(def port-servers
  {"8082" "ring-jetty"
   "8084" "aleph"
   "8087" "http-kit"
   "8090" "tomcat 7"
   "8091" "jetty 7 servlet"
   "8092" "jetty 8 servlet"
   "8093" "jetty 9 servlet"
   "8094" "nginx-clojure"
   "8095" "immutant 1"
   "8096" "ring-undertow"
   "8097" "vertx"
   "8098" "tomcat 8"
   "8099" "immutant 2"})

(def tb-headers
  {:server   "Server"
   :port     "Port"
   :conns    "Conns"
   :threads  "Threads"
   :qps      "Queries/Second"
   :mlat     "Mean Latency(s)"
   :n4lat    "99.99% Latency(s)"
   :errors   "Errors"
   :total    "Total"
   :err-rate "Error Rate(%)"})

(def ordered-headers
  (mapv tb-headers
    [:server :port :threads :conns :qps :mlat :n4lat :err-rate :errors :total]))

(defn try-match [pattern s]
  (if-let [r (re-find pattern (first s))]
    [r (rest s)]
    [nil s]))

(defn parse-header [[s r]]
  (let [[[_ port] s]          (try-match #"\:(\d+)\/"        s)
        [[_ threads conns] s] (try-match #"(\d+)[^\d]+(\d+)" s)]
    ;; (println "parse-header" {:port port  :threads threads :connections conns} )
    [s (merge r
         {(tb-headers :port)    port
          (tb-headers :threads) threads
          (tb-headers :conns)   conns})]))

(defn format-time [t]
  (if (nil? t)
    0
    (let [[_ num unit] (re-find #"(\d+[\.]\d+)([^\d\.]*)" t)
          num (Float/parseFloat num)]
      (case unit
        "us" (/ num 1000000)
        "ms" (/ num 1000)
        "m"  (* num 60)
        num))))

(defn trim-num [n] (-> n (* 1000) (int) (/ 1000.0)))

(defn parse-lats [[s r]]
  (let [[[_ mlat] s]    (try-match #"(\d+[^\s]+)" s)
        [[_ nnnnlat] s] (try-match #"%\s+([^\s]+)" (nthrest s 8))]
    ;; (println "parse-lats"  {:mlat  mlat :nnnnlat nnnnlat} )
    [s (merge r
         {(tb-headers :mlat)  (trim-num (format-time mlat))
          (tb-headers :n4lat) (trim-num (format-time nnnnlat))})]))

(defn skip-n [n [s r]] [(nthrest s n) r])

(defn parse-errors [[s r]]
  (let [[ss s]        (try-match #"\d+" s)
        [[_ & es ] s] (try-match #"errors:\s+connect (\d)+, read (\d+), write (\d+), timeout (\d+)" s)
        errors (if es (->> es (map #(Integer/parseInt %)) (reduce +)) 0)
        ss     (Integer/parseInt ss)
        total  (+ ss errors)]
    [s (merge r
         {(tb-headers :errors)   errors
          (tb-headers :total)    total
          (tb-headers :err-rate) (trim-num  (/ (* errors 100) (float total)))})]))

(defn parse-qps [[s r]]
 (let [[qps s] (try-match #"\d+" s)]
   [s (merge r
        {(tb-headers :qps) (Integer/parseInt qps)})]))

(defn parse-one [s]
  (-> [s {}]
      (parse-header)
      (parse-lats)
      ((partial skip-n 5))
      (parse-errors)
      (parse-qps)
      (second)
      (#(merge % {(tb-headers :server) (port-servers (% (tb-headers :port)))}))))

(defn parse-file "Parses a stripped wrk2 results file"
  [f]
  ;; (println "file " f)
  (with-open [rdr (io/reader f)]
    (->> (doall (line-seq rdr))
         (partition-by  #(re-find #"\-+\d+--DONE\-+|\=+=$" %))
         (filter #(> (count %) 1))
         (map parse-one))))

(defn save-table! [r f title keep-alive?]
  (spit (str f "-table.txt")
        (with-out-str
          (println title (if keep-alive? "(wrk2 Keep-Alive:On)" "(wrk2 Keep-Alive:Off)"))
          (println (ic/sel (ic/to-dataset r) :cols ordered-headers)))))

(defn save-pngs! [r f title keep-alive?]
  (let [y-label (if keep-alive? "(wrk2 Keep-Alive:On)" "(wrk2 Keep-Alive:Off)")
        server  (tb-headers :server)]
    (ic/with-data
      (ic/to-dataset r)
      (ic/save
        (ics/bar-chart server
          (tb-headers :qps)
          :group-by "Conns"
          :legend true
          :vertical false
          :title title
          :y-label (str (tb-headers :qps) y-label))
        (str f "-qps.png") :width 800 :height 600)

      (ic/save
        (ics/bar-chart server
          (tb-headers :mlat)
          :group-by "Conns"
          :legend true
          :vertical false
          :title title
          :y-label (str (tb-headers :mlat) y-label))
        (str f "-mlat.png") :width 800 :height 600)

      (ic/save
        (ics/bar-chart server
          (tb-headers :n4lat)
          :group-by "Conns"
          :legend true
          :vertical false
          :title title
          :y-label (str (tb-headers :n4lat) y-label))
        (str f "-n4lat.png") :width 800 :height 600)

      (ic/save
        (ics/bar-chart server (tb-headers :err-rate)
          :group-by "Conns"
          :legend true
          :vertical false
          :title title
          :y-label (str (tb-headers :err-rate) y-label))
        (str f "-errs.png") :width 800 :height 600))))

(def args-example-str "args: stripped-result-file title-string, e.g. 20150129-06-24-large.stripped  '2015-1-30 CentOS 7,2*Xeon E5-2620 v2@2.10GHz(24 hardware threads), Oracle JDK1.7.0_72,Clojure 1.7.0-alpha3 32 ~ 1024 Connections'")

(defn -main [& args]
  (if (< (count args) 2)
    (println "wrong args\n" args-example-str)
    (let [[f title] args
        stripped? (.endsWith f ".stripped")
        pf (subs f 0 (- (.length f) (.length ".stripped")))
        keep-alive? (> 0 (.indexOf f "-nonkeepalive"))]
    (if stripped?
      (let [r (parse-file f)]
        (save-table! r pf title keep-alive?)
        (save-pngs!  r pf title keep-alive?))
      (println "wrong args\n" args-example-str)))))

;; lein run ../../results/20150129-01-40-small.stripped '2015-1-30 CentOS 7,2*Xeon E5-2620 v2@2.10GHz(24 hardware threads), Oracle JDK1.7.0_72,Clojure 1.7.0-alpha3 32 ~ 1024 Connections'

;; lein run  ../../results/20150129-06-24-large.stripped '2015-1-30 CentOS 7,2*Xeon E5-2620 v2@2.10GHz(24 hardware threads), Oracle JDK1.7.0_72,Clojure 1.7.0-alpha3 10000~60000 Connections'

;; lein run ../../results/20150129-03-55-nonkeepalive.stripped '2015-1-30 CentOS 7,2*Xeon E5-2620 v2@2.10GHz(24 hardware threads), Oracle JDK1.7.0_72,Clojure 1.7.0-alpha3 32~1024 Connections'
