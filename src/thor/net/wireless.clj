(ns thor.net.wireless
  (:use thor.node thor.network clojure.contrib.generic.math-functions)
  )

(use '[clojure.contrib.math :only (expt)])
(def SPEED_LIGHT 3e8 )

  
  (defn get-wavelength 
  "Get the wavelength from given frequency in Hz" 
  [freq]  (/ SPEED_LIGHT freq))

(defn log_10 [x] 
  (/ (log x) 
     (log 10)))

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


