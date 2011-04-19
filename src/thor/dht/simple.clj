(ns thor.dht.simple 
  (:use thor.node thor.data-store)
  )
(use '[clojure.contrib.math :only (expt)])

; A simple DHT implmentation based on circular linked list
; http://rezahok.wordpress.com/2009/09/21/a-simple-distributed-hash-table-dht/
; For testing everything

; KEY_SIZE defines the maximum number of keys possible in the system
; 2**KEY_SIZE will the the size
(def KEY_SIZE 10)
(def NUM_NODES 10)
; you want some sort of network overlay to talk about the network
; or the topology in this case, it will be a ring of nodes 
(def *overlay* (atom {}))
(def *nodelist* (atom ()))

; returns a reference to the node
(defn get-overlay-node [id]
  (get @*overlay* id)
  )


; create an overlay network of some size
(defn create-overlay [{:keys [num size nodes]  :or {num 10 size 100 nodes '() }}]
  ; make overlay empty -- might need to change this logic
  (if (-> @*overlay* empty? not) 
    (reset! *overlay* {}))

  (if (empty? nodes) 
    (do
      ; create nodes with ids from 0 to 2**KEY_SIZE
      (reset! *nodelist* (create-seq-random-node-list 
                           num size size {:start 0 :end (expt 2 KEY_SIZE)} )))
    ;else
    (do
      ; needs testing
      (reset! *nodelist*  nodes ))
    )
  ; regardless, we should have *nodeslist* full
  ; to make a circular linked-list now
  (let [f (first @*nodelist*)
        l (last @*nodelist*)]
    ; loop through first to next to last node 
    (dotimes [i ( - (count @*nodelist*) 1)]
      (swap! *overlay* assoc (:id (nth @*nodelist* i))
             {:node (ref (nth @*nodelist* i))
              :next (ref (nth @*nodelist* (+ i 1)))}
             ))
    ; make it so that the first one is the next of the last one
    (swap! *overlay* assoc (:id l) 
           {:node (ref l)
            :next (ref f)})
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

(defn find-node [start-node-id hash-key]
  (loop [current (get-overlay-node start-node-id)]
    ; if hash-key is between the current-node-id and next node id      
    (if (and (<= (-> current :node deref :id) hash-key)
             (< hash-key (-> current :next deref :id))  )
      (closest-node (-> current :node deref) 
                    (-> current :next deref) 
                    hash-key)
      ; else
      (recur  (get-overlay-node (-> current :next deref :id) ))
      )
    ))

(defn store [start-node-id k v]
  (store-data (find-node start-node-id k) k v)
  )

(defn lookup [start-node-id k]
  (get (get-data-in-node (find-node start-node-id k)) :value)
  )

