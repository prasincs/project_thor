(use '[ thor.dht.simple thor.lang])

(defduration 100)
(defsamples 100)
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
  (let [expt (atom {})]
    (loop []
      (swap! expt assoc (int (rand *keyspace* )) 
             {:node (int (rand *total-devices*)) 
              :data (int (rand *total-devices))} )
      (if (< (count (keys expt)) *total-samples*) (recur))
      )
    ))


; todo-> define *total-devices*
(def *expt-data* (create-expt-data))
(create-overlay {:num *total-devices* :size (:width *expt*) })

(at 10 
    (doseq [item expt]
      (store (item :node) (item :key) (item :value))
      )
    )

(every 5
       (let [data (first *expt-data*)]
         (find-node 0 (:key data) )
         )
       )

(simulation-run)
(display-results)
