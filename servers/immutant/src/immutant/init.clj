(ns immutant.init
  (:require [immutant.web :as web]))

(def response {:status 200
               :headers {"content-type" "text/html"}
               :body (slurp "../index.html")})

(defn handler [request] response)

(web/start handler)
