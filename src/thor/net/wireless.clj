(ns thor.net.wireless
  (:use thor.node 
        thor.network
        thor.messages)
  (:require thor.queue 
            ) ; want queue actions to be more explicit
  )

(use '[clojure.contrib.math :only (expt)])
(def SPEED_LIGHT 3e8 )
; essentially the same thing as 
; *wireless-network* in thor.lang
; doing this for separation/testing and 
; possibly future proofing
(def *wireless-network-attrs* 
  (atom 
  {
   :bandwidth 1e8
   :frequency 2.4e8
   :channel 6
   :propagation-loss-expt 2
   :loss []
   :noise 0
  }))

; supposed to be directly applied from thor.lang when network is invoked
(defn set-wireless-attributes [attrs] 
  (reset! *wireless-network-attrs* attrs)
  )

(defn get-wavelength 
  "Get the wavelength from given frequency in Hz" 
  [freq]  (/ SPEED_LIGHT freq))

; for free space propagation loss exponent is 2 anyways
(defn friis-power-received [{:keys [freq distance 
                                    power-t gain-t gain-r propagation-loss-expt] 
                             :or {propagation-loss-expt 2}}]  
  (* 
    (expt 
      (/ (get-wavelength freq)      
         (* 4 Math/PI distance)) 
      propagation-loss-expt) 
    gain-t gain-r power-t)
  )

(defn calculate-network-attrs
  "Calculate the network parameters we'll see if a message 
  of certain size were to be sent from point A to point B"
  [from to ]
    {
      :power-received (friis-power-received {:freq }
                       )     
     }
  )



(defn send-network-message 
  "Send a network message from a node to other"
  [from to message  &[attrs]]
  (let [msg (create-message message from to attrs)]
      (assoc msg :network-attrs 
             (-> {} (assoc 
                      :power-received 
                      (/ 1 
                         (:time attrs) 
                            )))
               )
             ))
