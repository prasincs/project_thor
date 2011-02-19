(ns thor.node "The node class used for defining various nodes in the network" 
  (:require clojure.core)
  (:require clojure.contrib.math) ; this didn't work not sure why
  (:import [java.util Random])
  )

  (defstruct pos :x :y )
  (defstruct mem :total :free)
  (defstruct data :size :content)

  (defn create-data [size content]
    (struct-map data :size size :content content))

  (defn create-memory [size] 
    (struct-map mem :total size :free size))


  (defrecord Node 
    [id 
     name
     memory
     location  ; x y coordinate in graph pos type
     speed
     direction
     power]
    )
  
   (defn create-node "create a node based on given info"
     [id name pos memory-size]
    (Node.  
      id 
      name 
      (create-memory memory-size)
      pos 
      0
      0
      0
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
  (create-node   node-num (format "random node %d" node-num) (random-position max-width max-height) 100)))

; TODO: move to math or utils
(defn square [x] (* x x))

; TODO : make multimethod 
(defn get-location [n]
  (n :location))


(defn get-distance [n1 n2 ]
  (defn squared [x] (* x x))
  (Math/sqrt
      (+
      (squared ( - (-> n2 :location :x) (-> n1 :location :x)))
      (squared ( - (-> n2 :location :y) (-> n1 :location :y))))))



(defn create-random-node-list [nlist num width height]
  (dotimes [_ num] (swap! nlist concat (create-random-node width height))))

  ;(defn get-name [node] (node :name))