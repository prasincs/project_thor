(use 'thor.dht.simple 'thor.lang :reload-all )
(use 'clojure.contrib.logging)
(use '[clojure.contrib.math :only (expt)])

(defduration 100)
(defsamples 10)
(defkeyspace (expt  2 10))
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
      (swap! expt-data assoc (int (rand (get-keyspace) )) 
             {:node (int (rand (get-total-devices))) 
              :data (int (rand (get-total-devices)))} )
      (if (< (count (keys @expt-data)) 
             (get-total-samples)) 
        (recur))
      )
    ;(prn @expt-data)
    @expt-data))

; todo-> define *total-devices*
(def *expt-data* (create-expt-data))

(create-overlay {:num (get-total-devices) :size 
                 (:width (get-experiment-area)) })


(at 10
    (do
      (println "Storing datas")

      (doseq [k (keys *expt-data*)]
        (let [item (get *expt-data* k)]
          (println k item)
          (store (item :node) 
                 k 
                 (item :data))
          ))
      ))

;(at 15
;       (let [data (first *expt-data*)]
;         (find-node 0 (:key data) )
;         )
;       )
;
(simulation-run)
;(display-results)
