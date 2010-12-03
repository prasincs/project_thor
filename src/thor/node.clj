(ns thor.node "The node class used for defining various nodes in the network" 
  (:require clojure.core)
  (:require clojure.contrib.math) ; this didn't work not sure why
  (:import [java.util Random])
  )

  (defstruct pos :x :y )
  (defstruct node 
    :id 
    :name 
    :location  ; x y coordinate in graph pos type
    )
  
  (defn create-node "create a node based on given info" [id nodename pos]
    (struct-map node :id id 
     :name nodename
     :location pos
     )
  )

(defn position [x y] 
  (struct-map pos x y))

(defn random-position [max-width max-height]
  (let [random (Random.) ] 
    (struct-map pos :x (.nextInt random max-width)
      :y (.nextInt random max-height))))


;used to create a node with  
(defn create-random-node [max-width max-height]
  (let [ random (Random.)]
  (def node-num (.nextInt random (* max-width max-height)))
  (create-node   node-num (format "random node %d" node-num) (random-position max-width max-height))))

; TODO: move to math or utils
(defn square [x] (* x x))

; TODO : make multimethod 
(defn get-location [n]
  (n :location))


; TODO: make multimethod
  (defn get-distance [node1 node2]
    (let [loc1 (get-location node1)
          loc2 (get-location node2)]
          (Math/sqrt (+ (square (Math/abs (- (loc1 :x) (loc2 :x))))
                       (square (Math/abs (- ( loc1 :y) (loc2 :y))))
                       )  
          )))
      
  
  (defn get-name [node] (node :name))
  

