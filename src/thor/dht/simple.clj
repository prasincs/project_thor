(ns thor.dht.simple 
  (require thor.node))
(use '[clojure.contrib.math :only (expt)])

; A simple DHT implmentation based on circular linked list
; http://rezahok.wordpress.com/2009/09/21/a-simple-distributed-hash-table-dht/
; For testing everything

; KEY_SIZE defines the maximum number of keys possible in the system
; 2**KEY_SIZE will the the size
(def KEY_SIZE 10)

; you want some sort of network overlay to talk about the network
; or the topology in this case, it will be a ring of nodes 
(def *overlay* (atom ()))
(def *nodelist* (atom ()))
(def *data-store* (init-data-store))


; create an overlay network of some size
(defn create-overlay [size & nodes]
  ; make overlay empty -- might need to change this logic
  (if (-> *overlay* empty? not) 
    (reset! *overlay* ()))

  (if (empty? nodes) 
    (do

      )
    ;else
    )
  )

(defn closest-node [node successor hash-key]
  (if (> (:id node) (:id successor))
    (if (> (- (expt 2 KEY_SIZE) (:id successor) hash-key) 
           (- hash-key (:id node) )
           )
      node
      successor)
    ;else
    (if (> (- hash-key (:id node) (- (:id successor) hash-key)))
      successor
      node)))

(defn find-node [start-node hash-key]
  
  
  )