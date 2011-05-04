(use 'thor.lang 
     'thor.messages
     'thor.net.wireless
     'thor.node
    '(incanter core charts stats) 
     :reload-all)
(defduration 100)

(defdevice "phone-1" {
                    :type "phone"
                    :memory "100M"
                    :range "100"
                    :current
                      {:no-network 100 ; mA
                       :network 200 ;mA
                      }
                    :power {:rx 5 
                            :tx 10} ; in watts

                    :gain {:r 10 
                           :t 10}

                    :battery {
                              :type "Li-ion"
                              :voltage 3.6 
                              :capacity 100 ;mAh
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
                    :location {:x 250 :y 10}} ; start at very top
                   ))

(def receiver (new-node 
                {:device "phone-1"
                 :location {:x 250 :y 11}}
                )) ; start at distance 1 away from transmitter

(def power-loss-time (atom ()))
(def distances (atom ()))
(def battery-capacity-list (atom ()))
(at-start (do 
            (node-viewer-start)
                 ))

(at-end (do 
          ;(println "Results" )
          ;(println @power-loss-time)
          (let [ distance (reverse
                            @distances)
                power (reverse 
                        @power-loss-time)
                battery (reverse 
                          @battery-capacity-list)
                power-plot (xy-plot 
                             distance power
                             :title "Power Degradation over distance"
                             :series-label "power" 
                             :shape 1
                             :legend true)
                battery-plot (xy-plot 
                               distance battery
                               :title "Battery Degradation over distance"
                               :series-label "battery capacity" 
                               :shape 1
                               :legend true)
                ]
            (view power-plot)
            (view battery-plot)   
            ;(add-lines plot distance battery :shape 2)

            )
          
          )
        )


(every 1 (do
           (move-node receiver + {:x 0 :y 1})
           (println (get-current-time) (:location @receiver))
           (swap! distances conj 
                  (get-distance-between-nodes
                    transmitter receiver))
           ;(if (> (get-battery-capacity receiver ) 0)
           (deduct-power-usage receiver 
                               {:time 10
                                :network-used? true})
           ;)
           (swap! battery-capacity-list conj 
                  (get-battery-capacity receiver)
                  )
           ;(println (get-battery-capacity receiver))
           (swap! power-loss-time conj 
                  (:power-received 
                    (get-message-network-attrs 
                      (send-network-message 
                        "test" 
                        transmitter 
                        receiver 
                        {:time (get-current-time)})
                      )))
            (node-viewer-update-nodes {:text "Test"})
           ))

(simulation-run)
