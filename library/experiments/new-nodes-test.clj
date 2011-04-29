(use 'thor.lang 
     'thor.node 
     :reload-all)

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

(def t (new-node {:device "phone-1"}))
(prn t)
