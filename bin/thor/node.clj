(ns thor.node
  (:require clojure.core))
  (defstruct pos :x :y )
  (defstruct node 
    :id 
    :name 
    :location  ; x y coordinate in graph pos type
    )
  
  (defn create-node [id nodename pos]
    (struct-map node :id id 
     :name nodename
     :location pos
     )
  )
  
  (defn get-name [node] (node :name))
  

