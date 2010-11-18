(ns thor.queue)
(import '(java.util PriorityQueue Comparator Random))
(defstruct event :message :time )
(def random (Random.))
(defn create-event [message t] (struct-map event 
                                 :message message
                                 :time t
                                 ))

(defn make-comparator [compare-fn] 
  (proxy [Comparator] []
    (compare [left right] (compare-fn left right))))

(defn create-queue []
  (PriorityQueue. 10 
    (make-comparator #(. (%1 :time)  compareTo (%2 :time)) 
      )))

(defn add-to-queue [queue elem] 
(.add queue elem))

(defn get-next-event [q] 
  (.poll q))



;(let [q (create-queue)]
;  (dotimes [_ 10]
;  (add-to-queue q (create-event "a" (.nextInt random 100 )))
;  )
;  ;(add-to-queue q (create-event "k" 1))
;  (dotimes [_ 10]
;    (println ((get-next-event q) :time))
;  ))