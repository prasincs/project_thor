(ns thor.dht.aodv "AODV implementation" 
  (:require clojure.core)
  (:require thor.node)
  )

(def RREQ 1)
(def RREP 2)
(def RERR 3)
(def RERR-ACK 4)

; based on http://www.ietf.org/rfc/rfc3561.txt
(defstruct aodv-rreq-message
           :type
           :flags
           :hops
           :rreq-id
           :dest-node
           :dest-seq
           :orig-node
           :orig-seq
           :payload
           )

(defstruct aodv-rrep-message
           :type
           :flags
           :prefix-size
           :hop-count
           :dest-node
           :dest-seq
           :orig-node
           :lifetime
           :payload
           )

; RERR message is sent whenever a link break causes 
; one or more destinations to be become unreachable 
; from some of the node's neighbors
(defstruct aodv-rerr-message
           :type
           :flags
           :dest-count
           :unreachable-node
           :unreachable-seq
           )



