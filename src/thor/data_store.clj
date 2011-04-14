(ns thor.data-store)
; This is a way to globally store data 
; Format
; :key <HASH>
; :value <ANYTHING>
; :nodes (List of nodes)
;

(defstruct data-item :key :value :nodes)

(defn init-data-store [] (atom {}))


(defn add-node-to-data-item [item & nodes]
  (reset! (:nodes item) (set concat ( (set nodes)) )

(defn init-data-item [k v & nodes] 
  (let [ item (struct-map data-item :key k :value v :nodes (atom ()) )]
  (if (not (empty? nodes))
    (add-node-to-data-item item nodes)
    )
    item
    ))



