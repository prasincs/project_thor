(defdevice phone-1 {
                   :type "phone"
                   :memory "100M"
                   :range "100"
                   :power {:rx 0.01 
                           :tx 0.01} ; in watts
                    
                   :gain {:r 5.15
                          :t 5.15}
                    }
                    ;http://en.wikipedia.org/wiki/Lithium-ion_battery 
                    :battery {
                              :type "Li-ion"
                              :voltage 3.6 
                              :specific-energy 0.46
                              :efficiency 0.8 ; 80%
                             }
           )
