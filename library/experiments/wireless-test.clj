(use 'thor.lang :reload-all)

(defduration 100)

(defdevice phone-1 {
                    :type "phone"
                    :memory "100M"
                    :range "100"
                    :power {:rx 5 
                            :tx 5} ; in volts

                    :gain {:r 5.15 
                           :t 5.15}

                    :battery {
                              :type "Li-ion"
                              :voltage 3.6 
                              :specific-energy 0.46
                              :efficiency 0.8 ; 80%
                              }
                    }

           )

(defexpt wireless-expt
         {
          :title "Wireless Experiment"
          ;:devices {:types ["phone-1"]
          ;          :number 1e4
          ;          :sample 'random
          ;          }
          :area {:width 100 :height 100}
          :direction 0 ; eventually want to override for particular device
          :speed 0
          }
         )
; For the values of Path Propagation Loss - look at
; Wireless Networks - Local and Wireless Networks (Ivan Marsic ) [Pg 33]
;
; wireless is there to allow for expansion into wired network
(defmedium wireless {
            :bandwidth "1.5 Mbps"
            :frequency "2.4 GHz"
            :channel 6 ; not implementing - assumed default
            :propagation-loss-expt 2 ; free space (default)
            :loss [] ; determines how much power 
                     ; loss is to be calculated 
                     ; (propagation loss automatically done )
            :noise 0 ; noise ignored
            })


(def transmitter (create-node 
                   {:device phone-1 
                    :location {:x 50 :y 0}} ; start at very top
                   ))

(def receiver (create-node 
                {:device phone-1
                 :location {:x 50 :y 1}}
                )) ; start at distance 1 away from transmitter

(def power-loss-time (atom ()))


(at-start (do 
                 ))

(at-end (do 
          (println "Results" )
          (println @power-loss-time)
          ))


(every 1 (do
           (move-node receiver + {:x 0 :y 1})
           (swap! power-loss-time conj 
                  (:power-received 
                    (get-message-network-attrs 
                      (send-message "test" transmitter receiver ))))
           ))

(simulation-run)
