(ns thor.network
  (:require clojure.core thor.node
  ))


(defn create-node-list [] (ref ()))

(defn add-node [n node-list] 
  (dosync (alter node-list conj n)))

