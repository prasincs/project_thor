(ns #^{:doc "A description parser for thor"}
  thor.lang
  (:use thor.queue)
  )

(def *devices* (atom {}))

(defn add-device [n d]
  (reset! *devices* (conj @*devices* [n d])))

(defmacro defdevice 
  "Defines a device with name and a map of attributes"
  [n attrs]
  `(add-device '~n ~attrs ))

(def *experiment* (atom {}))
(def *duration* (atom 0))

(defmacro defduration [n]
  `(reset! *duration* ~n))

(defn create-experiment 
  "Create an experiment based on attributes"
  [n attrs]
  (reset! *experiment* attrs)
  )


(defn create-every-events  [t f]
  (let [events (atom ())]
    (loop [i 0]
      (reset! events (conj @events (create-event i f)))
      (when (< i @*duration*) (recur (+ i t)))
      )
  @events) )

(defmacro every [t f]
  `(create-every-events ~t '~f)
)

(defn on-function [attrs]
  attrs
  )

(defmacro on-new-neighbor [attrs]
  `(on-function ~attrs)

  )



(defmacro defexpt 
  "Defines an experiment"
  [n attrs]
    `(create-experiment '~n ~attrs)
  )


(defmacro defdata [n size]
  (cond
    (= size 'B) `(* ~n 1)
    (= size 'K) `(* ~n 1024)
    (= size 'M) `(* ~n 1024 1024)
    (= size 'G) `(* ~n 1024 1024 1024)
    )
  )

(defn load-experiment
  "Load experiment data from file f"
  [f]
 (let [oldns (ns-name *ns*)]

    (in-ns  'thor.lang)
    (binding [*experiment* (atom {})]
    (load-file f)
    (in-ns oldns)
    @*experiment*
  ))
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
