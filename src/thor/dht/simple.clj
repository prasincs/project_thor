(ns thor.dht.simple 
  (:use thor.node thor.data-store thor.messages)
  (:require thor.queue thor.ui.window)
  )
(use '[clojure.contrib.math :only (expt)])

; A simple DHT implmentation based on circular linked list
; http://rezahok.wordpress.com/2009/09/21/a-simple-distributed-hash-table-dht/
; For testing everything

; KEY_SIZE defines the maximum number of keys possible in the system
; 2**KEY_SIZE will the the size
(def KEY_SIZE 32)
(def NUM_NODES 10)
; you want some sort of network overlay to talk about the network
; or the topology in this case, it will be a ring of nodes 
(def *overlay* (atom {}))
(def *nodelist* (atom ()))

; returns a reference to the node
(defn get-overlay-node [id]
  (get @*overlay* id)
  )

(defn get-number-of-devices []
  (count @*nodelist*)
  )
; create an overlay network of some size
(defn create-overlay [& [{:keys [num size nodes]  
                          :or {:num 10 :size 100 :nodes () }}]]
  ; make overlay empty -- might need to change this logic
  (if (-> @*overlay* empty? not) 
    (reset! *overlay* {}))

  (if (empty? nodes) 
    (do
      (println "Populating Nodeslist with random nodes")
      (prn "num-devices -> " num)
      (prn size)
      ; create nodes with ids from 0 to 2**KEY_SIZE
      (reset! *nodelist* (create-seq-random-node-list 
                           num size size 
                           {:start 0 :end (expt 2 KEY_SIZE)} )))
    
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
  (println "Closest node")
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


; FIX infinite loop for 2**KEY_SIZE
(defn find-node [start-node-id hash-key]
  (println "Find  node")
  (loop [current (get-overlay-node start-node-id)
         hop 0
         message  nil        
         ]
    ; if hash-key is between the current-node-id and next node id      
    ;(println "current-node " current)
    (println (str "looking at " (-> current :node deref :id)))
    (if  
      (and (<= (-> current :node deref :id) hash-key)
           (or 
             (< hash-key (-> current :next deref :id))  
             (zero? (-> current :next deref :id ))
             )
           )
      (do
        (let [message (if (nil? message)
                     (create-message "find-node"
                                     start-node-id
                                     (-> current :node deref :id)
                                     {:hops hop})
                     message)]
        {:node 
         (closest-node (-> current :node deref) 
                       (-> current :next deref) 
                       hash-key)
         :message message }
        ))
      ; else recur
      (do
        (if (>= hop (get-number-of-devices))
          (do
            {:message 
             (create-message "find-node" start-node-id 
                             (-> current :node deref :id) 
                             {:hops 20})
             })
          ;else
          (recur  (get-overlay-node 
                    (-> current :next deref :id)) 
                 (inc hop)
                 (create-message "find-node"
                                 start-node-id 
                                 (-> current :node deref :id)
                                 {:hops hop}))
          )

        )
      )))
    

(defn store [start-node-id k v]
  (println "storing " k "->" v ". Starting from " start-node-id)
  (let [f (find-node start-node-id k)]
  (store-data 
    (:id 
      (:node f)) k v)
   {:message 
    (:message f)}
    ))

(defn lookup [start-node-id k]
  (let [f (find-node start-node-id k)]
    {:value
      (get (get-data-in-node (:id (:node f)) :value))
     :message
      (:message f)}))

(defn get-random-node []
  (deref (nth *nodelist* (-> (count @*nodelist*) rand int )
       ))
  )

(defn show-nodes []
  (thor.ui.window/init-window @*nodelist*)
  )
