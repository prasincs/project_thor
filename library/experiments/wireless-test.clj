(use 'thor.lang 
     'thor.messages
     'thor.net.wireless
    '(incanter core charts stats) 
     :reload-all)
(defduration 50)

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
                              :capacity 1600 ;mAh
                              :specific-energy 0.46
                              :efficiency 0.8 ; 80%
                              }
                    }

           )

(defexpt wireless-expt
         {
          :title "Wireless Experiment"
          ;:devices {:types ["phone-1"]
          ;          :number 10
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


(def transmitter (new-node 
                   {:device "phone-1" 
                    :location {:x 50 :y 0}} ; start at very top
                   ))

(def receiver (new-node 
                {:device "phone-1"
                 :location {:x 50 :y 1}}
                )) ; start at distance 1 away from transmitter

(def power-loss-time (atom ()))
(def distances (atom ()))

(at-start (do 
                 ))

(at-end (do 
          (println "Results" )
          (println @power-loss-time)
          (let [ distance (reverse @distances)
                power (reverse @power-loss-time)]
          (view 
            (xy-plot 
              distance power 
              :title "Power Degradation over distance"
              :series-label "results" 
              :legend true))
          )
        ))


(every 1 (do
           (move-node receiver + {:x 0 :y 1})
           (swap! distances conj (get-distance-between-nodes transmitter receiver))
           (swap! power-loss-time conj 
                  (:power-received 
                    (get-message-network-attrs 
                      (send-network-message 
                        "test" 
                        transmitter 
                        receiver 
                        {:time (get-current-time)})
                      )))
           ))

(simulation-run)
