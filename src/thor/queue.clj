(ns thor.queue
  {:require java.util.Random :as random})

(defstruct event :task :time )


(defn create-event [t task] { :time t
                              :task task
                                 })

(defn create-queue [] (atom ()))

(def *queue* (atom ()))

(defn add-events-to-queue [e] 
  (if (list? e )
  (swap! *queue* concat e)
  (swap! *queue* concat (list e))
    )
  (reset! *queue* (sort-by  :time @*queue*))
  )

(defn add-random-event [i s] 
  (add-events-to-queue 
    (create-event (int (rand s)) 
                  (read-string (format "(println %s)" i)))
    ))



(defn create-random-queue 
  "Creates a queue of random events for testing"
  [s]
  (binding [*queue* (create-queue)]
          (dotimes [_ s ]
            (add-random-event _ s)
  @*queue*)))

(defn next-event []
  (let [e (first @*queue*)]
  (reset! *queue* (rest @*queue*))
  e))

(defn get-next-event []
  (first *queue*)
  (swap! *queue* rest ))

; returns true  if there are events 
(defn has-events? [] (= false (empty? @*queue*)))
(defn no-events? [] (not (hasevents?)))


