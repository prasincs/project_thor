(use 'thor.lang 
     'thor.node 
     '(incanter core charts)
     :reload-all)

(defduration 100000)

(defdevice "phone-1" {
                    :type "phone"
                    :memory "100M"
                    :range "100"
                    :current
                      {:no-network 100 ; mA
                       :network 200 ;mA
                      }
                    :power {:rx 5 
                            :tx 5} ; in watts

                    :gain {:r 5.15 
                           :t 5.15}

                    :battery {
                              :type "Li-ion"
                              :voltage 3.6 
                              :capacity 100 ;mAh
                              :specific-energy 0.46
                              :efficiency 0.8 ; 80%
                              }
                    }

           )

(def t 
  (new-node 
    {:device "phone-1"}))

(def battery-capacity-list
  (atom ()))

(at-end (do
          
          ))

(every 1 
       (do
         (if (<= (get-battery-capacity t) 0)
           (do

             (let [time (range 1 (get-current-time))
                   battery (reverse @battery-capacity-list)]
               (view 
                 (line-chart 
                   time battery))
               )

             (prn "Battery dead")
             (prn (get-current-time))
             (end-simulation)
             )

           (do 
             (deduct-power-usage t)
             (swap! battery-capacity-list conj 
                    (get-battery-capacity t))

             ;(prn (get-current-time) 
             ;     " " (get-battery-capacity t))

             )
           )))

(every 5 (do
           (deduct-power-usage t true)
           ))


;(prn t)
(simulation-run)
