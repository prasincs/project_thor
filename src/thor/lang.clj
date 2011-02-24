(ns #^{:doc "A description parser for thor"}
  thor.lang)

(def *devices* (atom {}))

(defn add-device [n d]
  (reset! *devices* (conj @*devices* [n d])))

(defmacro defdevice 
  "Defines a device with name and a map of attributes"
  [n attrs]
  `(add-device '~n ~attrs ))

(def *experiment* (atom {}))


(defn create-experiment 
  "Create an experiment based on attributes"
  [n attrs]
  (swap! *experiment* [n attrs])
  )


(defmacro defexpt 
  "Defines an experiment"
  [n attrs]
  `(create-experiment '~n ~attrs)
  )





(defn load-device 
  "Loads a device from file f."
  [f]
  (let [oldns (ns-name *ns*)]
    (in-ns  'thor.lang)
    (binding [*devices* (atom {})]
    (load-file f)
    (in-ns oldns)
    ;just return all the devices found
    ;this could be then concat into another list 
    @*devices*
  )))
