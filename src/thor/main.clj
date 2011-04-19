(ns thor.main
  (:gen-class)
  (:use thor.window thor.lang thor.queue)
)

;(defn put-items[] 
;    (dotimes [_ 10]
;      (add-events-to-queue *queue* (create-event (format "event %d" _) (.nextInt random 10))
;        ))
;    (.size *queue*)
;    )
;


(defn main-prog [] 
  (binding [*queue* (create-queue)]
    (dotimes [_ 10]
      (add-events-to-queue *queue* (create-event (format "event %d" _) (-> 10 rand int))
                           ))

    (loop [event (get-next-event)]
      (eval (:task event))     
      (if (no-events?) 
        (recur (event (get-next-event))))
      )))


 (defn -main [& args]
 ;  (thor.window/main)
  (main-prog)
   )
  
