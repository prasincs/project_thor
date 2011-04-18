(ns thor.node "The node class used for defining various nodes in the network" 
  (:require clojure.core)
  (:require clojure.contrib.math) ; this didn't work not sure why
  (:import [java.util Random])
  )

; 
(def INFINITY (Integer/MAX_VALUE))
(defstruct pos :x :y )
(defstruct mem :total :free)
(defstruct data :size :content)

(defn create-data [size content]
  (struct-map data :size size :content content))

(defn create-memory [size] 
  (struct-map mem :total size :free size))

(defrecord Node 
  [id 
   memory
   location  ; x y coordinate in graph pos type
   speed
   direction
   power
   range
   ]

  )

(defn create-node "create a node based on given info"
  [id pos memory-size]
  (Node.  
    id 
    (create-memory memory-size)
    pos 
    0
    0
    0
    10
    )
  )

(defn store [node payload]
  
  )

(defn position [x y] 
  (struct-map pos :x x :y y))

(defn random-position [max-width max-height]
  (let [random (Random.) ] 
    (struct-map pos :x (.nextInt random max-width)
                :y (.nextInt random max-height))))


;used to create a node with  
(defn create-random-node [max-width max-height]
  (let [ random (Random.)]
    (def node-num (.nextInt random (* max-width max-height)))
    (create-node   node-num  (random-position max-width max-height) 100)))


; TODO : make multimethod 
(defn get-location [n]
  (n :location))

(defn get-distance-locations [loc1 loc2]
  (defn squared [x] (* x x))
  (Math/sqrt 
    (+
  (squared (- (loc2 :x) (loc1 :x)))
  (squared (- (loc2 :y) (loc1 :y)))
  )))

; need a place to refer back for use of -> macro
; so not refactoring this yet
(defn get-distance [n1 n2 ]
  (defn squared [x] (* x x))
  (Math/sqrt
    (+
      (squared ( - (-> n2 :location :x) (-> n1 :location :x)))
      (squared ( - (-> n2 :location :y) (-> n1 :location :y))))))

(defn get-nearest-node [nlist node]
  (let [dist (atom INFINITY)
        closest-node (atom {})]
    (doseq [n @nlist]
      (if (not (= (:id n) (:id node) ))
        (do
          (let [curr-dist (get-distance n node)]
            (if (< curr-dist @dist )
              (reset! dist curr-dist)
              (reset! closest-node n)
              )))))
    {:node @closest-node :distance @dist}
    ))
  
; store data in the data store 
; data is just key/value
(defn store [nlist node & datas]

  )

; creates nodes in a circle
; given width and height, returns nodes around
(defn create-nodes-in-circle [num width height]
  (let [center (position (/ width 2) (/ height 2))
        radius (get-distance-locations 
                 center 
                 (position width (/ height 2)) )  
        angle (/ (* 2 Math/PI) num)
        nlist (atom ())
        ]
    (dotimes [n num]
      (swap! nlist 
             concat 
             (list
               (create-node 
                 n
                 (position  
                   (+ (:x center)
                      (* (Math/cos 
                           (* angle n) ) radius)) 
                   (+ (:y center)
                      (* (Math/sin 
                           (* angle n) ) radius))
                   ) 
                 1000
                 ))))
    @nlist))

(defn create-random-node-list [nlist num width height]
  (dotimes [_ num] 
    (swap! nlist 
           concat 
           (list (create-random-node width height)))))

;(defn get-name [node] (node :name))
