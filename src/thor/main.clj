(ns thor.main
  (:gen-class)
  (:use thor.window thor.lang thor.queue)
)

(defn put-items[] 
    (dotimes [_ 10]
      (add-to-queue q (create-event (format "event %d" _) (.nextInt random 10))
        ))
    (.size q)
    )



(defn main-prog [] 
      (binding [*queue* (create-queue)]
      (dotimes [_ 10]
        (add-to-queue *queue* (create-event (format "event %d" _) (.nextInt random 10))
        ))

        (loop [event (get-next-event)]
          (eval (:task event))     
               (if (noevents?) 
                 (recur (event (get-next-event))))
               )))


 (defn -main [& args]
 ;  (thor.window/main)

   )
  
