(ns thor.queue)

(defstruct event :task :time )


(defn create-event [t task] { :time t
                              :task task
                                 })

(defn create-queue [] (atom ()))

(def *queue* (atom ()))

(defn add-events-to-queue [e] 
  (swap! *queue* concat e)
  (reset! *queue* (sort-by  :time @*queue*))
  )


(defn get-next-event []
  (first *queue*)
  (swap! *queue* rest ))


(defn noevents? [] (= true (empty? @*queue*)))


