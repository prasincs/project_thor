(ns thor.data-store
  (:require thor.utils))
; This is a way to globally store data 
; Format
; :key <HASH> => { :value <ANYTHING> :nodes (List of nodes)}
;
(defn init-data-store [] (atom {}))

(def *data-store* (init-data-store))

(defstruct data-store-item :value :nodes)

(defn init-data-store-item [v & node-ids] 
  (struct-map data-store-item :value v :nodes (ref node-ids))
  )

(defn get-data-item [k]
  (get @*data-store* k)
  )

(defn node-contains-data? [data-item node-id]
  (let [nodes (:nodes data-item)]
    (thor.utils/in-seq? node-id @nodes)
    )
  )

(defn get-data-in-node [node-id k]
  (let [data-item (get-data-item k)]
    (if (nil? data-item) nil
      (if (node-contains-data? @data-item node-id)
        @data-item)
  )))


(defn store-data [node-id k v]
  (let [data-item (get-data-item k)]  
    (if (nil? data-item)
      (swap! *data-store* assoc k (ref (init-data-store-item v node-id )))
        (if (not (node-contains-data? @data-item node-id))  
        (dosync
            (alter (:nodes @data-item) concat (list node-id)))
          ))
      ))







