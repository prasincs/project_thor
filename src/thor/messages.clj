(ns thor.messages
  (:require thor.queue ) 
  )

(def *TTL* (atom 10))
(def *DEFAULT_PACKET_SIZE* 64)
(def *messages* (atom ()))


(defrecord Message [id message attrs])

(defn get-message-content [msg]
  (get msg :message))

(defn get-message-attr [msg attr]
  (if (contains? (get msg :attrs) attr)
    (-> msg :attrs attr)
    ))
(defn get-message-network-attrs 
  [msg]
    (-> msg :network-attrs ))

(defn get-message-path [msg]
  (get-message-attr msg :nodes)
  )

(defn build-message-attrs 
  "Build a hash map of the attributes we care about
  like hops, size of the message, nodes, etc"
  [from to attrs]
  (let [get-attr 
        #(if (contains? attrs %1) 
           (%1 attrs ) 
           %2)]
    {:ttl (get-attr :ttl @*TTL*)
     :size (get-attr :size *DEFAULT_PACKET_SIZE*)
     :hops (get-attr :hops 0)
     :from (get-attr :from from)
     :to   (get-attr :to to)
     :nodes (get-attr :nodes [{:time (get-attr :time 0)
                               :path [from to]}])
     :time (get-attr :time 0)
     :network-attrs (get-attr :network-attrs {}) ; use this to store networking details
     }
    )) 

; only dealing with creation and modification of messages
; moved sending to thor.net.wireless
(defn create-message [message from to &[attrs]]
  (if (or (nil? from) (nil? to)) 
    (throw (Error. "Need from and to Nodes")))
  (let [

        msg-attrs (build-message-attrs 
                    from to attrs)
        msg
        (Message.  
          (inc (count @*messages*)) 
          "test" msg-attrs)]
    (swap! *messages* conj msg)
    msg)
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


