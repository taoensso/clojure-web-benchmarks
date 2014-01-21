(ns immutant.core
  (:require [immutant.web :as web]))

(defn init []
  (web/start-servlet "/" (new immutant.servlet)))
