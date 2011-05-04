(use 'thor.dht.simple 'thor.lang :reload-all )
(use '[clojure.contrib.logging :only (debug)])
(use '[clojure.contrib.math :only (expt)])
(use '(incanter core charts))

(defduration 11)
(defsamples 10)
(defkeyspace (expt  2 32))
(defexpt simple-dht
         {
          :title "Simple DHT"
          :devices {:types ["phone-1"]
                    :number 10
                    :sample 'random
                    }
          :area {:width 100 :height 100}
          :direction 0 ; eventually want to override for particular device
          :speed 0
          }
         )

(defn create-expt-data []
  (let [expt-data (atom {})]
    (loop []
      (debug (str "expt data " (count @expt-data)))
      (swap! expt-data 
             assoc (long (rand (get-keyspace) )) 
             { 
              :data (long (rand (get-total-devices)))} )
      (if (< (count (keys @expt-data)) 
             (get-total-samples)) 
        (recur))
      )
    ;(prn @expt-data)
    @expt-data))

; todo-> define *total-devices*
(def *expt-data* (create-expt-data))

(create-overlay {:num (get-total-devices) 
                 :size (:width (get-experiment-area)) })

(def hop-count-list (atom ()))

(at-end (do

          (println "Hop Count Average" 
                   (/ (sum @hop-count-list) (get-total-samples)))
          ) )
(show-nodes)
(at 10
    (do
      (println "Storing datas")
      (println "Keyspace " (get-keyspace))
      (doseq [k (keys *expt-data*)]
        (let [item (get *expt-data* k)]
          (println k item)
          (swap! hop-count-list conj 
            (-> (store 0 
                 k 
                 (item :data))
          :message :attrs :hops) )
      ))))

;(at 15
;       (let [data (first *expt-data*)]
;         (find-node 0 (:key data) )
;         )
;       )
;
(simulation-run)
;(display-results)
