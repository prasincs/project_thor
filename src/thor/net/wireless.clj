(ns thor.net.wireless
  (:use thor.node 
        thor.network
        thor.messages
        clojure.contrib.logging)
  (:require thor.queue 
            ) ; want queue actions to be more explicit
  )

(use '[clojure.contrib.math :only (expt)])
(def SPEED_LIGHT 3e8 )
; essentially the same thing as 
; *wireless-network* in thor.lang
; doing this for separation/testing and 
; possibly future proofing
;
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
  (debug "Setting wireless attributes")
  (println attrs)
  (reset! *wireless-network-attrs* attrs)
  )

(defn get-wireless-attr [attr]
  (debug "Getting wireless attributes")
  (println @*wireless-network-attrs*)
  (println attr "->"
  (get @*wireless-network-attrs* attr))
  (get @*wireless-network-attrs* attr)
  )

(defn get-wavelength 
  "Get the wavelength from given frequency in Hz" 
  [freq]  
  (println "Getting wavelength")
  (println freq)
  (/ SPEED_LIGHT freq))

; for free space propagation loss exponent is 2 anyways
(defn friis-power-received [{:keys [freq distance 
                                    power-t gain-t gain-r propagation-loss-expt] 
                             :or {propagation-loss-expt 2}}]  
  (debug "Friis power received calculation")
  (println "distance ->" distance)
  (println "freq ->" freq)
  (println "power-t ->" power-t)
  (println "gain-t ->" gain-t)
  (println "gain-r ->" gain-r)
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
  (debug "calculate-network-attrs")
    {
      :power-received (friis-power-received 
                        {:freq (get-wireless-attr  :frequency)
                         :distance (get-distance from to)
                         :power-t 
                            (-> (get-node-device-attrs from) :power :tx)
                         :gain-t 
                            (-> (get-node-device-attrs from) :gain :t )
                         :gain-r 
                            (-> (get-node-device-attrs to) :gain :r)

                         }
                       )     
     }
  )



;(defn send-network-message 
;  "Send a network message from a node to other"
;  [from to message  &[attrs]]
;  (let [msg (create-message message from to attrs)]
;      (assoc msg :network-attrs 
;             (-> {} (assoc 
;                      :power-received 
;                      (/ 1 
;                         (:time attrs) 
;                            )))
;               )
;             ))

(defn send-network-message 
  "Send a network message from a node to other"
  [message from to  &[attrs]]
  (let [msg (create-message message from to attrs)]
      (assoc msg :network-attrs 
             (calculate-network-attrs @from @to)     
             )
             ))
