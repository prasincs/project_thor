(ns thor.data-store
  (:require thor.utils))
; This is a way to globally store data 
; Format
; :key <HASH>
; :value <ANYTHING>
; :nodes (List of nodes)
;
(defn init-data-store [] (atom {}))

(def *data-store* (init-data-store))

(defstruct data-item :value :nodes)
(defn init-data-item [v & node-ids] 
  (struct-map data-item :value v :nodes (ref node-ids))
  )

(defn store-data [node-id k v]
  (let [data-item (get @*data-store* k)]  
    (if (nil? data-item)
      (swap! *data-store* assoc k (ref (init-data-item v node-id )))
      (let [nodes (:nodes @data-item)]
        (println (str "nodes" @nodes " " node-id))
        (if (not 
            (thor.utils/in-seq? node-id
            @nodes ))
          (dosync
          (alter nodes concat (list node-id)))
      ))
      )))

(defn get-data-item [k]
  (get @*data-store* k)
  )

(defn add-node-to-data-item [item & nodes]
  ;(if  (nil? (get-data-item (:key item)))

  ;))
  )




;(defn get-data [node-id k]
;  (if (contains? @*data-store* k)
;    (if (contains? )
;    )
;  )
;
