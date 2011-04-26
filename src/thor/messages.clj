(ns thor.messages)

(def *TTL* (atom 10))
(def *DEFAULT_PACKET_SIZE* 64)
(def *messages* (atom ()))


(defrecord Message [id message attrs])

(defn build-message-attrs [attrs]
  (let [get-attr 
        #(if (contains? attrs %1) 
           (%1 attrs ) 
           %2)]
  {:ttl (get-attr :ttl @*TTL*)
   :size (get-attr :size *DEFAULT_PACKET_SIZE*)
   :hops (get-attr :hops 0)
   :nodes (get-attr :nodes [])
   :time (get-attr :time 0)
   }
  ))

(defn create-message [message from to &[attrs]]
  (if (or (nil? from) (nil? to)) 
          (throw (Error. "Need from and to Nodes")))
  (let [
        msg-attrs (build-message-attrs 
                    attrs)
        msg
  (Message.  
    (inc (count @*messages*)) 
    "test" msg-attrs)]
    (swap! *messages* conj msg)
        msg)
  )

(defn send-message [message from to &[attrs]]
                    
                    )

(defn get-hash [type_t data]
  (.digest 
    (java.security.MessageDigest/getInstance 
      type_t) 
    (.getBytes data) ))

(defn sha1-hash [data]
  (get-hash "sha1" data))

(defn get-hash-str [data-bytes]
  (apply str 
         (map #(.substring 
                 (Integer/toString 
                   (+ (bit-and % 0xff) 0x100) 
                   16) 
                 1) 
              data-bytes)))


