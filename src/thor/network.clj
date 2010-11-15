(ns thor.network
  (:require clojure.core thor.node
  ))


(defn create-node-list [] (list []))

(defn add-node [& node-list ] 
  (dosync 
    (alter node-list     
      (vec (apply map + node-list)))))

